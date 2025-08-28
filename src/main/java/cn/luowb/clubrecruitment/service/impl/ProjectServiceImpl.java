package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.ProjectDO;
import cn.luowb.clubrecruitment.dao.entity.ProjectDetailDO;
import cn.luowb.clubrecruitment.dao.mapper.MediaResourceMapper;
import cn.luowb.clubrecruitment.dao.mapper.ProjectDetailMapper;
import cn.luowb.clubrecruitment.dao.mapper.ProjectMapper;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AwardRespDTO;
import cn.luowb.clubrecruitment.dto.resp.MediaResourceRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectDetailRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectRespDTO;
import cn.luowb.clubrecruitment.service.AwardService;
import cn.luowb.clubrecruitment.service.ProjectService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【project(项目基本信息表)】的数据库操作Service实现
 * @createDate 2025-08-27 17:39:11
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectDO>
        implements ProjectService {
    private final ProjectDetailMapper projectDetailMapper;
    private final MediaResourceMapper mediaResourceMapper;
    private final AwardService awardService;

    @Override
    public PageData<ProjectRespDTO> getPage(PageReqDTO requestParam) {
        Page<ProjectDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<ProjectDO>().orderByDesc(ProjectDO::getCreateTime));
        return PageData.of(page, each -> {
            @SuppressWarnings("unchecked") // 忽略 "vararg 形参的未检查的泛型数组创建" 警告
            ProjectRespDTO respDTO = BeanUtil.toBean(each, ProjectRespDTO.class,
                    CopyOptions.create()
                            // 忽略 teamMembers 和 techStackTags 字段
                            .setIgnoreProperties(ProjectRespDTO::getTeamMembers, ProjectRespDTO::getTechStackTags));
            // 反序列化 技术栈标签
            List<String> techStackTags = JSON.parseArray(each.getTechStackTags(), String.class);
            respDTO.setTechStackTags(techStackTags);
            // 反序列化 开发团队成员
            List<ProjectRespDTO.TeamMemberDTO> teamMemberDTOS = JSON.parseArray(each.getTeamMembers(),
                    ProjectRespDTO.TeamMemberDTO.class);
            respDTO.setTeamMembers(teamMemberDTOS);
            return respDTO;
        });
    }

    @Override
    public ProjectDetailRespDTO getProjectDetail(Long projectId) {
        // 查询项目详情信息
        ProjectDetailDO detailDO = projectDetailMapper.selectByProjectId(projectId);
        // 批量查询外部资源
        List<AwardRespDTO> awardList = Collections.emptyList();

        List<MediaResourceRespDTO> mediaResources = mediaResourceMapper.selectListByRefId(projectId).stream()
                .map(each -> BeanUtil.toBean(each, MediaResourceRespDTO.class))
                .toList();

        List<Long> awardIds = JSON.parseArray(detailDO.getAwardIds(), Long.class);
        if (CollectionUtil.isNotEmpty(awardIds)) {
            awardList = awardService.listByIds(awardIds).stream()
                    .map(each -> {
                        AwardRespDTO awardRespDTO = BeanUtil.toBean(each, AwardRespDTO.class);
                        // 反序列化 获奖人
                        List<String> winners = JSON.parseArray(each.getWinners(), String.class);
                        awardRespDTO.setWinners(winners);
                        return awardRespDTO;
                    })
                    .toList();
        }

        // 组装成最终 DTO
        // 基础字段拷贝
        @SuppressWarnings("unchecked") // 忽略 "vararg 形参的未检查的泛型数组创建" 警告
        ProjectDetailRespDTO respDTO = BeanUtil.toBean(detailDO, ProjectDetailRespDTO.class,
                CopyOptions.create()
                        // 忽略 awardIds, techStackDetail, teamDivision 字段
                        .setIgnoreProperties(ProjectDetailDO::getAwardIds,
                                ProjectDetailDO::getTechStackDetail, ProjectDetailDO::getTeamDivision));

        // 反序列化 技术栈标签
        List<String> techStackDetail = JSON.parseArray(detailDO.getTechStackDetail(), String.class);
        respDTO.setTechStackDetail(techStackDetail);
        respDTO.setDescriptionMd(detailDO.getDescriptionMd());
        // 反序列化 团队成员分工
        List<ProjectDetailRespDTO.TeamDivision> teamDivisions = JSON.parseArray(detailDO.getTeamDivision(),
                ProjectDetailRespDTO.TeamDivision.class);
        respDTO.setTeamDivision(teamDivisions);

        // 设置关联对象
        respDTO.setMediaResources(mediaResources);
        respDTO.setAwardList(awardList);
        return respDTO;
    }
}




