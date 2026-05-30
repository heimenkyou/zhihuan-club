<template>
  <div class="projects-page min-h-screen font-sans text-gray-800 bg-gray-50">
    <CommonNavbar />

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 mt-16">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1
          class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold text-gray-800 mb-4"
        >
          我们的项目
        </h1>
        <p class="text-gray-600 max-w-3xl">
          探索我们团队开发的各类创新项目，都是小团队鼓捣出来的小东西，期望以后有更多的人加入，一起打造更加强大的项目。
        </p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="col-span-full text-center py-16">
        <div
          class="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"
        ></div>
        <p class="mt-4 text-gray-600">正在加载项目列表...</p>
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="col-span-full text-center py-16">
        <p class="text-red-600">{{ error }}</p>
        <button
          @click="fetchProjects"
          class="mt-4 text-primary hover:underline"
        >
          重试
        </button>
      </div>

      <!-- 项目列表 -->
      <div
        v-else
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8 items-stretch"
      >
        <!-- 无项目提示 -->
        <div
          v-if="projects.length === 0"
          class="col-span-full text-center text-gray-500 py-12"
        >
          <p>暂无项目</p>
        </div>

        <!-- 项目卡片 -->
        <div
          v-for="(project, index) in projects"
          :key="project.id"
          class="bg-white rounded-xl shadow-lg overflow-hidden transform hover:-translate-y-2 transition-all duration-300 animate-fade-in cursor-pointer h-full flex flex-col"
          :style="{ animationDelay: `${0.1 + index * 0.1}s` }"
          @click="goToDetail(project.id)"
        >
          <img
            :src="project.coverImage || ''"
            :alt="project.title"
            class="w-full h-48 object-cover"
            loading="lazy"
          />
          <div class="p-6 flex flex-col flex-1">
            <div class="flex justify-between items-start mb-4">
              <h3 class="text-xl font-bold text-gray-800">
                {{ project.title }}
              </h3>
              <span
                class="bg-accent/10 text-accent text-xs px-2 py-1 rounded-full font-medium"
              >
                {{ project.category || '未分类' }}
              </span>
            </div>
            <p class="text-gray-600 mb-6 line-clamp-3">
              {{ project.briefIntro || '暂无项目简介' }}
            </p>
            <div class="flex flex-wrap gap-2 mb-6">
              <span
                v-for="tag in project.techStackTags || []"
                :key="tag"
                class="bg-[var(--tag-bg)] text-[var(--tag-text)] text-xs px-2 py-1 rounded-full"
                :style="{
                  '--tag-bg': getTagBg(tag),
                  '--tag-text': getTagText(tag),
                }"
              >
                {{ tag }}
              </span>
            </div>
            <div class="text-center text-gray-500 text-sm mt-auto pt-4">
              点击卡片查看详情
            </div>
          </div>
        </div>
      </div>

      <!-- Element Plus 分页组件 -->
      <div
        class="mt-12 text-center"
        v-if="!loading && !error && totalPages > 0"
      >
        <el-pagination
          :hide-on-single-page="true" 
          v-model:current-page="current"
          v-model:page-size="pageSize"
          :total="totalProjects"
          layout="total, prev, pager, next, jumper"
          background
          @current-change="handleCurrentChange"
        />
      </div>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup>
import CommonFooter from '@/components/CommonFooter.vue'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CommonNavbar from '@/components/CommonNavbar.vue'
import { getProjects } from '@/services/projectService'
import { ElPagination } from 'element-plus'

const router = useRouter()
const pageSize = ref(8)
const current = ref(1)
const projects = ref([])
const totalProjects = ref(0)
const totalPages = ref(0)
const loading = ref(false)
const error = ref('')

/**
 * 获取项目列表，并在失败时重置分页区，避免界面残留旧数据。
 */
const fetchProjects = async () => {
  loading.value = true
  error.value = ''

  try {
    const params = {
      current: current.value,
      size: pageSize.value,
    }

    const response = await getProjects(params)

    if (response && response.data && response.data.records) {
      projects.value = response.data.records || []
      totalProjects.value = response.data.total || 0
      totalPages.value = response.data.pages || 0
      current.value = response.data.current || 1
    }
  } catch (err) {
    error.value =
      err instanceof Error ? err.message : '获取项目列表失败，请稍后重试'
    console.error('获取项目列表失败:', err)
    projects.value = []
    totalProjects.value = 0
    totalPages.value = 0
  } finally {
    loading.value = false
  }
}

/**
 * 切换分页后重新拉取列表，保持分页与服务端结果同步。
 *
 * @param {number} page
 */
const handleCurrentChange = page => {
  current.value = page
  fetchProjects()
}

/**
 * 根据技术标签返回背景色类名，维持卡片标签的一致视觉分组。
 *
 * @param {string} tag
 * @returns {string}
 */
const getTagBg = tag => {
  const tagBgMap = {
    物联网: 'bg-blue-100',
    传感器: 'bg-green-100',
    嵌入式: 'bg-purple-100',
    机器人: 'bg-yellow-100',
    计算机视觉: 'bg-red-100',
    语音识别: 'bg-indigo-100',
    移动端: 'bg-pink-100',
    'React Native': 'bg-cyan-100',
    'Node.js': 'bg-orange-100',
    机器学习: 'bg-purple-100',
    Python: 'bg-green-100',
    TensorFlow: 'bg-blue-100',
    后端: 'bg-blue-100',
    Java: 'bg-yellow-100',
    开源: 'bg-gray-100',
    硬件: 'bg-green-100',
    Arduino: 'bg-red-100',
    教育: 'bg-orange-100',
    VR: 'bg-pink-100',
    Unity: 'bg-purple-100',
    Web前端: 'bg-blue-100',
    大数据: 'bg-green-100',
    可视化: 'bg-yellow-100',
  }
  return tagBgMap[tag] || 'bg-gray-100'
}

