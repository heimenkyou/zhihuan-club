<template>
  <div class="admin-container">
    <el-header class="admin-header">
      <div class="header-left">
        <span class="admin-title">社团管理后台</span>
      </div>
      <div class="header-right">
        <span>{{ userInfo?.username }}</span>
        <el-button type="text" @click="handleLogout">退出登录</el-button>
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
// 替换原来的导入方式
import { House } from "@element-plus/icons-vue"
import { User } from "@element-plus/icons-vue"
import { Message } from "@element-plus/icons-vue"
import { Trophy } from "@element-plus/icons-vue"
import { Box } from "@element-plus/icons-vue" // 导入Box图标

import { useRouter } from "vue-router"
import { useAdminStore } from "../../../stores/adminStore"
import { ref, onMounted, computed } from "vue"
import { ElMessage } from "element-plus"

const router = useRouter()
const adminStore = useAdminStore()
const userInfo = ref(adminStore.userInfo)

onMounted(() => {
  adminStore.checkLoginStatus()
  if (!adminStore.isLoggedIn) {
    router.push("/admin/login")
  } else {
    userInfo.value = adminStore.userInfo
  }
})

const handleMenuSelect = (key: string) => {
  router.push(key)
}

const handleLogout = () => {
  adminStore.logout()
  ElMessage.success("退出登录成功")
  router.push("/admin/login")
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
  background-color: #1890ff;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
.header-left {
  display: flex;
  align-items: center;
}
.admin-title {
  font-size: 20px;
  font-weight: bold;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.admin-aside {
  background-color: #304156;
  color: white;
}
.el-menu-vertical-demo {
  background-color: #304156;
  color: white;
}
.el-menu-item {
  color: #bfcbd9;
}
.el-menu-item.is-active {
  background-color: #1890ff;
  color: white;
}
.admin-main {
  padding: 20px;
  overflow-y: auto;
}
</style>
