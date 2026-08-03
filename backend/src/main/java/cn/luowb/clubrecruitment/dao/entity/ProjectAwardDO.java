package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 项目与奖项的关联记录。
 */
@Data
@Accessors(chain = true)
@TableName("project_award")
public class ProjectAwardDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Long awardId;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
