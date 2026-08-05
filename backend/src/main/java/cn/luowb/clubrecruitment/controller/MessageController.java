package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.enums.LikeAction;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "留言")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "添加留言")
    @PostMapping
    public Result<Void> createMessage(@RequestBody @Valid MessageReqDTO requestParam) {
        log.debug("添加留言: {}", requestParam);
        messageService.createMessage(requestParam);
        return Results.success();
    }

    @Operation(summary = "分页获取留言列表")
    @GetMapping
    public Result<PageData<MessagePageRespDTO>> getMessageList(@ParameterObject PageReqDTO requestParam) {
        log.debug("分页获取留言: {}", requestParam);
        PageData<MessagePageRespDTO> messageList = messageService.getMessageList(requestParam);
        return Results.success(messageList);
    }

    @Operation(summary = "点赞/取消点赞")
    @PostMapping("/{id}/like")
    public Result<LikeAction> likeMessage(@PathVariable Long id) {
        log.debug("点赞留言：{}", id);
        LikeAction action = messageService.toggleLikeMessage(id);
        return Results.success(action);
    }

    @Operation(summary = "删除留言", description = "只有留言对应的 IP 才能删除留言")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMessage(@Schema(description = "留言ID") @PathVariable Long id) {
        log.debug("删除留言：{}", id);
        messageService.deleteMessage(id);
        return Results.success();
    }
}
