package cn.luowb.clubrecruitment.service.impl;

import cn.luowb.clubrecruitment.common.util.RedisKeyUtil;
import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import cn.luowb.clubrecruitment.dao.mapper.MajorMappingMapper;
import cn.luowb.clubrecruitment.service.MajorMappingService;
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

/**
 * @author heimenkyou
 * @description 针对表【major_mapping(专业代号与名称映射表)】的数据库操作Service实现
 * @createDate 2025-09-14 23:34:31
 */
@Service
@RequiredArgsConstructor
public class MajorMappingServiceImpl extends ServiceImpl<MajorMappingMapper, MajorMappingDO>
        implements MajorMappingService {
    private final StringRedisTemplate redisTemplate;
    private final RedisKeyUtil redisKeyUtil;

    @Override
    public Map<String, MajorMappingDO> majors() {
        String key = redisKeyUtil.buildMajorMappingKey();
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
}




