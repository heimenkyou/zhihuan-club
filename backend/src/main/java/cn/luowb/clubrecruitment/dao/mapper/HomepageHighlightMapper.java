package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.HomepageHighlightDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 首页高光配置的数据访问接口。
 */
public interface HomepageHighlightMapper extends BaseMapper<HomepageHighlightDO> {

    /**
     * 加锁查询全部高光记录 id（按 id 排序）。
     * 所有会变更高光集合/排序的业务路径必须在同一事务内先调用本方法，
     * 借助 InnoDB 行锁将新增、删除、重排串行化，避免 MAX+1、删除、重排互相竞态。
     *
     * @return 全部高光记录 id 列表
     */
    @Select("SELECT id FROM homepage_highlight ORDER BY id FOR UPDATE")
    List<Long> selectAllIdsForUpdate();

    /**
     * 计算新增记录应使用的下一个排序值（当前 MAX + 1）。
     * 需在持有 {@link #selectAllIdsForUpdate()} 行锁的事务内调用。
     *
     * @return 下一个排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), -1) + 1 FROM homepage_highlight")
    Integer selectNextSortOrder();
}
