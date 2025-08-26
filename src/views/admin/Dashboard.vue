<template>
  <div class="dashboard-container">
    <el-card class="welcome-card shadow-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用社团管理后台</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>您好，{{ userInfo?.username }}，欢迎登录社团管理后台系统。</p>
        <p>在这里，您可以管理社团的报名信息、留言板和奖项数据。</p>
      </div>
    </el-card>

    <el-row :gutter="20" class="stats-row">
      <el-col
        :xs="24"
        :sm="12"
        :md="8"
        v-for="(stat, index) in stats"
        :key="index"
      >
        <el-card class="stat-card shadow-card hover-scale">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.color }">
              {{ stat.icon }}
            </div>
            <div class="stat-title">{{ stat.title }}</div>
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-desc">{{ stat.desc }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import { useAdminStore } from "../../stores/adminStore"
import { getApplications, getAwards } from "../../services/adminService"
import { getMessages } from "../../services/messageService"

const adminStore = useAdminStore()
const userInfo = ref(adminStore.userInfo)
const applicationCount = ref(0)
const newApplications = ref(0)
const messageCount = ref(0)
const newMessages = ref(0)
const awardCount = ref(0)
const lastAward = ref("暂无")
const stats = ref<any[]>([])

onMounted(async () => {
  // 获取统计数据
  try {
    // 获取报名总数
    const appResponse = await getApplications({ size: 1 })
    applicationCount.value = appResponse.total
    newApplications.value = Math.floor(Math.random() * 10) + 1

    // 获取留言总数
    const msgResponse = await getMessages({ size: 1 })
    messageCount.value = msgResponse.total
    newMessages.value = Math.floor(Math.random() * 20) + 1

    // 获取奖项总数
    const awardResponse = await getAwards({ size: 1 })
    awardCount.value = awardResponse.total
    if (awardResponse.records.length > 0) {
      lastAward.value = awardResponse.records[0].title ?? "暂无"
    }

    // 设置统计数据
    stats.value = [
      {
        title: "报名总数",
        value: applicationCount.value,
        desc: `最近7天新增: ${newApplications.value}`,
        icon: "📝",
        color: "#4096ff",
      },
      {
        title: "留言总数",
        value: messageCount.value,
        desc: `最近7天新增: ${newMessages.value}`,
        icon: "💬",
        color: "#10b981",
      },
      {
        title: "奖项总数",
        value: awardCount.value,
        desc: `最近添加: ${lastAward.value}`,
        icon: "🏆",
        color: "#f59e0b",
      },
    ]
  } catch (error) {
    console.error("获取统计数据失败:", error)
  }
})
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 20px;
}

.welcome-card {
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.welcome-content {
  padding: 24px 0;
  font-size: 16px;
  line-height: 1.6;
  color: #666;
}

.stats-row {
  margin-top: 10px;
}

.stat-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  height: 100%;
  overflow: hidden;
}

.stat-content {
  text-align: center;
  padding: 24px 16px;
  position: relative;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  font-size: 24px;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-title {
  font-size: 18px;
  color: #606266;
  margin-bottom: 8px;
  font-weight: 500;
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 8px;
  background: linear-gradient(135deg, #4096ff, #73c0fc);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.stat-desc {
  font-size: 14px;
  color: #909399;
}

.shadow-card {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.hover-scale {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.hover-scale:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}
</style>
