package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.ApplicationPageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ApplicationPageDTO;
import cn.luowb.clubrecruitment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报名信息后台管理接口。
 */
@RestController
@RequestMapping("/admin/applications")
@RequiredArgsConstructor
@Tag(name = "报名管理")
public class AdminApplicationController {
    private final ApplicationService applicationService;

    /**
     * 分页查询报名信息。
     *
     * @param requestParam 查询条件
     * @return 报名分页数据
     */
    @Operation(summary = "分页查询报名信息")
    @GetMapping
    public Result<PageData<ApplicationPageDTO>> list(@ParameterObject ApplicationPageReqDTO requestParam) {
        return Results.success(applicationService.getApplicationList(requestParam));
    }

    /**
     * 删除报名信息。
     *
     * @param id 报名 ID
     * @return 空响应
     */
    @Operation(summary = "删除报名信息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return Results.success();
    }

    /**
     * 查询报名数据中使用过的专业名称。
     *
     * @return 专业名称列表
     */
    @Operation(summary = "查询报名专业")
    @GetMapping("/majors")
    public Result<List<String>> majors() {
        return Results.success(applicationService.getApplicationMajors());
    }
}
