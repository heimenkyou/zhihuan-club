package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.util.QiniuStorageService;
import cn.luowb.clubrecruitment.dao.entity.AttachmentDO;
import cn.luowb.clubrecruitment.dao.mapper.AttachmentMapper;
import cn.luowb.clubrecruitment.dto.req.AttachmentUploadTokenReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentUploadTokenRespDTO;
import cn.luowb.clubrecruitment.service.AttachmentService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

/**
 * 附件业务服务实现。
 */
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, AttachmentDO>
        implements AttachmentService {
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_READY = "ready";

    private final QiniuStorageService qiniuStorageService;

    @Override
    public AttachmentUploadTokenRespDTO createUploadToken(AttachmentUploadTokenReqDTO requestParam) {
        if (!requestParam.getMimeType().toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new ClientException("只允许上传图片");
        }
        if (requestParam.getSize() > MAX_IMAGE_SIZE) {
            throw new ClientException("图片大小不能超过10MB");
        }

        String objectKey = qiniuStorageService.createObjectKey("attachments", requestParam.getOriginalName());
        String token = qiniuStorageService.createImageUploadToken(objectKey);
        AttachmentDO attachment = new AttachmentDO()
                .setObjectKey(objectKey)
                .setOriginalName(requestParam.getOriginalName())
                .setType("image")
                .setMimeType(requestParam.getMimeType())
                .setSize(requestParam.getSize())
                .setStatus(STATUS_PENDING);
        save(attachment);

        return AttachmentUploadTokenRespDTO.builder()
                .id(attachment.getId())
                .key(objectKey)
                .token(token)
                .uploadUrl(qiniuStorageService.getUploadUrl())
                .build();
    }

    @Override
    public AttachmentRespDTO complete(Long id) {
        AttachmentDO attachment = getById(id);
        if (attachment == null) {
            throw new ClientException("附件不存在");
        }
        long actualSize = qiniuStorageService.statSize(attachment.getObjectKey());
        if (actualSize != attachment.getSize()) {
            throw new ClientException("上传对象大小与申请信息不一致");
        }
        if (!STATUS_READY.equals(attachment.getStatus())) {
            attachment.setStatus(STATUS_READY);
            updateById(attachment);
        }
        return toResponse(attachment);
    }

    @Override
    public PageData<AttachmentRespDTO> getPage(PageReqDTO requestParam) {
        Page<AttachmentDO> page = page(
                new Page<>(requestParam.getCurrent(), requestParam.getSize()),
                Wrappers.<AttachmentDO>lambdaQuery()
                        .eq(AttachmentDO::getStatus, STATUS_READY)
                        .eq(AttachmentDO::getType, "image")
                        .orderByDesc(AttachmentDO::getCreateTime));
        return PageData.of(page, this::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AttachmentDO attachment = getById(id);
        if (attachment == null) {
            throw new ClientException("附件不存在");
        }
        if (attachment.getRefId() != null) {
            throw new ClientException("附件已被引用，不能删除");
        }
        if (StringUtils.hasText(attachment.getObjectKey())) {
            qiniuStorageService.delete(attachment.getObjectKey());
        }
        removeById(id);
    }

    @Override
    public List<AttachmentRespDTO> listByReference(String refType, Long refId) {
        return list(Wrappers.<AttachmentDO>lambdaQuery()
                .eq(AttachmentDO::getRefType, refType)
                .eq(AttachmentDO::getRefId, refId)
                .eq(AttachmentDO::getStatus, STATUS_READY)
                .orderByAsc(AttachmentDO::getId)).stream().map(this::toResponse).toList();
    }

    @Override
    public void replaceProjectAttachments(Long projectId, List<Long> attachmentIds) {
        List<Long> ids = attachmentIds == null
                ? Collections.emptyList()
                : new LinkedHashSet<>(attachmentIds).stream().toList();
        if (!ids.isEmpty()) {
            List<AttachmentDO> attachments = listByIds(ids);
            if (attachments.size() != ids.size()) {
                throw new ClientException("使用了不存在的附件");
            }
            boolean unavailable = attachments.stream().anyMatch(attachment ->
                    !STATUS_READY.equals(attachment.getStatus())
                            || attachment.getRefId() != null
                            && !("project".equals(attachment.getRefType())
                            && projectId.equals(attachment.getRefId())));
            if (unavailable) {
                throw new ClientException("附件未就绪或已被其他对象引用");
            }
        }

        clearReference("project", projectId);
        if (!ids.isEmpty()) {
            update(Wrappers.<AttachmentDO>lambdaUpdate()
                    .in(AttachmentDO::getId, ids)
                    .set(AttachmentDO::getRefType, "project")
                    .set(AttachmentDO::getRefId, projectId));
        }
    }

    @Override
    public void clearReference(String refType, Long refId) {
        update(Wrappers.<AttachmentDO>lambdaUpdate()
                .eq(AttachmentDO::getRefType, refType)
                .eq(AttachmentDO::getRefId, refId)
                .set(AttachmentDO::getRefType, null)
                .set(AttachmentDO::getRefId, null));
    }

    @Override
    public AttachmentRespDTO toResponse(AttachmentDO attachment) {
        AttachmentRespDTO response = BeanUtil.toBean(attachment, AttachmentRespDTO.class);
        response.setUrl(StringUtils.hasText(attachment.getLegacyUrl())
                ? attachment.getLegacyUrl()
                : qiniuStorageService.buildPublicUrl(attachment.getObjectKey()));
        return response;
    }
}
