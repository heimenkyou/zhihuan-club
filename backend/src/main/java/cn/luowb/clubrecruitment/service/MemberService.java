package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.MemberDO;
import cn.luowb.clubrecruitment.dto.req.MemberPageReqDTO;
import cn.luowb.clubrecruitment.dto.req.MemberReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MemberPageRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/** 成员服务。 */
public interface MemberService extends IService<MemberDO> {

    /**
     * 分页查询成员信息
     *
     * @param requestParam 分页参数
     * @return 分页数据
     */
    PageData<MemberPageRespDTO> getMemberPage(MemberPageReqDTO requestParam);

    /**
     * 查询成员详情
     *
     * @param id 成员ID
     * @return 成员详情
     */
    MemberPageRespDTO getMemberDetail(Long id);

    /**
     * 新增成员
     *
     * @param requestParam 成员参数
     */
    void createMember(MemberReqDTO requestParam);

    /**
     * 编辑成员
     *
     * @param id           成员ID
     * @param requestParam 成员参数
     */
    void updateMember(Long id, MemberReqDTO requestParam);

    /**
     * 切换成员状态
     *
     * @param id     成员ID
     * @param status 目标状态（active/inactive）
     */
    void updateMemberStatus(Long id, String status);
}
