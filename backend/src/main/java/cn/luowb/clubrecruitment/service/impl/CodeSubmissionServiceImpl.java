package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ZipUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.common.util.QiniuStorageService;
import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import cn.luowb.clubrecruitment.dao.entity.AttachmentDO;
import cn.luowb.clubrecruitment.dao.entity.CodeSubmissionDO;
import cn.luowb.clubrecruitment.dao.mapper.ApplicationMapper;
import cn.luowb.clubrecruitment.dao.mapper.CodeSubmissionMapper;
import cn.luowb.clubrecruitment.dto.req.CodeSubmissionReqDTO;
import cn.luowb.clubrecruitment.dto.resp.CodeSubmissionRespDTO;
import cn.luowb.clubrecruitment.service.AttachmentService;
import cn.luowb.clubrecruitment.service.CodeSubmissionService;
import cn.luowb.clubrecruitment.service.MajorMappingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * @author heimenkyou
 * @description 针对表【code_submission(代码提交记录表)】的数据库操作Service实现
 * @createDate 2025-10-09 08:17:32
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CodeSubmissionServiceImpl extends ServiceImpl<CodeSubmissionMapper, CodeSubmissionDO>
        implements CodeSubmissionService {

    private final CodeSubmissionMapper codeSubmissionMapper;
    private final QiniuStorageService qiniuStorageService;
    private final AttachmentService attachmentService;
    private final ApplicationMapper applicationMapper;
    private final MajorMappingService majorMappingService;

    /**
     * 文件大小限制：100MB
     */
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

    @Override
    @Transactional
    public CodeSubmissionRespDTO createCodeSubmission(CodeSubmissionReqDTO requestParam) {
        log.info("开始处理代码提交，学号: {}", requestParam.getStudentId());

        // 文件大小检测
        validateFileSize(requestParam.getCodeFile(), "代码压缩包");
        validateFileSize(requestParam.getVideoFile(), "演示视频");
        // 检测压缩包结构是否符合要求
        validateZipStructure(requestParam.getCodeFile());

        // 根据学号查询姓名
        ApplicationDO application = getApplicationByStudentId(requestParam.getStudentId());

        // 检查是否已有提交记录，如果有则删除旧文件
        CodeSubmissionDO existingSubmission = getExistingSubmission(requestParam.getStudentId());
        if (existingSubmission != null) {
            deleteOldFiles(existingSubmission);
        }

        // 上传代码压缩包
        AttachmentDO codeAttachment = uploadFile(
                requestParam.getCodeFile(), requestParam.getStudentId(), "code", "file", "代码压缩包");

        // 上传演示视频
        AttachmentDO videoAttachment = uploadFile(
                requestParam.getVideoFile(), requestParam.getStudentId(), "video", "video", "演示视频");

        // 保存代码提交信息
        CodeSubmissionDO submission = saveSubmission(requestParam, application, existingSubmission);

        codeAttachment.setRefType("code_submission").setRefId(submission.getId());
        videoAttachment.setRefType("code_submission").setRefId(submission.getId());
        attachmentService.saveBatch(List.of(codeAttachment, videoAttachment));

        return CodeSubmissionRespDTO.builder()
                .id(submission.getId())
                .studentId(requestParam.getStudentId())
                .name(application.getName())
                .description(requestParam.getDescription())
                .codeUrl(qiniuStorageService.buildPublicUrl(codeAttachment.getObjectKey()))
                .videoUrl(qiniuStorageService.buildPublicUrl(videoAttachment.getObjectKey()))
                .createTime(submission.getCreateTime())
                .build();
    }

    /**
     * 保存代码提交信息
     */
    private CodeSubmissionDO saveSubmission(CodeSubmissionReqDTO requestParam, ApplicationDO application,
                                            CodeSubmissionDO existingSubmission) {
        if (existingSubmission != null) {
            existingSubmission.setDescription(requestParam.getDescription());
            codeSubmissionMapper.updateById(existingSubmission);
            log.info("更新代码提交记录，ID: {}", existingSubmission.getId());
            return existingSubmission;
        } else {
            CodeSubmissionDO submission = new CodeSubmissionDO()
                    .setStudentId(requestParam.getStudentId())
                    .setName(application.getName())
                    .setDescription(requestParam.getDescription());
            codeSubmissionMapper.insert(submission);
            log.info("创建新的代码提交记录，ID: {}", submission.getId());
            return submission;
        }
    }

    /**
     * 验证文件大小
     */
    private void validateFileSize(org.springframework.web.multipart.MultipartFile file, String fileType) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ClientException(String.format("%s大小超过100MB限制", fileType));
        }
        log.info("{}文件大小验证通过: {} bytes", fileType, file.getSize());
    }

    /**
     * 验证压缩包结构是否符合要求
     * 检查是否为zip格式，能否正确解压，是否为空包，结构是否套文件夹
     */
    private void validateZipStructure(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 1. 检查扩展名
            String fileName = file.getOriginalFilename();
            if (!FileNameUtil.extName(fileName).equalsIgnoreCase("zip")) {
                throw new ClientException("代码压缩包必须是zip格式");
            }

            // 2. 复制输入流用于验证（避免消耗原始流）
            byte[] fileBytes = IoUtil.readBytes(inputStream); // 一次性读取到内存

            // 3. 创建临时文件用于解压检查
            java.io.File tempFile = java.io.File.createTempFile("temp_zip_", ".zip");
            try {
                Files.write(tempFile.toPath(), fileBytes); // 写入临时文件

                java.io.File tempDir = java.io.File.createTempFile("temp_unzip_", "");
                tempDir.delete();
                tempDir.mkdirs();

                try {
                    ZipUtil.unzip(tempFile, tempDir);

                    java.io.File[] files = tempDir.listFiles();
                    if (files == null || files.length == 0) {
                        throw new ClientException("压缩包为空，请检查文件内容");
                    }

                    if (files.length == 1 && files[0].isDirectory()) {
                        throw new ClientException("压缩包结构不符合要求：不能直接套文件夹，请将项目文件直接放在压缩包根目录下");
                    }

                    log.info("压缩包结构验证通过，包含 {} 个顶层项目", files.length);

                } finally {
                    FileUtil.del(tempDir);
                }

            } finally {
                FileUtil.del(tempFile);
            }

        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("压缩包验证失败: {}", e.getMessage());
            throw new ClientException("压缩包验证失败：" + e.getMessage());
        }
    }

    /**
     * 根据学号查询报名信息
     */
    private ApplicationDO getApplicationByStudentId(String studentId) {
        ApplicationDO application = applicationMapper.selectOne(
                new LambdaQueryWrapper<ApplicationDO>().eq(ApplicationDO::getStudentId, studentId));
        if (application == null) {
            throw new ClientException("ฅ^•ﻌ•^ฅ 你不是社团新成员吧, 盯~\uD83D\uDC40\uD83D\uDEAB"); // 👀🚫
        }
        return application;
    }

    /**
     * 获取已存在的提交记录
     */
    private CodeSubmissionDO getExistingSubmission(String studentId) {
        return codeSubmissionMapper.selectOne(
                Wrappers.<CodeSubmissionDO>lambdaQuery()
                        .eq(CodeSubmissionDO::getStudentId, studentId)
        );
    }

    /**
     * 删除旧文件
     */
    private void deleteOldFiles(CodeSubmissionDO existingSubmission) {
        try {
            List<AttachmentDO> attachments = attachmentService.list(
                    Wrappers.<AttachmentDO>lambdaQuery()
                            .eq(AttachmentDO::getRefId, existingSubmission.getId())
                            .eq(AttachmentDO::getRefType, "code_submission"));

            for (AttachmentDO attachment : attachments) {
                // 历史 MinIO 记录没有可靠对象键，只清理数据库记录。
                if (StringUtils.hasText(attachment.getObjectKey())) {
                    qiniuStorageService.delete(attachment.getObjectKey());
                }
                log.info("删除旧附件: {}", attachment.getObjectKey());
            }
            attachmentService.removeByIds(attachments.stream().map(AttachmentDO::getId).toList());
        } catch (Exception e) {
            throw new ServiceException("删除旧文件失败: " + e.getMessage());
        }
    }

    /**
     * 上传文件到七牛云并构造附件记录
     */
    private AttachmentDO uploadFile(MultipartFile file, String studentId, String folder, String type, String fileType) {
        try {
            String name = majorMappingService.buildClassName(studentId);
            String fileExtension = FileUtil.extName(file.getOriginalFilename());
            String fileName = String.format("%s.%s", name, fileExtension);
            String objectKey = qiniuStorageService.createObjectKey(folder, fileName);
            qiniuStorageService.upload(file, objectKey);
            log.info("{}上传成功: {}", fileType, objectKey);
            return new AttachmentDO()
                    .setObjectKey(objectKey)
                    .setOriginalName(fileName)
                    .setType(type)
                    .setMimeType(StringUtils.hasText(file.getContentType())
                            ? file.getContentType() : "application/octet-stream")
                    .setSize(file.getSize())
                    .setStatus("ready")
                    .setUsage(folder);
        } catch (Exception e) {
            log.error("{}上传失败: {}", fileType, e.getMessage());
            throw e;
        }
    }

}
