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

    <el-row :gutter="isMobile ? 10 : 20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="8" v-for="(stat, index) in stats" :key="index">
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

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAdminStore } from '@/stores/adminStore'
import {
  getAdminAwards,
  getAdminMessages,
  getApplications,
} from '@/services/adminService'
import { ElMessage } from 'element-plus'

const isMobile = computed(() => {
  return window.innerWidth <= 768
})

const adminStore = useAdminStore()
const userInfo = computed(() => adminStore.userInfo)
const applicationCount = ref(0)
const messageCount = ref(0)
const awardCount = ref(0)
const lastAward = ref('暂无')
const stats = ref([])

/**
 * 加载后台真实统计数据，失败的统计项保持为零。
 */
onMounted(async () => {
  try {
    try {
      const awardData = await getAdminAwards()
      awardCount.value = awardData.length
      if (awardData.length > 0) {
        lastAward.value = awardData[0].competitionName ?? '暂无'
      }
    } catch (e) {
      console.warn('获取奖项数据失败', e)
      ElMessage.warning('获取奖项数据失败')
    }

    try {
      const msgResponse = await getAdminMessages({ size: 1 })
      messageCount.value = msgResponse.total
    } catch (e) {
      console.warn('获取留言数据失败', e)
      ElMessage.warning('获取留言数据失败')
    }

    try {
      const appResponse = await getApplications({ size: 1 })
      applicationCount.value = appResponse.total
    } catch (e) {
      console.warn('获取报名数据失败', e)
      ElMessage.warning('获取报名数据失败')
    }

    stats.value = [
      {
        title: '报名总数',
        value: applicationCount.value,
        desc: '当前累计报名数',
        icon: '📝',
        color: '#4096ff',
      },
      {
        title: '留言总数',
        value: messageCount.value,
        desc: '当前累计留言数',
        icon: '💬',
        color: '#10b981',
      },
      {
        title: '奖项总数',
        value: awardCount.value,
        desc: `最新奖项: ${lastAward.value}`,
        icon: '🏆',
        color: '#f59e0b',
      },
    ]
  } catch (error) {
    console.error('获取统计数据失败', error)
    ElMessage.error('加载数据失败，请稍后重试')
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
  background-clip: text;
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

/* 移动端样式优化 */
@media (max-width: 768px) {
  .dashboard-container {
    padding: 10px;
    gap: 15px;
  }

  .card-header {
    font-size: 18px;
  }

  .welcome-content {
    padding: 15px 0;
    font-size: 14px;
  }

  .stat-content {
    padding: 15px 10px;
  }

  .stat-icon {
    width: 50px;
    height: 50px;
    font-size: 20px;
    margin-bottom: 12px;
  }

  .stat-title {
    font-size: 16px;
    margin-bottom: 6px;
  }

  .stat-value {
    font-size: 28px;
    margin-bottom: 6px;
  }

  .stat-desc {
    font-size: 12px;
  }

  .stats-row {
    margin-top: 5px;
  }
}

@media (max-width: 480px) {
  .dashboard-container {
    padding: 5px;
    gap: 10px;
  }

  .card-header {
    font-size: 16px;
  }

  .welcome-content {
    padding: 10px 0;
    font-size: 13px;
  }

  .stat-content {
    padding: 12px 8px;
  }

  .stat-icon {
    width: 40px;
    height: 40px;
    font-size: 18px;
    margin-bottom: 10px;
  }

  .stat-title {
    font-size: 14px;
    margin-bottom: 5px;
  }

  .stat-value {
    font-size: 24px;
    margin-bottom: 5px;
  }

  .stat-desc {
    font-size: 11px;
  }
}
</style>
