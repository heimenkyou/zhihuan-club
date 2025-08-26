package cn.luowb.clubrecruitment.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 奖项信息返回数据
 */
@Data
public class AwardRespDTO {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 竞赛名称(如蓝桥杯、ACM/ICPC)
     */
    @Schema(description = "竞赛名称(如蓝桥杯、ACM/ICPC)")
    private String competitionName;

    /**
     * 赛道/组别
     */
    @Schema(description = "赛道/组别")
    private String competitionTrack;

    /**
     * 竞赛级别（校级/省级/国家级）
     */
    @Schema(description = "竞赛级别（校级/省级/国家级）")
    private String competitionLevel;

    /**
     * 奖项级别（一等奖/二等奖/优胜奖等）
     */
    @Schema(description = "奖项级别（一等奖/二等奖/优胜奖等）")
    private String awardLevel;

    /**
     * 获奖人(JSON数组格式存储人名)
     */
    @Schema(description = "获奖人(JSON数组格式存储人名)")
    private List<String> winners;

    /**
     * 获奖年份(用于分类展示)
     */
    @Schema(description = "获奖年份(用于分类展示)")
    private Integer year;

    /**
     * 获奖时间(精确到月份)
     */
    @Schema(description = "获奖时间(精确到月份)")
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate awardDate;
}