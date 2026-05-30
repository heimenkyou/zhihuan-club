package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.dao.entity.AwardDO;
import cn.luowb.clubrecruitment.dao.mapper.AwardMapper;
import cn.luowb.clubrecruitment.dto.req.AwardReqDTO;
import cn.luowb.clubrecruitment.service.AwardService;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author heimenkyou
 * @description 针对表【award(奖项信息表)】的数据库操作Service实现
 * @createDate 2025-08-25 22:03:34
 */
@Service
public class AwardServiceImpl extends ServiceImpl<AwardMapper, AwardDO>
        implements AwardService {

    @Override
    public void add(AwardReqDTO awardReqDTO) {
        if (StrUtil.isBlank(awardReqDTO.getCompetitionName())) {
            throw new IllegalArgumentException("竞赛名称不能为空");
        }
        if (StrUtil.isBlank(awardReqDTO.getCompetitionLevel())) {
            throw new IllegalArgumentException("竞赛级别不能为空");
        }
        if (StrUtil.isBlank(awardReqDTO.getAwardLevel())) {
            throw new IllegalArgumentException("奖项级别不能为空");
        }
        if (awardReqDTO.getWinners() == null || awardReqDTO.getWinners().isEmpty()) {
            throw new IllegalArgumentException("获奖人不能为空");
        }
        if (awardReqDTO.getAwardDate() == null) {
            throw new IllegalArgumentException("获奖时间不能为空");
        }
        AwardDO awardDO = BeanUtil.toBean(awardReqDTO, AwardDO.class);
        // 存储获奖年份
        awardDO.setYear(awardReqDTO.getAwardDate().getYear());
        // 存储获奖人
        awardDO.setWinners(JSONArray.toJSONString(awardReqDTO.getWinners()));
        this.save(awardDO);
    }

    @Override
    public void update(Long id, AwardReqDTO awardReqDTO) {
        AwardDO awardDO = this.getById(id);
        if (awardDO == null) {
            throw new ClientException("奖项不存在");
        }
        AwardDO newAwardDO = BeanUtil.toBean(awardReqDTO, AwardDO.class);
        // 存储获奖年份
        if (awardReqDTO.getAwardDate() != null) {
            newAwardDO.setYear(awardReqDTO.getAwardDate().getYear());
        }
        // 存储获奖人
        if (!ArrayUtil.isEmpty(awardReqDTO.getWinners())) {
            newAwardDO.setWinners(JSONArray.toJSONString(awardReqDTO.getWinners()));
        }
        newAwardDO.setId(id);
        this.updateById(newAwardDO);
    }
}




