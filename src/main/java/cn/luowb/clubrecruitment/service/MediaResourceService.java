package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dao.entity.MediaResourceDO;
import cn.luowb.clubrecruitment.dto.req.MediaUploadReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MediaResourceRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【media_resource(媒体资源表)】的数据库操作Service
 * @createDate 2025-08-28 10:43:49
 */
public interface MediaResourceService extends IService<MediaResourceDO> {

    /**
     * 上传媒体资源
     *
     * @param file 媒体资源文件
     * @return 媒体资源响应DTO
     */
    MediaResourceRespDTO requestParam(MediaUploadReqDTO file);

    /**
     * 获取未引用的媒体资源
     *
     * @return 未引用的媒体资源响应DTO列表
     */
    List<MediaResourceRespDTO> getUnreferencedMedia();
}
