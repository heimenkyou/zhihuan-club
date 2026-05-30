package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.CodeSubmissionReqDTO;
import cn.luowb.clubrecruitment.dto.resp.CodeSubmissionRespDTO;
import cn.luowb.clubrecruitment.service.CodeSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/code-submissions")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "代码提交")
@Validated
public class CodeSubmissionController {
    private final CodeSubmissionService codeSubmissionService;

    /**
     * 提交代码
     * 流程：上传代码压缩包->上传演示视频->保存提交信息
     */
    @Operation(summary = "提交代码")
    @PostMapping
    public Result<CodeSubmissionRespDTO> createCodeSubmission(@Valid @ModelAttribute CodeSubmissionReqDTO requestParam) {
        log.info("收到代码提交请求，学号: {}", requestParam.getStudentId());
        CodeSubmissionRespDTO result = codeSubmissionService.createCodeSubmission(requestParam);
        return Results.success(result);
    }
}
