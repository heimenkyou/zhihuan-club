package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "留言")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "添加留言")
    @PostMapping
    public Result<Void> createMessage(@RequestBody MessageReqDTO requestParam) {
        messageService.createMessage(requestParam);
        return Results.success();
    }

    @Operation(summary = "分页获取留言列表")
    @GetMapping
    public Result<IPage<MessagePageRespDTO>> getMessageList(Page<MessageDO> requestParam) {
        return Results.success(messageService.getMessageList(requestParam));
    }

    @Operation(summary = "点赞留言")
    @PostMapping("/{id}/like")
    public Result<Void> likeMessage(@PathVariable Long id) {
        messageService.likeMessage(id);
        return Results.success();
    }

    @Operation(summary = "删除留言")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMessage(@Schema(description = "留言ID") @PathVariable Long id) {
        messageService.removeById(id);
        return Results.success();
    }
}
