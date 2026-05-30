package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dao.entity.CodeSubmissionDO;
import cn.luowb.clubrecruitment.dto.req.CodeSubmissionReqDTO;
import cn.luowb.clubrecruitment.dto.resp.CodeSubmissionRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author heimenkyou
 * @description 针对表【code_submission(代码提交记录表)】的数据库操作Service
 * @createDate 2025-10-09 08:17:32
 */
public interface CodeSubmissionService extends IService<CodeSubmissionDO> {

    /**
     * 创建代码提交记录
     *
     * @param requestParam 代码提交请求参数
     * @return 代码提交响应DTO
     */
    CodeSubmissionRespDTO createCodeSubmission(CodeSubmissionReqDTO requestParam);
}
