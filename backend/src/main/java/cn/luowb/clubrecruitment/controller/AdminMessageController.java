package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 留言后台管理接口。
 */
@RestController
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
@Tag(name = "留言管理")
public class AdminMessageController {
    private final MessageService messageService;

    /**
     * 分页查询留言。
     *
     * @param requestParam 分页参数
     * @return 留言分页数据
     */
    @Operation(summary = "分页查询留言")
    @GetMapping
    public Result<PageData<MessagePageRespDTO>> list(@ParameterObject PageReqDTO requestParam) {
        return Results.success(messageService.getMessageList(requestParam));
    }

    /**
     * 管理员删除留言。
     *
     * @param id 留言 ID
     * @return 空响应
     */
    @Operation(summary = "删除留言")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return Results.success();
    }
}
