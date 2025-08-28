package cn.luowb.clubrecruitment.common.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {
    /**
     * 上传文件到minio
     *
     * @param file     上传的文件
     * @param fileName 文件名
     * @param folder   文件夹
     * @return 文件的url
     */
    public String upload(MultipartFile file, String fileName, String folder) {
        return null;
    }
}
