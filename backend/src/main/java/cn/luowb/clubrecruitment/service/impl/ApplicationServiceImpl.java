package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // 招新批次年份由后端按当前年份填入，不接收前端传值
        int applicationYear = LocalDate.now().getYear();
        ApplicationDO applicationDO = this.getOne(
                new LambdaQueryWrapper<ApplicationDO>()
                        .eq(ApplicationDO::getStudentId, requestParam.getStudentId())
                        .eq(ApplicationDO::getApplicationYear, applicationYear)
        );
        ApplicationDO newApplicationDO = BeanUtil.toBean(requestParam, ApplicationDO.class);
        newApplicationDO.setApplicationYear(applicationYear);
        // 从学号中获取班级 例如20230613109 -> B231
        String studentId = requestParam.getStudentId();
        String className = "B" + studentId.substring(2, 4) + studentId.charAt(8);
        newApplicationDO.setClassName(className);
        // 第一阶段尝试方向转换为JSON数组字符串
        if (requestParam.getInitialDirections() != null) {
            newApplicationDO.setInitialDirections(JSONArray.toJSONString(requestParam.getInitialDirections()));
        }
        if (applicationDO != null) {
            // 该学号同年已报名，覆盖更新；不同年份则走新增
            newApplicationDO.setId(applicationDO.getId());
            if (!this.updateById(newApplicationDO)) {
                throw new ServiceException("修改报名信息失败");
            }
        } else {
            // 该学号同年未报名，则新增
            try {
                if (!this.save(newApplicationDO)) {
                    throw new ServiceException("保存报名信息失败");
                }
            } catch (DuplicateKeyException ex) {
                // 并发报名时先查后插的查询结果已过期，唯一键冲突改走覆盖更新
                ApplicationDO exist = this.getOne(
                        new LambdaQueryWrapper<ApplicationDO>()
                                .eq(ApplicationDO::getStudentId, newApplicationDO.getStudentId())
                                .eq(ApplicationDO::getApplicationYear, newApplicationDO.getApplicationYear())
                );
                if (exist == null) {
                    throw new ServiceException("保存报名信息失败");
                }
                newApplicationDO.setId(exist.getId());
                if (!this.updateById(newApplicationDO)) {
                    throw new ServiceException("修改报名信息失败");
                }
            }
        }
    }

    @Override
    public PageData<ApplicationPageDTO> getApplicationList(ApplicationPageReqDTO requestParam) {
        LambdaQueryWrapper<ApplicationDO> queryWrapper = Wrappers.lambdaQuery(ApplicationDO.class);
        queryWrapper
                .like(StrUtil.isNotBlank(requestParam.getName()), ApplicationDO::getName, requestParam.getName())
                .like(StrUtil.isNotBlank(requestParam.getStudentId()), ApplicationDO::getStudentId, requestParam.getStudentId())
                .eq(requestParam.getApplicationYear() != null, ApplicationDO::getApplicationYear, requestParam.getApplicationYear())
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
                    // 第一阶段尝试方向转换为JSON数组
                    if (each.getInitialDirections() != null) {
                        pageDTO.setInitialDirections(JSONArray.parseArray(each.getInitialDirections(), String.class));
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

    @Override
    public List<Integer> getApplicationYears() {
        return applicationMapper.selectDistinctYears();
    }
}
