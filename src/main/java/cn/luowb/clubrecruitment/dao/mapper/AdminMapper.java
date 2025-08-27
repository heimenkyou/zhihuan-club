package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author heimenkyou
 * @description 针对表【admin(管理员账户信息表，存储系统管理员身份凭证)】的数据库操作Mapper
 * @createDate 2025-08-26 16:56:13
 * @Entity cn.luowb.clubrecruitment.dao.entity.AdminDO
 */
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




