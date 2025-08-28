package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.MediaUploadReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MediaResourceRespDTO;
import cn.luowb.clubrecruitment.service.MediaResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/media")
@Slf4j
@Tag(name = "媒体资源")
@RequiredArgsConstructor
public class MediaResourceController {
    private final MediaResourceService mediaResourceService;

    @PostMapping("/upload")
    @Operation(summary = "上传媒体资源")
    public Result<MediaResourceRespDTO> uploadMedia(@RequestPart("dto") MediaUploadReqDTO requestParam) {
        MediaResourceRespDTO mediaResourceRespDTO = mediaResourceService.requestParam(requestParam);
        return Results.success(mediaResourceRespDTO);
    }

    @GetMapping("/unreferenced")
    @Operation(summary = "获取未引用的媒体资源")
    public Result<List<MediaResourceRespDTO>> getUnreferencedMedia() {
        List<MediaResourceRespDTO> mediaResourceRespDTOS = mediaResourceService.getUnreferencedMedia();
        return Results.success(mediaResourceRespDTOS);
    }
}
