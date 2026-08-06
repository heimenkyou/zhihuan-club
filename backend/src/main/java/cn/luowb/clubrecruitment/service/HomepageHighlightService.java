package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dto.req.HomepageHighlightActivityReqDTO;
import cn.luowb.clubrecruitment.dto.resp.HomepageHighlightRespDTO;

import java.util.List;

/**
 * 首页高光服务。
 */
public interface HomepageHighlightService {

    /**
     * 查询全部高光记录，按 sort_order ASC、id ASC 排序。
     * 项目高光动态映射项目表的 title/briefIntro/coverImage。
     *
     * @return 高光列表
     */
    List<HomepageHighlightRespDTO> listAll();

    /**
     * 添加项目高光。
     *
     * @param projectId 项目ID
     * @return 高光ID
     */
    Long addProject(Long projectId);

    /**
     * 添加活动高光。
     *
     * @param reqDTO 活动高光信息
     * @return 高光ID
     */
    Long addActivity(HomepageHighlightActivityReqDTO reqDTO);

    /**
     * 更新活动高光（仅允许活动记录）。
     *
     * @param id     高光ID
     * @param reqDTO 活动高光信息
     */
    void updateActivity(Long id, HomepageHighlightActivityReqDTO reqDTO);

    /**
     * 删除高光（仅删除配置记录，不影响附件）。
     *
     * @param id 高光ID
     */
    void delete(Long id);

    /**
     * 按目标顺序全量重排高光。
     *
     * @param ids 目标顺序的高光ID列表
     */
    void reorder(List<Long> ids);

    /**
     * 删除指定项目的项目高光（供项目删除时同事务调用）。
     *
     * @param projectId 项目ID
     */
    void deleteByProjectId(Long projectId);
}
