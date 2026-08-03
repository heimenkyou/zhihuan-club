package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.AttachmentDO;
import cn.luowb.clubrecruitment.dto.req.AttachmentUploadTokenReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentUploadTokenRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 附件业务服务。
 */
public interface AttachmentService extends IService<AttachmentDO> {
    /**
     * 创建图片直传记录与上传凭证。
     *
     * @param requestParam 文件元数据
     * @return 上传凭证
     */
    AttachmentUploadTokenRespDTO createUploadToken(AttachmentUploadTokenReqDTO requestParam);

    /**
     * 确认直传对象并将附件标记为可用。
     *
     * @param id 附件ID
     * @return 附件信息
     */
    AttachmentRespDTO complete(Long id);

    /**
     * 分页查询可用附件。
     *
     * @param requestParam 分页参数
     * @return 附件分页
     */
    PageData<AttachmentRespDTO> getPage(PageReqDTO requestParam);

    /**
     * 删除图片及其存储对象。
     *
     * @param id 附件ID
     */
    void delete(Long id);

    /**
     * 将附件记录转换为包含访问地址的响应。
     *
     * @param attachment 附件记录
     * @return 附件响应
     */
    AttachmentRespDTO toResponse(AttachmentDO attachment);
}
