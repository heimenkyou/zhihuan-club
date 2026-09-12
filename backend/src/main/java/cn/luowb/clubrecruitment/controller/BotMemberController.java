package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.errorcode.BaseErrorCode;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import cn.luowb.clubrecruitment.dao.entity.MemberDO;
import cn.luowb.clubrecruitment.dto.resp.QQMemberRespDTO;
import cn.luowb.clubrecruitment.service.ApplicationService;
import cn.luowb.clubrecruitment.service.MajorMappingService;
import cn.luowb.clubrecruitment.service.MemberService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 为智环社团群机器人提供最小化成员查询接口。
 */
@Validated
@RestController
@RequestMapping("/api/integrations/qq-members")
@RequiredArgsConstructor
@Tag(name = "群机器人集成")
public class BotMemberController {
    private final ApplicationService applicationService;
    private final MajorMappingService majorMappingService;
    private final MemberService memberService;

    @Value("${app.bot-api-token:}")
    private String botApiToken;

    /**
     * 按 QQ 号查询用于设置群名片的信息。
     *
     * @param qq QQ 号
     * @param token 机器人访问令牌
     * @return 报名表优先、成员表回退的成员信息；不存在时 data 为 null
     */
    @Operation(summary = "查询 QQ 群成员名片信息")
    @GetMapping("/{qq:\\d+}")
    public Result<QQMemberRespDTO> getMember(
            @PathVariable String qq,
            @RequestHeader(value = "X-Club-Bot-Token", defaultValue = "") String token) {
        if (botApiToken.isBlank() || !MessageDigest.isEqual(
                botApiToken.getBytes(StandardCharsets.UTF_8), token.getBytes(StandardCharsets.UTF_8))) {
            throw new ClientException("机器人访问令牌无效", BaseErrorCode.TOKEN_VERIFY_ERROR);
        }

        ApplicationDO application = applicationService.getOne(Wrappers.lambdaQuery(ApplicationDO.class)
                .eq(ApplicationDO::getQQNumber, qq)
                .orderByDesc(ApplicationDO::getUpdateTime)
                .orderByDesc(ApplicationDO::getId)
                .last("LIMIT 1"));
        if (application != null) {
            return Results.success(toResponse(
                    application.getQQNumber(), application.getMajor(), application.getClassName(), application.getName()));
        }

        MemberDO member = memberService.getOne(Wrappers.lambdaQuery(MemberDO.class)
                .eq(MemberDO::getQQNumber, qq)
                .orderByDesc(MemberDO::getUpdateTime)
                .orderByDesc(MemberDO::getId)
                .last("LIMIT 1"));
        if (member == null) {
            return Results.success(null);
        }
        return Results.success(toResponse(member.getQQNumber(), member.getMajor(), member.getClassName(), member.getName()));
    }

    private QQMemberRespDTO toResponse(String qqNumber, String major, String className, String name) {
        MajorMappingDO mapping = majorMappingService.getOne(Wrappers.lambdaQuery(MajorMappingDO.class)
                .eq(MajorMappingDO::getFullName, major)
                .last("LIMIT 1"));
        return new QQMemberRespDTO()
                .setQqNumber(qqNumber)
                .setMajor(major)
                .setMajorShortName(mapping == null ? null : mapping.getShortName())
                .setClassName(className)
                .setName(name);
    }
}
