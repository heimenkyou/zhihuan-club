package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 媒体资源响应参数
 */
@Data
@Schema(description = "媒体资源响应参数")
public class MediaResourceRespDTO {
    /**
     * 资源ID
     */
    @Schema(description = "资源ID")
    private Long id;

    /**
     * 资源类型,image/video/audio
     */
    @Schema(description = "资源类型,image/video/audio")
    private String type;

    /**
     * 资源URL
     */
    @Schema(description = "资源URL")
    private String url;

    /**
     * 资源标题
     */
    @Schema(description = "资源标题")
    private String title;

    /**
     * 资源描述
     */
    @Schema(description = "资源描述")
    private String description;
}