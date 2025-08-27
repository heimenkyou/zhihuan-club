package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.ProjectDO;
import cn.luowb.clubrecruitment.dao.mapper.ProjectMapper;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectRespDTO;
import cn.luowb.clubrecruitment.service.ProjectService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【project(项目基本信息表)】的数据库操作Service实现
 * @createDate 2025-08-27 17:39:11
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectDO>
        implements ProjectService {

    @Override
    public PageData<ProjectRespDTO> getPage(PageReqDTO requestParam) {
        Page<ProjectDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<ProjectDO>().orderByDesc(ProjectDO::getCreateTime));
        return PageData.of(page, each -> {
            @SuppressWarnings("unchecked") // 忽略 "vararg 形参的未检查的泛型数组创建" 警告
            ProjectRespDTO respDTO = BeanUtil.toBean(each, ProjectRespDTO.class,
                    CopyOptions.create()
                            .setIgnoreProperties(ProjectRespDTO::getTeamMembers, ProjectRespDTO::getTechStackTags));
            List<String> techStackTags = JSON.parseArray(each.getTechStackTags(), String.class);
            respDTO.setTechStackTags(techStackTags);
            List<ProjectRespDTO.TeamMemberDTO> teamMemberDTOS = JSON.parseArray(each.getTeamMembers(), ProjectRespDTO.TeamMemberDTO.class);
            respDTO.setTeamMembers(teamMemberDTOS);
            return respDTO;
        });
    }
}




