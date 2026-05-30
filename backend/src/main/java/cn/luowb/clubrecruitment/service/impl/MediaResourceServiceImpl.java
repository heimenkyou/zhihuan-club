package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.util.MinioService;
import cn.luowb.clubrecruitment.dao.entity.MediaResourceDO;
import cn.luowb.clubrecruitment.dao.mapper.MediaResourceMapper;
import cn.luowb.clubrecruitment.dto.req.MediaUploadReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MediaResourceRespDTO;
import cn.luowb.clubrecruitment.service.MediaResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【media_resource(媒体资源表)】的数据库操作Service实现
 * @createDate 2025-08-28 10:43:49
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MediaResourceServiceImpl extends ServiceImpl<MediaResourceMapper, MediaResourceDO>
        implements MediaResourceService {
    private final MediaResourceMapper mediaResourceMapper;
    private final MinioService minioService;

    @Override
    public MediaResourceRespDTO uploadMedia(MediaUploadReqDTO requestParam) {
        // todo 转成webp 压缩图片
        // todo 校验文件类型
        MultipartFile file = requestParam.getFile();
        // 实现文件上传逻辑，保存到存储服务（如本地存储、OSS等）
        // 生成唯一文件名
        String timePrefix = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        String fileName = String.format("%s_%s", timePrefix, file.getOriginalFilename());
        // 指定文件夹
        String folder = "media";
        String fileUrl = minioService.upload(file, fileName, folder);
        if (fileUrl == null) {
            throw new RuntimeException("文件上传失败, 接口还未完成");
        }

        // 保存到数据库
        if (StrUtil.isBlank(requestParam.getTitle())) {
            requestParam.setTitle(file.getOriginalFilename());
        }
        if (StrUtil.isBlank(requestParam.getDescription())) {
            requestParam.setDescription("上传于 " + DateUtil.formatLocalDateTime(LocalDateTime.now()));
        }
        MediaResourceDO mediaResourceDO = new MediaResourceDO()
                .setType(getFileType(file.getContentType()))
                .setUrl(fileUrl)
                .setTitle(requestParam.getTitle())
                .setDescription(requestParam.getDescription());
        mediaResourceMapper.insert(mediaResourceDO);

        return BeanUtil.toBean(mediaResourceDO, MediaResourceRespDTO.class);
    }

    private String getFileType(String contentType) {
        if (contentType == null) {
            return "other";
        }
        if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("video/")) {
            return "video";
        } else if (contentType.startsWith("audio/")) {
            return "audio";
        }
        return "other";
    }

    @Override
    public List<MediaResourceRespDTO> getUnreferencedMedia() {
        List<MediaResourceDO> mediaList = mediaResourceMapper.selectUnreferenced();
        return mediaList.stream()
                .map(media -> BeanUtil.toBean(media, MediaResourceRespDTO.class))
                .toList();
    }

    @Override
    public void delete(Long id) {
        MediaResourceDO mediaResourceDO = mediaResourceMapper.selectById(id);
        if (mediaResourceDO == null) {
            throw new ClientException("媒体资源不存在");
        }
        minioService.delete(mediaResourceDO.getUrl());
        // 删除数据库记录
        mediaResourceMapper.deleteById(id);
    }
}




