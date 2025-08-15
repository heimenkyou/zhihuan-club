package cn.luowb.clubrecruitment.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum LikeAction {
    @Schema(description = "已点赞")
    LIKED,
    @Schema(description = "已取消点赞")
    UNLIKED
}
