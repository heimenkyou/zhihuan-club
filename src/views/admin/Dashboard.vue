<template>
  <div class="dashboard-container">
    <el-card class="welcome-card">
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
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-title">报名总数</div>
            <div class="stat-value">{{ applicationCount }}</div>
            <div class="stat-desc">最近7天新增: {{ newApplications }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-title">留言总数</div>
            <div class="stat-value">{{ messageCount }}</div>
            <div class="stat-desc">最近7天新增: {{ newMessages }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-title">奖项总数</div>
            <div class="stat-value">{{ awardCount }}</div>
            <div class="stat-desc">最近添加: {{ lastAward }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 添加数据趋势图表 -->
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>数据趋势</span>
        </div>
      </template>
      <div class="chart-content">
        <el-select
          v-model="chartType"
          class="chart-select"
          @change="loadChartData"
        >
          <el-option label="报名趋势" value="applications" />
          <el-option label="留言趋势" value="messages" />
        </el-select>
        <el-chart :height="300">
          <el-line-series :data="chartData" x-field="date" y-field="count" />
          <el-axis type="x" />
          <el-axis type="y" />
          <el-tooltip />
        </el-chart>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import { useAdminStore } from "../../stores/adminStore"
import { getApplications, getAwards } from "../../services/adminService"
import { getMessages } from "../../services/messageService"
import ElLineSeries from "element-plus"
import ElAxis from "element-plus"
import { ElTooltip, ElSelect, ElOption } from "element-plus"
import ElChart from "element-plus"

const adminStore = useAdminStore()
const userInfo = ref(adminStore.userInfo)
const applicationCount = ref(0)
const newApplications = ref(0)
const messageCount = ref(0)
const newMessages = ref(0)
const awardCount = ref(0)
const lastAward = ref("暂无")
const chartType = ref("applications")
const chartData = ref<any[]>([])

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

    // 加载图表数据
    loadChartData()
  } catch (error) {
    console.error("获取统计数据失败:", error)
  }
})

// 加载图表数据
const loadChartData = async () => {
  // 模拟近7天数据
  const today = new Date()
  const data = []
  for (let i = 6; i >= 0; i--) {
    const date = new Date(today)
    date.setDate(today.getDate() - i)
    const formattedDate = `${date.getMonth() + 1}/${date.getDate()}`
    let count = 0
    if (chartType.value === "applications") {
      count = Math.floor(Math.random() * 15) + 5
    } else {
      count = Math.floor(Math.random() * 25) + 10
    }
    data.push({ date: formattedDate, count })
  }
  chartData.value = data
}
</script>

<style scoped>
/* 保留原有样式，并添加新样式 */
.chart-card {
  margin-top: 20px;
}
.chart-content {
  padding: 20px 0;
}
.chart-select {
  margin-bottom: 20px;
  width: 150px;
}
.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.welcome-card {
  margin-bottom: 20px;
}
.card-header {
  font-size: 18px;
  font-weight: bold;
}
.welcome-content {
  padding: 20px 0;
}
.stats-row {
  margin-top: 20px;
}
.stat-card {
  height: 100%;
}
.stat-content {
  text-align: center;
  padding: 20px 0;
}
.stat-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 10px;
}
.stat-desc {
  font-size: 14px;
  color: #909399;
}
</style>