/**
 * 根据技术标签返回文字色类名，与背景色映射保持配套。
 *
 * @param {string} tag
 * @returns {string}
 */
const getTagText = tag => {
  const tagTextMap = {
    物联网: 'text-blue-800',
    传感器: 'text-green-800',
    嵌入式: 'text-purple-800',
    机器人: 'text-yellow-800',
    计算机视觉: 'text-red-800',
    语音识别: 'text-indigo-800',
    移动端: 'text-pink-800',
    'React Native': 'text-cyan-800',
    'Node.js': 'text-orange-800',
    机器学习: 'text-purple-800',
    Python: 'text-green-800',
    TensorFlow: 'text-blue-800',
    后端: 'text-blue-800',
    Java: 'text-yellow-800',
    开源: 'text-gray-800',
    硬件: 'text-green-800',
    Arduino: 'text-red-800',
    教育: 'text-orange-800',
    VR: 'text-pink-800',
    Unity: 'text-purple-800',
    Web前端: 'text-blue-800',
    大数据: 'text-green-800',
    可视化: 'text-yellow-800',
  }
  return tagTextMap[tag] || 'text-gray-800'
}

/**
 * 跳转到项目详情页，并通过 query 传递当前项目 ID。
 *
 * @param {number} projectId
 */
const goToDetail = projectId => {
  router.push({ path: '/projectdetail', query: { id: projectId } })
}

onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
  /* 1. 定义自定义颜色类（适配CDN） */
  .bg-primary {
    background-color: #3b82f6;
  }
  .text-primary {
    color: #3b82f6;
  }
  .bg-accent {
    background-color: #f59e0b;
  }
  .text-accent {
    color: #f59e0b;
  }

  /* 2. 动画样式 */
  .animate-fade-in {
    opacity: 0; /* 初始状态为不可见 */
    animation: fadeIn 0.8s ease-in-out forwards;
  }
  .animate-slide-up {
    opacity: 0;
    transform: translateY(20px);
    animation: slideUp 0.6s ease-out forwards;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
  @keyframes slideUp {
    from {
      transform: translateY(20px);
      opacity: 0;
    }
    to {
      transform: translateY(0);
      opacity: 1;
    }
  }

  /* 3. 响应式适配 */
  @media (max-width: 640px) {
    .grid {
      grid-template-columns: 1fr;
    }
  }

  /* 4. 分页组件样式 */
  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 32px;
  }

  /* 优化分页组件样式 - PC版 */
  :deep(.el-pagination) {
    /* 调整分页组件整体样式 */
    font-size: 14px;
  }

  :deep(.el-pagination__total) {
    color: #606266;
    margin-right: 16px;
  }

  :deep(.el-pagination__sizes) {
    display: none; /* 确保不显示每页数量选择 */
  }

  :deep(.el-pagination__prev),
  :deep(.el-pagination__next) {
    background-color: #fff;
    border: 1px solid #dcdfe6;
    color: #606266;
    width: 32px;
    height: 32px;
    line-height: 32px;
    margin: 0 2px;
  }

  :deep(.el-pagination__prev:hover),
  :deep(.el-pagination__next:hover) {
    color: #409eff;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }

  :deep(.el-pagination__item) {
    background-color: #fff;
    border: 1px solid #dcdfe6;
    color: #606266;
    width: 32px;
    height: 32px;
    line-height: 32px;
    margin: 0 2px;
  }

  :deep(.el-pagination__item:hover) {
    color: #409eff;
    border-color: #c6e2ff;
  }

  :deep(.el-pagination__item.is-current) {
    background-color: #409eff;
    border-color: #409eff;
    color: #fff;
  }

  :deep(.el-pagination__jump) {
    margin-left: 16px;
    color: #606266;
  }

  :deep(.el-pagination__editor) {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    width: 50px;
    text-align: center;
    margin: 0 5px;
  }

  /* 响应式设计 - 移动端适配 */
  @media screen and (max-width: 768px) {
    .grid {
      grid-template-columns: 1fr;
      gap: 16px;
    }

    :deep(.el-pagination) {
      font-size: 12px;
      padding: 0 8px;
    }

    :deep(.el-pagination__total) {
      margin-right: 8px;
      font-size: 12px;
    }

    :deep(.el-pagination__prev),
    :deep(.el-pagination__next),
    :deep(.el-pagination__item) {
      width: 28px;
      height: 28px;
      line-height: 28px;
      margin: 0 1px;
    }

    :deep(.el-pagination__jump) {
      margin-left: 8px;
    }

    :deep(.el-pagination__editor) {
      width: 40px;
      font-size: 12px;
    }

    /* 小屏幕上可能需要隐藏部分元素以保持美观 */
    @media screen and (max-width: 360px) {
      :deep(.el-pagination__total) {
        display: none;
      }

      :deep(.el-pagination__jump) {
        display: none;
      }
    }
  }
</style>
