package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.AwardDO;
import cn.luowb.clubrecruitment.dao.entity.ProjectDO;
import cn.luowb.clubrecruitment.dao.entity.ProjectAwardDO;
import cn.luowb.clubrecruitment.dao.entity.ProjectDetailDO;
import cn.luowb.clubrecruitment.dao.mapper.ProjectDetailMapper;
import cn.luowb.clubrecruitment.dao.mapper.ProjectMapper;
import cn.luowb.clubrecruitment.dao.mapper.ProjectAwardMapper;
import cn.luowb.clubrecruitment.dto.TeamDivisionDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.req.ProjectSaveReqDTO;
import cn.luowb.clubrecruitment.dto.resp.*;
import cn.luowb.clubrecruitment.service.AwardService;
import cn.luowb.clubrecruitment.service.ProjectService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** 项目服务实现。 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectDO>
        implements ProjectService {
    private final ProjectDetailMapper projectDetailMapper;
    private final ProjectAwardMapper projectAwardMapper;
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
            List<String> teamMemberDTOS = JSON.parseArray(each.getTeamMembers(),
                    String.class);
            respDTO.setTeamMembers(teamMemberDTOS);
            return respDTO;
        });
    }

    @Override
    public ProjectDetailRespDTO getProjectDetail(Long projectId) {
        // 查询项目基本信息
        ProjectDO projectDO = baseMapper.selectById(projectId);
        if (projectDO == null) {
            throw new ClientException("项目不存在");
        }
        // 查询项目详情信息
        ProjectDetailDO detailDO = projectDetailMapper.selectByProjectId(projectId);
        if (detailDO == null) {
            throw new ClientException("项目详情不存在");
        }

        // 组装成最终 DTO
        ProjectDetailRespDTO respDTO = new ProjectDetailRespDTO();
        respDTO.setProjectId(projectId);
        respDTO.setTitle(projectDO.getTitle());
        respDTO.setCategory(projectDO.getCategory());
        respDTO.setTimeRange(detailDO.getTimeRange());
        respDTO.setDescriptionMd(detailDO.getDescriptionMd());
        respDTO.setImageUrls(JSON.parseArray(detailDO.getImageUrls(), String.class));
        // 反序列化 技术栈标签
        List<String> techStackDetail = JSON.parseArray(detailDO.getTechStackTags(), String.class);
        respDTO.setTechStackTags(techStackDetail);
        // 反序列化 团队成员分工
        List<TeamDivisionDTO> teamDivisions = JSON.parseArray(detailDO.getTeamDivision(),
                TeamDivisionDTO.class);
        respDTO.setTeamDivisions(teamDivisions);

        // 设置关联对象
        respDTO.setAwards(toAwardResponses(projectAwardMapper.selectAwardsByProjectId(projectId)));
        return respDTO;
    }

    @Override
    @Transactional
    public Long saveOrUpdateProject(Long projectId, ProjectSaveReqDTO reqDTO) {
        // 1. 保存项目基本信息
        ProjectDO projectDO = new ProjectDO();
        if (projectId != null) {
            projectDO.setId(projectId);
            projectDO = this.getById(projectId);
            if (projectDO == null) {
                throw new ClientException("项目不存在");
            }
        }

        // 更新字段
        // projectDO.setCategory(reqDTO.getCategory());
        // projectDO.setCoverImage(reqDTO.getCoverImage());
        // projectDO.setTitle(reqDTO.getTitle());
        // projectDO.setBriefIntro(reqDTO.getBriefIntro());
        BeanUtil.copyProperties(reqDTO, projectDO);
        projectDO.setTechStackTags(JSON.toJSONString(reqDTO.getTechStackTags()));
        // 提取开发团队人名
        List<String> teamMembers = reqDTO.getTeamDivision().stream()
                .map(TeamDivisionDTO::getName)
                .toList();
        projectDO.setTeamMembers(JSON.toJSONString(teamMembers));

        if (projectId == null) {
            this.save(projectDO);
            projectId = projectDO.getId();
        } else {
            this.updateById(projectDO);
        }

        // 2. 保存项目详情信息
        saveProjectDetail(projectId, reqDTO);

        replaceProjectAwards(projectId, reqDTO.getAwardIds());

        return projectId;
    }

    @Override
    public ProjectEditRespDTO getProjectEdit(Long projectId) {
        // 查询项目基础信息
        ProjectDO projectDO = this.getById(projectId);
        if (projectDO == null) {
            throw new ClientException("项目不存在");
        }
        // 查询项目详情信息
        ProjectDetailDO detailDO = projectDetailMapper.selectByProjectId(projectId);
        if (detailDO == null) {
            throw new ClientException("项目详情不存在");
        }
        // 拷贝项目基础信息到 DTO
        ProjectEditRespDTO respDTO = new ProjectEditRespDTO();
        respDTO.setProjectId(projectId);
        respDTO.setCategory(projectDO.getCategory());
        respDTO.setCoverImage(projectDO.getCoverImage());
        respDTO.setTitle(projectDO.getTitle());
        respDTO.setBriefIntro(projectDO.getBriefIntro());
        respDTO.setTimeRange(detailDO.getTimeRange());
        respDTO.setDescriptionMd(detailDO.getDescriptionMd());
        // 反序列化 技术栈标签
        List<String> techStackTags = JSON.parseArray(projectDO.getTechStackTags(), String.class);
        respDTO.setTechStackTags(techStackTags);
        // 反序列化 开发团队成员
        List<TeamDivisionDTO> teamDivisions = JSON.parseArray(detailDO.getTeamDivision(), TeamDivisionDTO.class);
        respDTO.setTeamDivisions(teamDivisions);
        respDTO.setImageUrls(JSON.parseArray(detailDO.getImageUrls(), String.class));
        respDTO.setAwards(toAwardResponses(projectAwardMapper.selectAwardsByProjectId(projectId)));
        return respDTO;
    }

    @Override
    @Transactional
    public void delete(Long projectId) {
        projectAwardMapper.delete(new LambdaQueryWrapper<ProjectAwardDO>()
                .eq(ProjectAwardDO::getProjectId, projectId));
        // 删除项目详情
        projectDetailMapper.deleteByProjectId(projectId);
        // 删除项目
        this.removeById(projectId);
    }

    private void saveProjectDetail(Long projectId, ProjectSaveReqDTO reqDTO) {
        ProjectDetailDO detailDO = projectDetailMapper.selectByProjectId(projectId);
        if (detailDO == null) {
            detailDO = new ProjectDetailDO();
            detailDO.setProjectId(projectId);
        }

        detailDO.setTimeRange(reqDTO.getTimeRange());
        detailDO.setTechStackTags(JSON.toJSONString(reqDTO.getTechStackTags()));
        detailDO.setTeamDivision(JSON.toJSONString(reqDTO.getTeamDivision()));
        detailDO.setImageUrls(JSON.toJSONString(reqDTO.getImageUrls()));
        detailDO.setDescriptionMd(reqDTO.getDescriptionMd());

        if (detailDO.getId() == null) {
            projectDetailMapper.insert(detailDO);
        } else {
            projectDetailMapper.updateById(detailDO);
        }
    }

    /**
     * 以请求顺序替换项目奖项关联。
     */
    private void replaceProjectAwards(Long projectId, List<Long> awardIds) {
        List<Long> ids = awardIds == null ? List.of() : new ArrayList<>(new LinkedHashSet<>(awardIds));
        if (!ids.isEmpty()) {
            long count = awardService.count(new LambdaQueryWrapper<AwardDO>().in(AwardDO::getId, ids));
            if (count != ids.size()) {
                throw new ClientException("使用了不存在的奖项");
            }
        }
        projectAwardMapper.delete(new LambdaQueryWrapper<ProjectAwardDO>()
                .eq(ProjectAwardDO::getProjectId, projectId));
        for (int index = 0; index < ids.size(); index++) {
            projectAwardMapper.insert(new ProjectAwardDO()
                    .setProjectId(projectId)
                    .setAwardId(ids.get(index))
                    .setSortOrder(index));
        }
    }

    private List<AwardRespDTO> toAwardResponses(List<AwardDO> awards) {
        return awards.stream().map(each -> {
            AwardRespDTO awardRespDTO = BeanUtil.toBean(each, AwardRespDTO.class);
            awardRespDTO.setWinners(JSON.parseArray(each.getWinners(), String.class));
            return awardRespDTO;
        }).toList();
    }

}
