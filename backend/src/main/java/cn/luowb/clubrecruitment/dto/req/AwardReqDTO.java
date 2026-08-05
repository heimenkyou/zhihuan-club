package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 奖项信息请求参数
 */
@Data
public class AwardReqDTO {

    /** 创建奖项时必填字段的校验分组。 */
    public interface Create {
    }

    /**
     * 竞赛名称(如蓝桥杯、ACM/ICPC)
     */
    @Schema(description = "竞赛名称(蓝桥杯、ACM/ICPC)")
    @NotBlank(message = "竞赛名称不能为空", groups = Create.class)
    private String competitionName;

    /**
     * 赛道/组别
     */
    @Schema(description = "赛道/组别")
    private String competitionTrack;

    /**
     * 竞赛级别(校级/省级/国家级)
     */
    @Schema(description = "竞赛级别(校级/省级/国家级)")
    @NotBlank(message = "竞赛级别不能为空", groups = Create.class)
    private String competitionLevel;

    /**
     * 奖项级别(一等奖/二等奖/优胜奖等)
     */
    @Schema(description = "奖项级别(一等奖/二等奖/优胜奖等)")
    @NotBlank(message = "奖项级别不能为空", groups = Create.class)
    private String awardLevel;

    /**
     * 获奖人
     */
    @Schema(description = "获奖人")
    @NotEmpty(message = "获奖人不能为空", groups = Create.class)
    private List<@NotBlank(message = "获奖人不能为空", groups = {Create.class, Default.class})
    @Pattern(regexp = "^[\\p{IsHan}]+$", message = "获奖人姓名只能包含汉字", groups = {Create.class, Default.class}) String> winners;

    /**
     * 获奖时间(精确到月份), 但传递时格式为yyyy-MM-dd
     */
    @Schema(description = "获奖时间(精确到月份), 但传递时格式为yyyy-MM-dd")
    @NotNull(message = "获奖时间不能为空", groups = Create.class)
    private LocalDate awardDate;
}
