package cn.luowb.clubrecruitment.service.impl;

import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.dao.entity.HomepageHighlightDO;
import cn.luowb.clubrecruitment.dao.entity.ProjectDO;
import cn.luowb.clubrecruitment.dao.mapper.HomepageHighlightMapper;
import cn.luowb.clubrecruitment.dao.mapper.ProjectMapper;
import cn.luowb.clubrecruitment.dto.req.HomepageHighlightActivityReqDTO;
import cn.luowb.clubrecruitment.dto.resp.HomepageHighlightRespDTO;
import cn.luowb.clubrecruitment.service.HomepageHighlightService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 首页高光服务实现。 */
@Service
@RequiredArgsConstructor
public class HomepageHighlightServiceImpl extends ServiceImpl<HomepageHighlightMapper, HomepageHighlightDO>
        implements HomepageHighlightService {
    private static final String TYPE_PROJECT = "project";
    private static final String TYPE_ACTIVITY = "activity";

    private final ProjectMapper projectMapper;

    @Override
    public List<HomepageHighlightRespDTO> listAll() {
        List<HomepageHighlightDO> highlights = this.list(new LambdaQueryWrapper<HomepageHighlightDO>()
                .orderByAsc(HomepageHighlightDO::getSortOrder)
                .orderByAsc(HomepageHighlightDO::getId));
        if (highlights.isEmpty()) {
            return List.of();
        }
        // 批量查询项目高光关联的项目信息，保证项目修改后自动反映到首页
        List<Long> projectIds = highlights.stream()
                .filter(each -> TYPE_PROJECT.equals(each.getItemType()))
                .map(HomepageHighlightDO::getProjectId)
                .distinct()
                .toList();
        Map<Long, ProjectDO> projectMap = projectIds.isEmpty() ? Map.of()
                : projectMapper.selectBatchIds(projectIds).stream()
                        .collect(Collectors.toMap(ProjectDO::getId, Function.identity()));
        return highlights.stream().map(each -> toRespDTO(each, projectMap.get(each.getProjectId()))).toList();
    }

    @Override
    @Transactional
    public Long addProject(Long projectId) {
        // 先持有行锁，串行化新增与删除、重排
        baseMapper.selectAllIdsForUpdate();
        if (projectMapper.selectById(projectId) == null) {
            throw new ClientException("项目不存在");
        }
        HomepageHighlightDO highlightDO = new HomepageHighlightDO()
                .setItemType(TYPE_PROJECT)
                .setProjectId(projectId)
                .setSortOrder(nextSortOrder());
        try {
            this.save(highlightDO);
        } catch (DuplicateKeyException ex) {
            // 唯一索引防并发重复，转换为友好提示
            throw new ClientException("该项目已添加");
        }
        return highlightDO.getId();
    }

    @Override
    @Transactional
    public Long addActivity(HomepageHighlightActivityReqDTO reqDTO) {
        // 先持有行锁，串行化新增与删除、重排
        baseMapper.selectAllIdsForUpdate();
        HomepageHighlightDO highlightDO = new HomepageHighlightDO()
                .setItemType(TYPE_ACTIVITY)
                .setTitle(reqDTO.getTitle())
                .setDescription(reqDTO.getDescription())
                .setCoverImage(reqDTO.getCoverImage())
                .setActivityDate(reqDTO.getActivityDate())
                .setSortOrder(nextSortOrder());
        this.save(highlightDO);
        return highlightDO.getId();
    }

    @Override
    @Transactional
    public void updateActivity(Long id, HomepageHighlightActivityReqDTO reqDTO) {
        HomepageHighlightDO highlightDO = this.getById(id);
        if (highlightDO == null) {
            throw new ClientException("高光记录不存在");
        }
        if (!TYPE_ACTIVITY.equals(highlightDO.getItemType())) {
            throw new ClientException("仅允许编辑活动高光");
        }
        highlightDO.setTitle(reqDTO.getTitle())
                .setDescription(reqDTO.getDescription())
                .setCoverImage(reqDTO.getCoverImage())
                .setActivityDate(reqDTO.getActivityDate());
        this.updateById(highlightDO);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 先持有行锁，避免删除与新增 MAX+1、重排竞态
        baseMapper.selectAllIdsForUpdate();
        if (!this.removeById(id)) {
            throw new ClientException("高光记录不存在");
        }
    }

    @Override
    @Transactional
    public void reorder(List<Long> ids) {
        // 先持有行锁，避免重排与新增、删除竞态
        List<Long> lockedIds = baseMapper.selectAllIdsForUpdate();
        // 拒绝重复ID
        Set<Long> uniqueIds = new HashSet<>(ids);
        if (uniqueIds.size() != ids.size()) {
            throw new ClientException("排序列表存在重复记录");
        }
        // ids 必须与当前全部记录集合完全一致，不能遗漏或多出
        if (ids.size() != lockedIds.size() || !uniqueIds.containsAll(lockedIds)) {
            throw new ClientException("排序列表与当前高光记录不一致");
        }
        for (int index = 0; index < ids.size(); index++) {
            HomepageHighlightDO update = new HomepageHighlightDO();
            update.setId(ids.get(index));
            update.setSortOrder(index);
            this.updateById(update);
        }
    }

    @Override
    @Transactional
    public void deleteByProjectId(Long projectId) {
        // 与项目删除同事务调用；同样持锁，避免与集合变更竞态
        baseMapper.selectAllIdsForUpdate();
        this.remove(new LambdaQueryWrapper<HomepageHighlightDO>()
                .eq(HomepageHighlightDO::getItemType, TYPE_PROJECT)
                .eq(HomepageHighlightDO::getProjectId, projectId));
    }

    /**
     * 计算新记录排序值：当前最大排序值 + 1。
     * 必须在已持锁的事务内调用。
     */
    private Integer nextSortOrder() {
        return baseMapper.selectNextSortOrder();
    }

    /**
     * 组装统一响应 DTO；项目类型动态读取项目表字段。
     */
    private HomepageHighlightRespDTO toRespDTO(HomepageHighlightDO each, ProjectDO project) {
        HomepageHighlightRespDTO respDTO = new HomepageHighlightRespDTO();
        respDTO.setId(each.getId());
        respDTO.setType(each.getItemType());
        respDTO.setProjectId(each.getProjectId());
        respDTO.setActivityDate(each.getActivityDate());
        if (TYPE_PROJECT.equals(each.getItemType())) {
            if (project != null) {
                respDTO.setTitle(project.getTitle());
                respDTO.setDescription(project.getBriefIntro());
                respDTO.setCoverImage(project.getCoverImage());
            }
        } else {
            respDTO.setTitle(each.getTitle());
            respDTO.setDescription(each.getDescription());
            respDTO.setCoverImage(each.getCoverImage());
        }
        return respDTO;
    }
}
