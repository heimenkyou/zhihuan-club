package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 奖项信息请求参数
 */
@Data
public class AwardReqDTO {

    /**
     * 竞赛名称(如蓝桥杯、ACM/ICPC)
     */
    @Schema(description = "竞赛名称(蓝桥杯、ACM/ICPC)")
    private String competitionName;

    /**
     * 竞赛级别(校级/省级/国家级)
     */
    @Schema(description = "竞赛级别(校级/省级/国家级)")
    private String competitionLevel;

    /**
     * 奖项级别(一等奖/二等奖/优胜奖等)
     */
    @Schema(description = "奖项级别(一等奖/二等奖/优胜奖等)")
    private String awardLevel;

    /**
     * 获奖人
     */
    @Schema(description = "获奖人")
    private List<String> winners;

    /**
     * 获奖时间(精确到月份), 但传递时格式为yyyy-MM-dd
     */
    @Schema(description = "获奖时间(精确到月份), 但传递时格式为yyyy-MM-dd")
    private LocalDate awardDate;
}