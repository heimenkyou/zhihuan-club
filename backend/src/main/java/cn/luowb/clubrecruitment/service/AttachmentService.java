package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.AttachmentDO;
import cn.luowb.clubrecruitment.dto.req.AttachmentUploadTokenReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentUploadTokenRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     * 删除未被引用的附件。
     *
     * @param id 附件ID
     */
    void delete(Long id);

    /**
     * 查询指定业务对象的可用附件。
     *
     * @param refType 引用类型
     * @param refId   引用ID
     * @return 附件列表
     */
    List<AttachmentRespDTO> listByReference(String refType, Long refId);

    /**
     * 替换项目的附件关联，空列表表示清空。
     *
     * @param projectId     项目ID
     * @param attachmentIds 附件ID列表
     */
    void replaceProjectAttachments(Long projectId, List<Long> attachmentIds);

    /**
     * 清除指定业务对象的附件关联。
     *
     * @param refType 引用类型
     * @param refId   引用ID
     */
    void clearReference(String refType, Long refId);

    /**
     * 将附件记录转换为包含访问地址的响应。
     *
     * @param attachment 附件记录
     * @return 附件响应
     */
    AttachmentRespDTO toResponse(AttachmentDO attachment);
}
