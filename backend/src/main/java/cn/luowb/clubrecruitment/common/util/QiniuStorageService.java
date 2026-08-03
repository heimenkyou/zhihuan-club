package cn.luowb.clubrecruitment.common.util;

import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.common.properties.QiniuProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

/**
 * 七牛云对象存储操作服务。
 */
@Service
@RequiredArgsConstructor
public class QiniuStorageService {
    private static final long UPLOAD_TOKEN_EXPIRES_SECONDS = 600;

    private final QiniuProperties properties;

    /**
     * 生成仅允许写入指定对象的上传凭证。
     *
     * @param objectKey 对象键
     * @return 上传凭证
     */
    public String createUploadToken(String objectKey) {
        return createUploadToken(objectKey, null);
    }

    /**
     * 生成仅允许上传图片到指定对象的上传凭证。
     *
     * @param objectKey 对象键
     * @return 上传凭证
     */
    public String createImageUploadToken(String objectKey) {
        return createUploadToken(objectKey, "image/*");
    }

    private String createUploadToken(String objectKey, String mimeLimit) {
        validateConfiguration();
        StringMap policy = new StringMap().put("insertOnly", 1);
        if (StringUtils.hasText(mimeLimit)) {
            // MIME 限制写入七牛上传策略，避免客户端绕过申请参数上传非图片内容。
            policy.put("mimeLimit", mimeLimit);
        }
        return auth().uploadToken(properties.getBucket(), objectKey, UPLOAD_TOKEN_EXPIRES_SECONDS, policy);
    }

    /**
     * 生成不可预测且保留安全扩展名的对象键。
     *
     * @param folder       对象目录
     * @param originalName 原文件名
     * @return 对象键
     */
    public String createObjectKey(String folder, String originalName) {
        String extension = getSafeExtension(originalName);
        LocalDate today = LocalDate.now();
        return "%s/%d/%02d/%s%s".formatted(
                folder, today.getYear(), today.getMonthValue(), UUID.randomUUID(), extension);
    }

    /**
     * 将文件上传到指定对象键。
     *
     * @param file      上传文件
     * @param objectKey 对象键
     */
    public void upload(MultipartFile file, String objectKey) {
        validateConfiguration();
        UploadManager uploadManager = new UploadManager(Configuration.create(Region.autoRegion()));
        String mimeType = StringUtils.hasText(file.getContentType())
                ? file.getContentType() : "application/octet-stream";
        try (InputStream inputStream = file.getInputStream()) {
            Response response = uploadManager.put(
                    inputStream, objectKey, createUploadToken(objectKey), null, mimeType);
            boolean uploaded = response.isOK();
            response.close();
            if (!uploaded) {
                throw new ServiceException("文件上传到七牛云失败");
            }
        } catch (IOException e) {
            throw new ServiceException("文件上传到七牛云失败");
        }
    }

    /**
     * 查询对象大小，用于确认客户端直传结果。
     *
     * @param objectKey 对象键
     * @return 对象字节数
     */
    public long statSize(String objectKey) {
        validateConfiguration();
        try {
            FileInfo fileInfo = bucketManager().stat(properties.getBucket(), objectKey);
            return fileInfo.fsize;
        } catch (QiniuException e) {
            throw new ServiceException("无法确认七牛云对象状态");
        }
    }

    /**
     * 按对象键删除七牛云对象。
     *
     * @param objectKey 对象键
     */
    public void delete(String objectKey) {
        validateConfiguration();
        try {
            bucketManager().delete(properties.getBucket(), objectKey);
        } catch (QiniuException e) {
            throw new ServiceException("七牛云对象删除失败");
        }
    }

    /**
     * 根据公开域名构造对象访问地址。
     *
     * @param objectKey 对象键
     * @return 公开访问地址
     */
    public String buildPublicUrl(String objectKey) {
        if (!StringUtils.hasText(objectKey)) {
            return null;
        }
        validateConfiguration();
        return properties.getDomain().replaceAll("/+$", "") + "/" + objectKey;
    }

    /**
     * 获取浏览器直传使用的上传域名。
     *
     * @return 上传域名
     */
    public String getUploadUrl() {
        validateConfiguration();
        return properties.getDomain().replaceAll("/+$", "");
    }

    private Auth auth() {
        return Auth.create(properties.getAccessKey(), properties.getSecretKey());
    }

    private BucketManager bucketManager() {
        return new BucketManager(auth(), Configuration.create(Region.autoRegion()));
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(properties.getAccessKey())
                || !StringUtils.hasText(properties.getSecretKey())
                || !StringUtils.hasText(properties.getBucket())
                || !StringUtils.hasText(properties.getDomain())) {
            throw new ServiceException("七牛云配置不完整");
        }
    }

    private String getSafeExtension(String originalName) {
        if (!StringUtils.hasText(originalName)) {
            return "";
        }
        int separator = originalName.lastIndexOf('.');
        if (separator < 0 || separator == originalName.length() - 1) {
            return "";
        }
        String extension = originalName.substring(separator + 1).toLowerCase(Locale.ROOT);
        return extension.matches("[a-z0-9]{1,10}") ? "." + extension : "";
    }
}
