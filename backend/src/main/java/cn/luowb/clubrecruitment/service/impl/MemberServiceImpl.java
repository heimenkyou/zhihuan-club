package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.MemberExistException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.MemberDO;
import cn.luowb.clubrecruitment.dao.mapper.MemberMapper;
import cn.luowb.clubrecruitment.dto.req.MemberPageReqDTO;
import cn.luowb.clubrecruitment.dto.req.MemberReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MemberPageRespDTO;
import cn.luowb.clubrecruitment.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/** 成员服务实现。 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, MemberDO>
        implements MemberService {

    /** 合法成员状态集合 */
    private static final Set<String> VALID_STATUS = Set.of("active", "inactive");

    private final MemberMapper memberMapper;

    @Override
    public PageData<MemberPageRespDTO> getMemberPage(MemberPageReqDTO requestParam) {
        LambdaQueryWrapper<MemberDO> queryWrapper = Wrappers.lambdaQuery(MemberDO.class);
        queryWrapper
                .like(StrUtil.isNotBlank(requestParam.getName()), MemberDO::getName, requestParam.getName())
                .like(StrUtil.isNotBlank(requestParam.getStudentId()), MemberDO::getStudentId, requestParam.getStudentId())
                .like(StrUtil.isNotBlank(requestParam.getMajor()), MemberDO::getMajor, requestParam.getMajor())
                .eq(StrUtil.isNotBlank(requestParam.getDepartment()), MemberDO::getDepartment, requestParam.getDepartment())
                .eq(requestParam.getJoinYear() != null, MemberDO::getJoinYear, requestParam.getJoinYear())
                .eq(StrUtil.isNotBlank(requestParam.getStatus()), MemberDO::getStatus, requestParam.getStatus())
                .orderByDesc(MemberDO::getCreateTime);
        Page<MemberDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        page = this.page(page, queryWrapper);
        return PageData.of(page, each -> BeanUtil.toBean(each, MemberPageRespDTO.class));
    }

    @Override
    public MemberPageRespDTO getMemberDetail(Long id) {
        MemberDO memberDO = memberMapper.selectById(id);
        if (memberDO == null) {
            throw new ClientException("成员不存在");
        }
        return BeanUtil.toBean(memberDO, MemberPageRespDTO.class);
    }

    @Override
    public void createMember(MemberReqDTO requestParam) {
        // 学号唯一，重复时携带已有成员ID返回，前端据此提示并打开已有成员
        MemberDO existMember = this.getOne(new LambdaQueryWrapper<MemberDO>()
                .eq(MemberDO::getStudentId, requestParam.getStudentId()));
        if (existMember != null) {
            throw new MemberExistException(existMember.getId());
        }
        MemberDO memberDO = BeanUtil.toBean(requestParam, MemberDO.class);
        memberDO.setStatus(StrUtil.blankToDefault(requestParam.getStatus(), "active"));
        validateStatus(memberDO.getStatus());
        if (!this.save(memberDO)) {
            throw new ClientException("保存成员信息失败");
        }
    }

    @Override
    public void updateMember(Long id, MemberReqDTO requestParam) {
        MemberDO memberDO = memberMapper.selectById(id);
        if (memberDO == null) {
            throw new ClientException("成员不存在");
        }
        // 学号调整时不允许与其他成员冲突，自身的学号保持原值视为不调整
        List<MemberDO> sameStudent = this.list(new LambdaQueryWrapper<MemberDO>()
                .eq(MemberDO::getStudentId, requestParam.getStudentId()));
        if (sameStudent.stream().anyMatch(item -> !item.getId().equals(id))) {
            throw new MemberExistException(sameStudent.stream()
                    .filter(item -> !item.getId().equals(id))
                    .map(MemberDO::getId)
                    .findFirst()
                    .orElse(null));
        }
        MemberDO updateDO = BeanUtil.toBean(requestParam, MemberDO.class);
        updateDO.setId(id);
        // 编辑保留原状态，未传状态时按原值；传了则允许重新启用（active）
        if (StrUtil.isBlank(requestParam.getStatus())) {
            updateDO.setStatus(memberDO.getStatus());
        }
        validateStatus(updateDO.getStatus());
        if (!this.updateById(updateDO)) {
            throw new ClientException("更新成员信息失败");
        }
    }

    @Override
    public void updateMemberStatus(Long id, String status) {
        validateStatus(status);
        MemberDO memberDO = memberMapper.selectById(id);
        if (memberDO == null) {
            throw new ClientException("成员不存在");
        }
        memberDO.setStatus(status);
        if (!this.updateById(memberDO)) {
            throw new ClientException("更新成员状态失败");
        }
    }

    /**
     * 校验成员状态仅允许 active/inactive
     *
     * @param status 目标状态
     */
    private void validateStatus(String status) {
        if (!VALID_STATUS.contains(status)) {
            throw new ClientException("成员状态仅支持 active/inactive");
        }
    }
}
