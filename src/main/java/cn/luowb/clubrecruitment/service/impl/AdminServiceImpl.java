package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.luowb.clubrecruitment.common.context.UserContext;
import cn.luowb.clubrecruitment.common.context.UserInfoDTO;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.util.RedisKeyUtil;
import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import cn.luowb.clubrecruitment.dao.mapper.AdminMapper;
import cn.luowb.clubrecruitment.dto.req.AdminLoginReqDTO;
import cn.luowb.clubrecruitment.dto.req.AdminReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminLoginRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminPageRespDTO;
import cn.luowb.clubrecruitment.service.AdminService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

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
    private final RedisKeyUtil redisKeyUtil;
    private final StringRedisTemplate stringRedisTemplate;

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
        String token = IdUtil.simpleUUID();
        // 存储token到redis
        String key = redisKeyUtil.buildAdminTokenKey(token);
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userId(adminDO.getId())
                .username(adminDO.getUsername())
                .role(adminDO.getRole())
                .build();
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(userInfoDTO),
                Duration.ofMinutes(redisKeyUtil.adminTokenExpireMinutes));
        // 返回数据
        AdminLoginRespDTO loginRespDTO = BeanUtil.toBean(adminDO, AdminLoginRespDTO.class);
        loginRespDTO.setToken(token);
        return loginRespDTO;
    }

    @Override
    public void add(AdminReqDTO requestParam) {
        String role = UserContext.getRole();
        if (!"super".equals(role)) {
            throw new ClientException("权限不足");
        }
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
        String role = UserContext.getRole();
        if (!"super".equals(role)) {
            throw new ClientException("权限不足");
        }
        AdminDO adminDO = adminMapper.selectById(id);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        adminMapper.deleteById(id);
    }

    @Override
    public void update(AdminReqDTO requestParam, Long id) {
        String role = UserContext.getRole();
        if (!"super".equals(role)) {
            throw new ClientException("权限不足");
        }
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
    public void logout(String token) {
        String key = redisKeyUtil.buildAdminTokenKey(token);
        stringRedisTemplate.delete(key);
    }

    @Override
    public AdminPageRespDTO getAdminInfo() {
        Long userId = UserContext.getUserId();
        return this.getAdminInfo(userId);
    }

    @Override
    public AdminPageRespDTO getAdminInfo(Long id) {
        AdminDO adminDO = adminMapper.selectById(id);
        if (adminDO == null) {
            throw new ClientException("用户不存在");
        }
        return BeanUtil.toBean(adminDO, AdminPageRespDTO.class);
    }

}




