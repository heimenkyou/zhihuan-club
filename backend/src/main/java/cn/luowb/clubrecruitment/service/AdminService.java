package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import cn.luowb.clubrecruitment.dto.req.AdminLoginReqDTO;
import cn.luowb.clubrecruitment.dto.req.AdminReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminLoginRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminPageRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/** 管理员服务。 */
public interface AdminService extends IService<AdminDO> {

    /**
     * 管理员登录
     *
     * @param requestParam 登录请求参数
     * @return 登录响应参数
     */
    AdminLoginRespDTO login(AdminLoginReqDTO requestParam);

    /**
     * 管理员添加
     *
     * @param requestParam 添加请求参数
     */
    void add(AdminReqDTO requestParam);

    /**
     * 分页查询管理员
     *
     * @param requestParam 分页查询请求参数
     * @return 分页查询响应参数
     */
    PageData<AdminPageRespDTO> getAdminPage(PageReqDTO requestParam);

    /**
     * 删除管理员
     *
     * @param id 管理员唯一标识
     */
    void delete(Long id);

    /**
     * 更新管理员
     *
     * @param requestParam 更新请求参数
     * @param id           管理员唯一标识
     */
    void update(AdminReqDTO requestParam, Long id);

    /**
     * 管理员登出
     */
    void logout();

    /**
     * 获取当前管理员信息
     *
     * @return 管理员信息
     */
    AdminPageRespDTO getAdminInfo();

    /**
     * 查询指定管理员信息
     *
     * @param id 管理员唯一标识
     * @return 管理员信息
     */
    AdminPageRespDTO getAdminInfo(Long id);

    /**
     * 更新当前管理员资料。
     *
     * @param requestParam 更新请求参数
     */
    void updateCurrent(AdminReqDTO requestParam);
}
