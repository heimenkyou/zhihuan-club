package cn.luowb.clubrecruitment.service.impl;

import cn.luowb.clubrecruitment.common.constant.RedisCacheKeyEnum;
import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import cn.luowb.clubrecruitment.dao.mapper.ApplicationMapper;
import cn.luowb.clubrecruitment.dao.mapper.MajorMappingMapper;
import cn.luowb.clubrecruitment.service.MajorMappingService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MajorMappingServiceImpl extends ServiceImpl<MajorMappingMapper, MajorMappingDO>
        implements MajorMappingService {
    private final MajorMappingMapper majorMappingMapper;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationMapper applicationMapper;

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
    public String getAbbreviation(String studentId) {
        String code = studentId.substring(4, 8);
        MajorMappingDO majorMappingDO = getOne(Wrappers.<MajorMappingDO>lambdaQuery()
                .eq(MajorMappingDO::getCode, code));
        if (majorMappingDO == null) {
            log.info("未找到专业代号对应的专业简称：{}", code);
            return null;
        }
        return majorMappingDO.getShortName();
    }

    @Override
    public String buildClassName(String studentId) {
        ApplicationDO application = applicationMapper.selectOne(
                Wrappers.<ApplicationDO>lambdaQuery().eq(ApplicationDO::getStudentId, studentId));
        if (application == null) {
            throw new RuntimeException("未找到学号对应的报名信息：" + studentId);
        }
        return buildClassName(studentId, application.getName());
    }

    @Override
    public String buildClassName(String studentId, String name) {
        String majorAbbreviation = getAbbreviation(studentId);
        if (majorAbbreviation == null) {
            ApplicationDO application = applicationMapper.selectOne(
                    Wrappers.<ApplicationDO>lambdaQuery().eq(ApplicationDO::getStudentId, studentId));
            majorAbbreviation = application != null ? application.getMajor() : "未知专业";
        }
        return String.format("%sB%s%s",
                majorAbbreviation, // 专业简称
                studentId.substring(2, 4) + studentId.charAt(8), // 班级
                name);
    }
}




