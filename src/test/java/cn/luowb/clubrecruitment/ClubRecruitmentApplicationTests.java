package cn.luowb.clubrecruitment;

import cn.luowb.clubrecruitment.dao.entity.AdminDO;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dao.mapper.AdminMapper;
import cn.luowb.clubrecruitment.dao.mapper.MessageMapper;
import cn.luowb.clubrecruitment.dto.req.AdminReqDTO;
import cn.luowb.clubrecruitment.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
class ClubRecruitmentApplicationTests {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminMapper adminMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void databaseTest() {
        MessageDO messageDO = new MessageDO();
        messageDO.setNickname("测试用户");
        messageDO.setContent("测试内容");
        int insert = messageMapper.insert(messageDO);
        Assertions.assertEquals(1, insert);
        log.info("插入数据成功：{}", messageDO);
        messageDO = messageMapper.selectById(messageDO.getId());
        Assertions.assertNotNull(messageDO);
        Assertions.assertEquals("测试用户", messageDO.getNickname());
        log.info("查询数据：{}", messageDO);
        // 删除
        int delete = messageMapper.deleteById(messageDO.getId());
        Assertions.assertEquals(1, delete);
        log.info("删除数据成功：{}", messageDO);
        messageDO = messageMapper.selectById(messageDO.getId());
        Assertions.assertNull(messageDO);
    }

    @Test
    void addAdminTest() {
        AdminReqDTO adminReqDTO = new AdminReqDTO();
        adminReqDTO.setUsername("admin");
        adminReqDTO.setPassword("123456");
        adminReqDTO.setRole("super");
        adminService.add(adminReqDTO);
        AdminDO adminDO = adminMapper.selectByUsername("admin");
        Assertions.assertNotNull(adminDO);
    }

}
