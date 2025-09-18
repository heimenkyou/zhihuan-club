<template>
  <div class="admin-container">
    <el-header class="admin-header">
      <div class="header-left">
        <span class="admin-title">社团管理后台</span>
      </div>
      <div class="header-right">
        <el-dropdown trigger="hover" @command="handleCommand">
          <div class="user-info">
            <el-icon class="user-icon"><User /></el-icon>
            <span class="username">{{ userInfo?.username }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人资料
              </el-dropdown-item>
              <el-dropdown-item command="home">
                <el-icon><House /></el-icon>
                返回前台
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside class="admin-aside" width="200px">
        <el-menu
          :default-active="currentRoutePath"
          class="el-menu-vertical-demo"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/admin/dashboard">
            <template #title>
              <el-icon><House /></el-icon>
              <span>后台首页</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/applications">
            <template #title>
              <el-icon><User /></el-icon>
              <span>报名信息管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/messages">
            <template #title>
              <el-icon><Message /></el-icon>
              <span>留言板管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/awards">
            <template #title>
              <el-icon><Trophy /></el-icon>
              <span>奖项管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/projects">
            <template #title>
              <el-icon><Box /></el-icon>
              <span>项目管理</span>
            </template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="admin-main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
  // 导入Element Plus图标组件
  import {
    House,
    User,
    Message,
    Trophy,
    Box,
    ArrowDown,
    SwitchButton,
  } from '@element-plus/icons-vue'

  import { useRouter } from 'vue-router'
  import { useAdminStore } from '../../../stores/adminStore'
  import { ref, onMounted, computed } from 'vue'
  import { ElMessage } from 'element-plus'

  const router = useRouter()
  const adminStore = useAdminStore()
  const userInfo = ref(adminStore.userInfo)

  onMounted(() => {
    adminStore.checkLoginStatus()
    if (!adminStore.isLoggedIn) {
      router.push('/admin/login')
    } else {
      userInfo.value = adminStore.userInfo
    }
  })

  const handleMenuSelect = (key: string) => {
    router.push(key)
  }

  /**
   * 处理下拉菜单命令
   * @param command 菜单命令
   */
  const handleCommand = (command: string) => {
    switch (command) {
      case 'profile':
        // TODO: 跳转到个人资料页面
        ElMessage.info('个人资料功能开发中')
        break
      case 'home':
        // 返回前台首页
        router.push('/')
        break
      case 'logout':
        handleLogout()
        break
    }
  }

  /**
   * 处理退出登录
   */
  const handleLogout = () => {
    try {
      adminStore.logout()
      ElMessage.success('退出登录成功')
      router.push('/admin/login')
    } catch (error) {
      console.error('退出登录失败:', error)
      ElMessage.error('退出登录失败')
    }
  }
  // 获取当前路由路径
  const currentRoutePath = computed(() => router.currentRoute.value.path)
</script>

<style scoped>
  .admin-container {
    height: 100vh;
    display: flex;
    flex-direction: column;
  }
  .admin-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  }
  .header-left {
    display: flex;
    align-items: center;
  }
  .admin-title {
    font-size: 20px;
    font-weight: 600;
    letter-spacing: 0.5px;
  }
  .header-right {
    display: flex;
    align-items: center;
  }
  .user-info {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
}
.user-info:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-1px);
}
.user-icon {
  font-size: 18px;
  margin-right: 8px;
  color: #ffffff;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}
  .username {
  font-size: 14px;
  font-weight: 500;
  margin-right: 4px;
  color: #ffffff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}
  .dropdown-icon {
  font-size: 12px;
  transition: transform 0.3s ease;
  color: #ffffff;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}
.el-dropdown:hover .dropdown-icon {
  transform: rotate(180deg);
}
  .admin-aside {
    background-color: #304156;
    color: white;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  }
  .el-menu-vertical-demo {
    background-color: #304156;
    color: white;
    border-right: none;
  }
  .el-menu-item {
    color: #bfcbd9;
    transition: all 0.3s ease;
    border-radius: 4px;
    margin: 2px 8px;
  }
  .el-menu-item:hover {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  .el-menu-item.is-active {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    font-weight: 500;
  }
  .admin-main {
    padding: 24px;
    overflow-y: auto;
    background-color: #f5f7fa;
    min-height: calc(100vh - 60px);
  }
</style>
