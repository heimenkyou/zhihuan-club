package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import cn.luowb.clubrecruitment.dao.mapper.ApplicationMapper;
import cn.luowb.clubrecruitment.dto.req.ApplicationPageReqDTO;
import cn.luowb.clubrecruitment.dto.req.ApplicationReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ApplicationPageDTO;
import cn.luowb.clubrecruitment.service.ApplicationService;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/** 报名服务实现。 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, ApplicationDO>
        implements ApplicationService {

    private final ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationMapper applicationMapper) {
        this.applicationMapper = applicationMapper;
    }

    @Override
    public void createApplication(ApplicationReqDTO requestParam) {
        // 参数校验
        if (StringUtils.isBlank(requestParam.getName())) {
            throw new ClientException("姓名不能为空");
        } else if (StringUtils.isBlank(requestParam.getStudentId())) {
            throw new ClientException("学号不能为空");
        } else if (StringUtils.isBlank(requestParam.getMajor())) {
            throw new ClientException("专业名称不能为空");
        } else if (StringUtils.isBlank(requestParam.getQQNumber())) {
            throw new ClientException("QQ号不能为空");
        } else if (StringUtils.isBlank(requestParam.getDepartment())) {
            throw new ClientException("第一意向部门不能为空");
        } else if (StringUtils.isBlank(requestParam.getIntroduction())) {
            throw new ClientException("个人介绍不能为空");
        } else if (StringUtils.isBlank(requestParam.getReason())) {
            throw new ClientException("加入原因不能为空");
        }
        if (!StrUtil.isNumeric(requestParam.getStudentId()) || requestParam.getStudentId().length() != 11) {
            throw new ClientException("学号格式错误");
        } else if (!StrUtil.isNumeric(requestParam.getPhone()) || requestParam.getPhone().length() != 11) {
            throw new ClientException("手机号格式错误");
        } else if (!StrUtil.isNumeric(requestParam.getQQNumber())) {
            throw new ClientException("QQ号格式错误");
        }
        ApplicationDO applicationDO = this.getOne(
                new LambdaQueryWrapper<ApplicationDO>().eq(ApplicationDO::getStudentId, requestParam.getStudentId())
        );
        ApplicationDO newApplicationDO = BeanUtil.toBean(requestParam, ApplicationDO.class);
        // 从学号中获取班级 例如20230613109 -> B231
        String studentId = requestParam.getStudentId();
        String className = "B" + studentId.substring(2, 4) + studentId.charAt(8);
        newApplicationDO.setClassName(className);
        // 兴趣方向转换为JSON数组字符串
        if (requestParam.getInterests() != null) {
            newApplicationDO.setInterests(JSONArray.toJSONString(requestParam.getInterests()));
        }
        if (applicationDO != null) {
            // 该学号已经报名, 则修改
            newApplicationDO.setId(applicationDO.getId());
            if (!this.updateById(newApplicationDO)) {
                throw new ServiceException("修改报名信息失败");
            }
        } else {
            // 该学号未报名, 则新增
            if (!this.save(newApplicationDO)) {
                throw new ServiceException("保存报名信息失败");
            }
        }
    }

    @Override
    public PageData<ApplicationPageDTO> getApplicationList(ApplicationPageReqDTO requestParam) {
        LambdaQueryWrapper<ApplicationDO> queryWrapper = Wrappers.lambdaQuery(ApplicationDO.class);
        queryWrapper
                .like(StrUtil.isNotBlank(requestParam.getName()), ApplicationDO::getName, requestParam.getName())
                .like(StrUtil.isNotBlank(requestParam.getStudentId()), ApplicationDO::getStudentId, requestParam.getStudentId())
                .in(CollectionUtil.isNotEmpty(requestParam.getMajors()), ApplicationDO::getMajor, requestParam.getMajors())
                .like(StrUtil.isNotBlank(requestParam.getQQNumber()), ApplicationDO::getQQNumber, requestParam.getQQNumber())
                .orderByDesc(ApplicationDO::getCreateTime);
        // 处理部门匹配逻辑
        String first = requestParam.getDepartment();
        String second = requestParam.getSecondDepartment();
        boolean matchAll = requestParam.isMatchAllDepartments();
        if (StrUtil.isNotBlank(first) && StrUtil.isNotBlank(second)) {
            // 两个都填了: 根据 matchAll 决定 AND 或 OR
            if (matchAll) {
                // AND：两个都必须匹配
                queryWrapper
                        .like(ApplicationDO::getDepartment, first)
                        .like(ApplicationDO::getSecondDepartment, second);
            } else {
                // OR：任一匹配
                queryWrapper.and(wrapper ->
                        wrapper.like(ApplicationDO::getDepartment, first)
                                .or()
                                .like(ApplicationDO::getSecondDepartment, second)
                );
            }
        } else if (StrUtil.isNotBlank(first)) {
            // 只有第一志愿
            queryWrapper.like(ApplicationDO::getDepartment, first);
        } else if (StrUtil.isNotBlank(second)) {
            // 只有第二志愿
            queryWrapper.like(ApplicationDO::getSecondDepartment, second);
        }
        Page<ApplicationDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        page = this.page(page, queryWrapper);
        return PageData.of(page,
                each -> {
                    ApplicationPageDTO pageDTO = BeanUtil.toBean(each, ApplicationPageDTO.class);
                    // 兴趣方向转换为JSON数组字符串
                    if (each.getInterests() != null) {
                        pageDTO.setInterests(JSONArray.parseArray(each.getInterests(), String.class));
                    }
                    return pageDTO;
                });
    }

    @Override
    public void deleteApplication(Long id) {
        if (!this.removeById(id)) {
            throw new ServiceException("删除报名信息失败");
        }
    }

    @Override
    public List<String> getApplicationMajors() {
        return applicationMapper.selectDistinctMajors();
    }
}
