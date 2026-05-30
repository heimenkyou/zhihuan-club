<template>
  <div class="admin-container">
    <el-header class="admin-header">
      <div class="header-left">
        <el-icon class="menu-toggle" @click="toggleMenu" v-if="isMobile">
          <component :is="isMenuCollapsed ? 'Expand' : 'Fold'" />
        </el-icon>
        <span class="admin-title">{{
          isMobile && isMenuCollapsed ? '管理后台' : '社团管理后台'
        }}</span>
      </div>
      <div class="header-right">
        <el-dropdown trigger="hover" @command="handleCommand">
          <div class="user-info">
            <el-icon class="user-icon"><User /></el-icon>
            <span class="username" v-if="!isMobile || !isMenuCollapsed">{{
              userInfo?.username
            }}</span>
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
      <el-aside
        class="admin-aside"
        :width="isMobile ? (isMenuCollapsed ? '0' : '200px') : '200px'"
        :class="{ collapsed: isMobile && isMenuCollapsed }"
      >
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

<script setup>
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
import { useAdminStore } from '@/stores/adminStore'
import { ref, onMounted, computed, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { logout as logoutApi } from '@/services/adminService'

const router = useRouter()
const adminStore = useAdminStore()
const userInfo = ref(adminStore.userInfo)
const isMenuCollapsed = ref(false)
const isMobile = ref(false)

/**
 * 根据屏幕宽度切换菜单折叠策略，避免移动端侧栏遮挡主体内容。
 */
const checkIsMobile = () => {
  isMobile.value = window.innerWidth <= 768
  if (isMobile.value) {
    isMenuCollapsed.value = true
  } else {
    isMenuCollapsed.value = false
  }
}

/**
 * 切换移动端菜单展开状态。
 */
const toggleMenu = () => {
  isMenuCollapsed.value = !isMenuCollapsed.value
}

/**
 * 响应窗口尺寸变化，保持菜单状态与当前设备形态一致。
 */
const handleResize = () => {
  checkIsMobile()
}

onMounted(async () => {
  checkIsMobile()
  window.addEventListener('resize', handleResize)

  await adminStore.checkLoginStatus()
  userInfo.value = adminStore.userInfo
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})

/**
 * 处理侧边栏导航，并在移动端点击后自动收起菜单。
 *
 * @param {string} key
 */
const handleMenuSelect = key => {
  router.push(key)
  if (isMobile.value) {
    isMenuCollapsed.value = true
  }
}

/**
 * 处理右上角下拉菜单命令。
 *
 * @param {string} command
 */
const handleCommand = command => {
  switch (command) {
    case 'profile':
      router.push('/admin/profile')
      break
    case 'home':
      router.push('/')
      break
    case 'logout':
      handleLogout()
      break
  }
}

/**
 * 退出登录时先清理本地状态，再异步通知后端，避免页面停留在无效登录态。
 */
const handleLogout = () => {
  logoutApi().catch(error => {
    console.warn('后端登出接口调用失败:', error)
  })

  adminStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/admin/login')
}

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
    height: 60px;
  }
  .header-left {
    display: flex;
    align-items: center;
  }
  .menu-toggle {
    font-size: 24px;
    margin-right: 15px;
    cursor: pointer;
    display: none;
  }
  .admin-title {
    font-size: 20px;
    font-weight: 600;
    letter-spacing: 0.5px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
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
    white-space: nowrap;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
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
    transition: width 0.3s ease;
    overflow: hidden;
  }
  .admin-aside.collapsed {
    width: 0;
  }
  .el-menu-vertical-demo {
    background-color: #304156;
    color: white;
    border-right: none;
    height: 100%;
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

  /* 移动端样式 */
  @media (max-width: 768px) {
    .menu-toggle {
      display: block;
    }

    .admin-header {
      padding: 0 15px;
      height: 50px;
    }

    .admin-title {
      font-size: 18px;
      max-width: 150px;
    }

    .user-icon {
      font-size: 16px;
      margin-right: 5px;
    }

    .username {
      font-size: 13px;
      max-width: 80px;
    }

    .admin-main {
      padding: 15px;
    }

    .admin-aside {
      position: absolute;
      z-index: 1000;
      height: calc(100vh - 50px);
      transition: width 0.3s ease;
    }
  }

  @media (max-width: 480px) {
    .admin-header {
      padding: 0 10px;
    }

    .admin-title {
      font-size: 16px;
      max-width: 120px;
    }

    .username {
      max-width: 60px;
    }

    .admin-main {
      padding: 10px;
    }
  }
</style>
