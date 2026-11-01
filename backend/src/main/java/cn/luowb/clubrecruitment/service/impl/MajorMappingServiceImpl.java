package cn.luowb.clubrecruitment.service.impl;

import cn.luowb.clubrecruitment.common.constant.RedisCacheKeyEnum;
import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import cn.luowb.clubrecruitment.dao.mapper.MajorMappingMapper;
import cn.luowb.clubrecruitment.service.MajorMappingService;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 专业映射服务实现。 */
@Service
@RequiredArgsConstructor
public class MajorMappingServiceImpl extends ServiceImpl<MajorMappingMapper, MajorMappingDO>
        implements MajorMappingService {
    private final MajorMappingMapper majorMappingMapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public Map<String, MajorMappingDO> majors() {
        String key = RedisCacheKeyEnum.MAJOR_MAPPING_KEY.getKey();
        // 从redis中获取
        String map = redisTemplate.opsForValue().get(key);
        if (map != null) {
            return JSON.parseObject(map, new TypeReference<>() {
            });
        }
        List<MajorMappingDO> majorMappingDOList = list();
        Map<String, MajorMappingDO> majorMap = majorMappingDOList.stream()
                .collect(Collectors.toMap(MajorMappingDO::getCode, Function.identity()));
        // 存到redis
        redisTemplate.opsForValue().set(key, JSON.toJSONString(majorMap));
        // 设置过期时间
        redisTemplate.expire(key, 30L, TimeUnit.MINUTES);
        return majorMap;
    }

    @Override
    public void createMapping(MajorMappingDO mapping) {
        validate(mapping);
        if (getById(mapping.getCode()) != null
                || lambdaQuery().eq(MajorMappingDO::getFullName, mapping.getFullName()).exists()) {
            throw new ServiceException("专业编号或专业全称已存在");
        }
        if (!save(mapping)) {
            throw new ServiceException("新增专业映射失败");
        }
        clearCache();
    }

    @Override
    public void updateMapping(String code, MajorMappingDO mapping) {
        validate(mapping);
        if (!code.equals(mapping.getCode())) {
            throw new ServiceException("专业编号不可修改");
        }
        if (getById(code) == null) {
            throw new ServiceException("专业映射不存在");
        }
        if (lambdaQuery().eq(MajorMappingDO::getFullName, mapping.getFullName())
                .ne(MajorMappingDO::getCode, code).exists()) {
            throw new ServiceException("专业全称已存在");
        }
        if (!updateById(mapping)) {
            throw new ServiceException("更新专业映射失败");
        }
        clearCache();
    }

    private void validate(MajorMappingDO mapping) {
        if (mapping == null || mapping.getCode() == null || !mapping.getCode().matches("\\d{4}")
                || mapping.getShortName() == null || mapping.getShortName().isBlank()
                || mapping.getFullName() == null || mapping.getFullName().isBlank()) {
            throw new ServiceException("专业编号必须是4位数字，专业简称和全称不能为空");
        }
    }

    private void clearCache() {
        redisTemplate.delete(RedisCacheKeyEnum.MAJOR_MAPPING_KEY.getKey());
    }
}




