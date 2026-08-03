package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import cn.luowb.clubrecruitment.dao.mapper.AdminMapper;
import cn.luowb.clubrecruitment.dto.req.AdminLoginReqDTO;
import cn.luowb.clubrecruitment.dto.req.AdminReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminLoginRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminPageRespDTO;
import cn.luowb.clubrecruitment.service.AdminService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author heimenkyou
 * @description 针对表【admin(管理员账户信息表，存储系统管理员身份凭证)】的数据库操作Service实现
 * @createDate 2025-08-26 16:56:13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminDO>
        implements AdminService {
    private final AdminMapper adminMapper;

    @Override
    public AdminLoginRespDTO login(AdminLoginReqDTO requestParam) {
        String username = requestParam.getUsername();
        String password = requestParam.getPassword();
        AdminDO adminDO = adminMapper.selectByUsername(username);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        if (!BCrypt.checkpw(password, adminDO.getPasswordHash())) {
            throw new ClientException("密码错误");
        }
        StpUtil.login(adminDO.getId());
        AdminLoginRespDTO loginRespDTO = BeanUtil.toBean(adminDO, AdminLoginRespDTO.class);
        loginRespDTO.setToken(StpUtil.getTokenValue());
        return loginRespDTO;
    }

    @Override
    public void add(AdminReqDTO requestParam) {
        AdminDO adminDO = adminMapper.selectByUsername(requestParam.getUsername());
        if (adminDO != null) {
            throw new ClientException("用户名已存在");
        }
        adminDO = BeanUtil.toBean(requestParam, AdminDO.class);
        adminDO.setPasswordHash(BCrypt.hashpw(requestParam.getPassword()));
        adminMapper.insert(adminDO);
    }

    @Override
    public PageData<AdminPageRespDTO> getAdminPage(PageReqDTO requestParam) {
        Page<AdminDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        page = this.page(page);
        return PageData.of(page, each -> BeanUtil.toBean(each, AdminPageRespDTO.class));
    }

    @Override
    public void delete(Long id) {
        AdminDO adminDO = adminMapper.selectById(id);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        adminMapper.deleteById(id);
    }

    @Override
    public void update(AdminReqDTO requestParam, Long id) {
        AdminDO adminDO = adminMapper.selectById(id);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        adminDO = BeanUtil.toBean(requestParam, AdminDO.class);
        adminDO.setId(id);
        if (!StrUtil.isBlank(requestParam.getPassword())) {
            adminDO.setPasswordHash(BCrypt.hashpw(requestParam.getPassword()));
        }
        adminMapper.updateById(adminDO);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public AdminPageRespDTO getAdminInfo() {
        return this.getAdminInfo(StpUtil.getLoginIdAsLong());
    }

    @Override
    public AdminPageRespDTO getAdminInfo(Long id) {
        AdminDO adminDO = adminMapper.selectById(id);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        return BeanUtil.toBean(adminDO, AdminPageRespDTO.class);
    }

    @Override
    public void updateCurrent(AdminReqDTO requestParam) {
        Long id = StpUtil.getLoginIdAsLong();
        AdminDO adminDO = adminMapper.selectById(id);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        if (StrUtil.isNotBlank(requestParam.getUsername())) {
            AdminDO sameUsernameAdmin = adminMapper.selectByUsername(requestParam.getUsername());
            if (sameUsernameAdmin != null && !sameUsernameAdmin.getId().equals(id)) {
                throw new ClientException("用户名已存在");
            }
            adminDO.setUsername(requestParam.getUsername());
        }
        if (StrUtil.isNotBlank(requestParam.getPassword())) {
            adminDO.setPasswordHash(BCrypt.hashpw(requestParam.getPassword()));
        }
        adminMapper.updateById(adminDO);
    }

}




