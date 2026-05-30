package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码提交响应DTO
 */
@Data
@Builder
@Schema(description = "代码提交响应")
public class CodeSubmissionRespDTO {
    /**
     * 提交ID
     */
    @Schema(description = "提交ID")
    private Long id;

    /**
     * 学号
     */
    @Schema(description = "学号")
    private String studentId;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 项目描述
     */
    @Schema(description = "项目描述")
    private String description;

    /**
     * 代码压缩包URL
     */
    @Schema(description = "代码压缩包URL")
    private String codeUrl;

    /**
     * 演示视频URL
     */
    @Schema(description = "演示视频URL")
    private String videoUrl;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}