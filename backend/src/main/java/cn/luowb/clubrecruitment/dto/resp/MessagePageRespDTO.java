package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "留言分页查询返回实体")
public class MessagePageRespDTO {
    /**
     * 主键ID
     */
    @Schema(description = "留言ID")
    private Long id;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 留言内容
     */
    @Schema(description = "留言内容")
    private String content;

    /**
     * 点赞数量
     */
    @Schema(description = "点赞数量")
    private Integer likeCount;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 当前访客是否已点赞
     */
    @Schema(description = "当前访客是否已点赞")
    private Boolean liked;

    /**
     * 当前访客是否可删除此留言
     */
    @Schema(description = "当前访客是否可删除此留言")
    private Boolean canDelete;
}
