package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.properties.AttachmentProperties;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * 附件业务服务实现。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, AttachmentDO>
        implements AttachmentService {
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_READY = "ready";

    private final QiniuStorageService qiniuStorageService;
    private final AttachmentProperties attachmentProperties;

    @Override
    public AttachmentUploadTokenRespDTO createUploadToken(AttachmentUploadTokenReqDTO requestParam) {
        if (!requestParam.getMimeType().toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new ClientException("只允许上传图片");
        }
        if (requestParam.getSize() > attachmentProperties.getMaxImageSize().toBytes()) {
            throw new ClientException("图片大小超过允许上限");
        }

        String objectKey = qiniuStorageService.createObjectKey("attachments", requestParam.getOriginalName());
        String token = qiniuStorageService.createImageUploadToken(objectKey);
        AttachmentDO attachment = new AttachmentDO()
                .setObjectKey(objectKey)
                .setOriginalName(requestParam.getOriginalName())
                .setMimeType(requestParam.getMimeType())
                .setSize(requestParam.getSize())
                .setStatus(STATUS_PENDING);
        save(attachment);

        return AttachmentUploadTokenRespDTO.builder()
                .id(attachment.getId())
                .key(objectKey)
                .token(token)
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
                        .orderByDesc(AttachmentDO::getCreateTime));
        return PageData.of(page, this::toResponse);
    }

    @Override
    public void delete(Long id) {
        AttachmentDO attachment = getById(id);
        if (attachment == null) {
            throw new ClientException("附件不存在");
        }
        if (!StringUtils.hasText(attachment.getLegacyUrl()) && StringUtils.hasText(attachment.getObjectKey())) {
            qiniuStorageService.delete(attachment.getObjectKey());
            qiniuStorageService.refreshPublicUrl(attachment.getObjectKey());
        }
        removeById(id);
    }

    @Override
    public AttachmentRespDTO rename(Long id, String originalName) {
        AttachmentDO attachment = getById(id);
        if (attachment == null) {
            throw new ClientException("附件不存在");
        }
        attachment.setOriginalName(originalName);
        updateById(attachment);
        return toResponse(attachment);
    }

    @Override
    public AttachmentRespDTO toResponse(AttachmentDO attachment) {
        AttachmentRespDTO response = BeanUtil.toBean(attachment, AttachmentRespDTO.class);
        response.setUrl(StringUtils.hasText(attachment.getLegacyUrl())
                ? attachment.getLegacyUrl()
                : qiniuStorageService.buildPublicUrl(attachment.getObjectKey()));
        return response;
    }

    /**
     * 清理未完成上传的过期图片记录，避免直传中断留下无主对象。
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void cleanExpiredPendingAttachments() {
        List<AttachmentDO> attachments = list(Wrappers.<AttachmentDO>lambdaQuery()
                .eq(AttachmentDO::getStatus, STATUS_PENDING)
                .lt(AttachmentDO::getCreateTime, LocalDateTime.now().minusMinutes(10)));
        for (AttachmentDO attachment : attachments) {
            try {
                if (StringUtils.hasText(attachment.getObjectKey())) {
                    qiniuStorageService.delete(attachment.getObjectKey());
                }
                removeById(attachment.getId());
            } catch (Exception e) {
                log.warn("清理过期待上传图片失败, attachmentId={}", attachment.getId(), e);
            }
        }
    }
}
