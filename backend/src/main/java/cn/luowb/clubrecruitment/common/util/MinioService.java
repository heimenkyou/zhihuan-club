package cn.luowb.clubrecruitment.common.util;

import cn.luowb.clubrecruitment.common.properties.MinioProperties;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
public class MinioService {
    private final MinioClient minioClient;
    private final String bucketName;
    private final MinioProperties minioProperties;

    @Autowired
    public MinioService(MinioProperties minioProperties, MinioClient minioClient) {
        this.bucketName = minioProperties.getBucketName();
        this.minioClient = minioClient;
        // 确保桶存在并设置公共读策略
        initializeBucket();
        this.minioProperties = minioProperties;
    }

    /**
     * 初始化桶，确保存在并设置公共读策略
     */
    private void initializeBucket() {
        try {
            // 尝试检查桶是否存在
            boolean isExist;
            try {
                isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build());
            } catch (Exception e) {
                log.warn("检查桶存在时遇到权限问题，尝试继续初始化: {}", e.getMessage());
                isExist = true; // 假设桶已存在
            }
            if (!isExist) {
                // 创建桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                log.info("创建Minio桶: {}", bucketName);
            }


            String policyJson = """
                    {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Effect": "Allow",
                                "Principal": "*",
                                "Action": ["s3:GetObject"],
                                "Resource": ["arn:aws:s3:::%s/*"]
                            }
                        ]
                    }
                    """.formatted(bucketName);

            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policyJson)
                    .build());

            log.info("已设置Minio桶 {} 为公共读", bucketName);
        } catch (Exception e) {
            log.error("初始化Minio桶失败", e);
            throw new RuntimeException("Minio初始化失败", e);
        }
    }

    /**
     * 上传文件到minio
     *
     * @param file     上传的文件
     * @param fileName 文件名
     * @param folder   文件夹
     * @return 文件的url
     */
    public String upload(MultipartFile file, String fileName, String folder) {
        try {
            // 构建对象名称（包含文件夹路径）
            String objectName = StringUtils.hasText(folder) ?
                    folder + "/" + fileName : fileName;

            // 获取文件输入流和内容类型
            InputStream inputStream = file.getInputStream();
            String contentType = file.getContentType();

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(contentType)
                    .build());

            // 构建文件访问URL
            return String.format("%s/%s/%s",
                    minioProperties.getPublicEndpoint(),
                    bucketName,
                    objectName);

        } catch (Exception e) {
            log.error("文件上传到Minio失败: {}", fileName, e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 删除minio文件
     *
     * @param fileName 文件名
     * @param folder   文件夹
     */
    public void delete(String fileName, String folder) {
        try {
            // 构建对象名称（包含文件夹路径）
            String objectName = StringUtils.hasText(folder) ?
                    folder + "/" + fileName : fileName;

            // 删除文件
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            log.info("已从Minio删除文件: {}", objectName);
        } catch (Exception e) {
            log.error("从Minio删除文件失败: {}", fileName, e);
            throw new RuntimeException("文件删除失败", e);
        }
    }

    /**
     * 删除minio文件
     *
     * @param fileUrl 文件url
     */
    public void delete(String fileUrl) {
        try {
            String endpoint = minioProperties.getEndpoint();
            // 检查 URL 是否以 MinIO endpoint 开头
            if (!fileUrl.startsWith(endpoint) && !fileUrl.startsWith(minioProperties.getPublicEndpoint())) {
                log.warn("URL 不是当前系统的 MinIO 资源, 不删除: {}", fileUrl);
                return;
            }
            // 从URL中提取对象名称
            String path = fileUrl.substring(endpoint.length());
            // 去掉第一个 /，然后按 / 分割，取第二段之后作为 objectName
            String[] parts = path.substring(1).split("/", 2); // 分成最多两部分
            if (parts.length < 2) {
                log.info("URL 不合法, 不删除: {}", fileUrl);
                return;
            }
            String objectName = parts[1]; // 即: media/20250829012018_食堂.jpg
            // 删除文件
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            log.info("已从Minio删除文件: {}", objectName);
        } catch (Exception e) {
            log.error("从Minio删除文件失败: {}", fileUrl, e);
            throw new RuntimeException("文件删除失败", e);
        }
    }
}
