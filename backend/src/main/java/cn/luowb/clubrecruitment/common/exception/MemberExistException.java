package cn.luowb.clubrecruitment.common.exception;

import cn.luowb.clubrecruitment.common.errorcode.BaseErrorCode;
import lombok.Getter;

/**
 * 成员学号已存在异常｜新增成员时学号重复，携带已有成员ID供前端定位
 */
@Getter
public class MemberExistException extends AbstractException {

    /**
     * 已存在成员的ID
     */
    private final Long memberId;

    public MemberExistException(Long memberId) {
        super("该学号已是社团成员", null, BaseErrorCode.MEMBER_STUDENT_ID_EXIST);
        this.memberId = memberId;
    }
}
