package cn.luowb.clubrecruitment.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import cn.luowb.clubrecruitment.dao.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 为 Sa-Token 提供管理员角色。
 */
@Component
@RequiredArgsConstructor
public class AdminRoleProvider implements StpInterface {
    private final AdminMapper adminMapper;

    /**
     * 当前项目不使用权限码。
     *
     * @param loginId 登录管理员 ID
     * @param loginType 登录类型
     * @return 空权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

    /**
     * 查询当前管理员角色。
     *
     * @param loginId 登录管理员 ID
     * @param loginType 登录类型
     * @return 管理员角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        AdminDO admin = adminMapper.selectById(Long.valueOf(loginId.toString()));
        return admin == null ? Collections.emptyList() : List.of(admin.getRole());
    }
}
