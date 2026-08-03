package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dao.entity.AwardDO;
import cn.luowb.clubrecruitment.dto.req.AwardReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/** 奖项服务。 */
public interface AwardService extends IService<AwardDO> {

    /**
     * 添加奖项
     *
     * @param awardReqDTO 奖项信息请求参数
     */
    void add(AwardReqDTO awardReqDTO);

    /**
     * 更新奖项
     *
     * @param id          奖项id
     * @param awardReqDTO 奖项信息请求参数
     */
    void update(Long id, AwardReqDTO awardReqDTO);

    /**
     * 删除未被项目引用的奖项。
     *
     * @param id 奖项ID
     */
    void delete(Long id);
}
