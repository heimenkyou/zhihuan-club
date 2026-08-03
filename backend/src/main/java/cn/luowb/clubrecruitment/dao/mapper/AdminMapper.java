package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/** 管理员数据访问。 */
public interface AdminMapper extends BaseMapper<AdminDO> {

    /**
     * 根据用户名查询管理员
     *
     * @param username 用户名
     * @return 管理员实体
     */
    @Select("select * from admin where username = #{username}")
    AdminDO selectByUsername(String username);
}




