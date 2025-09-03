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
          探索我们团队开发的各类创新项目，从软件应用到硬件系统，展示我们的技术实力与创新精神
        </p>
      </div>

      <!-- 筛选区域 -->
      <div class="mb-8 bg-white rounded-xl p-4 shadow-sm">
        <h2 class="text-lg font-semibold mb-4">筛选条件</h2>
        <div class="flex flex-wrap gap-3 sm:gap-6">
          <!-- 竞赛类型筛选：筛选后重置页码为1 -->
          <div class="flex flex-wrap items-center gap-2">
            <span class="text-sm font-medium text-gray-700 whitespace-nowrap"
              >竞赛类型：</span
            >
            <button
              v-for="item in competitionTypes"
              :key="item.value"
              @click="handleCompetitionChange(item.value)"
              :class="[
                'px-3 py-1 rounded-full text-sm font-medium transition-colors',
                selectedCompetition === item.value
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
            >
              {{ item.label }}
            </button>
          </div>

          <!-- 技术栈筛选：筛选后重置页码为1 -->
          <div class="flex flex-wrap items-center gap-2 mt-3 sm:mt-0">
            <span class="text-sm font-medium text-gray-700 whitespace-nowrap"
              >技术栈：</span
            >
            <button
              v-for="item in techStacks"
              :key="item.value"
              @click="handleTechChange(item.value)"
              :class="[
                'px-3 py-1 rounded-full text-sm font-medium transition-colors',
                selectedTech === item.value
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
            >
              {{ item.label }}
            </button>
          </div>
        </div>
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

      <!-- 项目列表：循环「分页后的项目」 -->
      <div
        v-else
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6"
      >
        <!-- 无匹配项目提示 -->
        <div
          v-if="filteredProjects.length === 0"
          class="col-span-full text-center text-gray-500 py-12"
        >
          <p>暂无匹配的项目</p>
        </div>

        <!-- 分页后无项目（如筛选后页码超出总页数） -->
        <div
          v-else-if="paginatedProjects.length === 0"
          class="col-span-full text-center text-gray-500 py-12"
        >
          <p>当前页暂无项目</p>
          <button
            @click="current = totalPages"
            class="mt-2 text-primary hover:underline"
          >
            跳至最后一页
          </button>
        </div>

        <!-- 项目卡片：循环 paginatedProjects（当前页项目） -->
        <div
          v-for="(project, index) in paginatedProjects"
          :key="project.id"
          class="bg-white rounded-xl shadow-lg overflow-hidden transform hover:-translate-y-2 transition-all duration-300 animate-fade-in"
          :style="{ animationDelay: `${0.1 + index * 0.1}s` }"
        >
          <!-- 修复 coverImage 可能为 undefined 的问题 -->
          <img
            :src="
              project.coverImage ? project.coverImage.replace(/[`\s]/g, '') : ''
            "
            :alt="project.title"
            class="w-full h-48 object-cover"
            loading="lazy"
          />
          <div class="p-6">
            <div class="flex justify-between items-start mb-4">
              <h3 class="text-xl font-bold text-gray-800">
                {{ project.title }}
              </h3>
              <span
                class="bg-accent/10 text-accent text-xs px-2 py-1 rounded-full font-medium"
              >
                {{ project.category || "未分类" }}
              </span>
            </div>
            <p class="text-gray-600 mb-6 line-clamp-3">
              {{ project.briefIntro || "暂无项目简介" }}
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
            <button
              @click="goToDetail(project.id)"
              class="w-full bg-primary/10 text-primary hover:bg-primary hover:text-white transition-all px-4 py-2 rounded-lg font-medium"
            >
              查看详情
            </button>
          </div>
        </div>
      </div>

      <!-- 动态分页 -->
      <div
        class="mt-12 text-center"
        v-if="!loading && !error && totalPages > 0"
      >
        <nav class="inline-flex rounded-md shadow-sm" aria-label="Pagination">
          <!-- 上一页：当前页>1时可点击 -->
          <button
            @click="current--"
            :disabled="current === 1"
            class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span class="sr-only">上一页</span>
            <i class="fa fa-chevron-left"></i>
          </button>

          <!-- 动态生成页码：只显示首尾页+当前页前后2页（避免页码过多） -->
          <template v-for="page in totalPages" :key="page">
            <!-- 显示：第1页、最后1页、当前页±1 -->
            <button
              v-if="
                page === 1 ||
                page === totalPages ||
                (page >= current - 1 && page <= current + 1)
              "
              @click="current = page"
              :class="[
                'relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium',
                current === page
                  ? 'bg-primary text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-50',
              ]"
            >
              {{ page }}
            </button>

            <!-- 省略号：第1页和当前页-2之间，或最后1页和当前页+2之间 -->
            <span
              v-else-if="
                (page === current - 2 && current > 3) ||
                (page === current + 2 && current < totalPages - 2)
              "
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700"
            >
              ...
            </span>
          </template>

          <!-- 下一页：当前页<总页数时可点击 -->
          <button
            @click="current++"
            :disabled="current === totalPages"
            class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span class="sr-only">下一页</span>
            <i class="fa fa-chevron-right"></i>
          </button>
        </nav>

        <!-- 分页信息：显示当前页/总页数/总项目数 -->
        <p class="mt-4 text-sm text-gray-500">
          第 {{ current }} 页 / 共 {{ totalPages }} 页（总计
          {{ filteredProjects.length }} 个项目）
        </p>
      </div>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup lang="ts">
import CommonFooter from "../components/CommonFooter.vue"
import { ref, computed, watch, onMounted } from "vue"
import { useRouter } from "vue-router"
import CommonNavbar from "../components/CommonNavbar.vue"
import { getProjects } from "../services/projectService"
import type {
  Project,
  ProjectListParams,
  // ProjectListResponse,
} from "../services/projectService"

// 筛选选项
const competitionTypes = ref([
  { label: "全部", value: "all" },
  { label: "算法", value: "algorithm" },
  { label: "双创", value: "innovation" },
  { label: "硬件", value: "hardware" },
])

const techStacks = ref([
  { label: "全部", value: "all" },
  { label: "Web前端", value: "frontend" },
  { label: "后端开发", value: "backend" },
  { label: "移动端", value: "mobile" },
  { label: "人工智能", value: "ai" },
])

// 响应式状态：筛选+分页
const selectedCompetition = ref("all")
const selectedTech = ref("all")
const router = useRouter()
const pageSize = ref(4) // 每页显示4个项目
const current = ref(1) // 当前页码
const projects = ref<Project[]>([])
const totalProjects = ref(0)
const totalPages = ref(0)
const loading = ref(false)
const error = ref("")

// 1. 筛选逻辑：获取所有匹配的项目
const filteredProjects = computed<Project[]>(() => {
  return projects.value.filter((project) => {
    const matchCompetition = true // 暂时不按竞赛类型筛选，因为后端返回的数据中没有这个字段
    const matchTech =
      selectedTech.value === "all"
        ? true
        : (project.techStackTags || []).some((tag) =>
            tag.toLowerCase().includes(selectedTech.value.toLowerCase())
          )
    return matchCompetition && matchTech
  })
})

// 2. 分页逻辑：截取当前页的项目
const paginatedProjects = computed<Project[]>(() => {
  const startIndex = Math.max(0, (current.value - 1) * pageSize.value)
  const endIndex = startIndex + pageSize.value
  return filteredProjects.value.slice(startIndex, endIndex)
})

// 获取项目列表数据 - 完全适配最新数据格式
const fetchProjects = async () => {
  loading.value = true
  error.value = ""

  try {
    // 使用正确的参数格式
    const params: ProjectListParams = {
      current: current.value,
      size: pageSize.value,
    }

    // 添加筛选参数
    if (selectedCompetition.value !== "all") {
      params.competitionType = selectedCompetition.value
    }
    if (selectedTech.value !== "all") {
      params.techStack = selectedTech.value
    }

    // 修改函数调用并调整响应处理逻辑
    const response = await getProjects(params)

    // 增强型响应处理，适配不同格式的返回数据
    if (response) {
      // 情况1: 标准分页响应格式（带data.records）
      if (response.data && response.data.records) {
        projects.value = response.data.records || []
        totalProjects.value = response.data.total || 0
        totalPages.value = response.data.pages || 0
        current.value = response.data.current || 1
        // console.log(response)
      }
      //   // 情况2: 直接返回项目数组
      //   else if (Array.isArray(response)) {
      //     projects.value = response
      //     totalProjects.value = response.length
      //     totalPages.value = Math.ceil(response.length / pageSize.value)
      //   }
      //   // 情况3: 错误响应
      //   else if (response.code !== "0" && !response.success) {
      //     error.value = response.message || "获取项目列表失败"
      //     projects.value = []
      //     totalProjects.value = 0
      //     totalPages.value = 0
      //   }
      // } else {
      //   // 无数据情况
      //   projects.value = []
      //   totalProjects.value = 0
      //   totalPages.value = 0
      //   error.value = "未获取到项目数据"
    }
  } catch (err) {
    error.value =
      err instanceof Error ? err.message : "获取项目列表失败，请稍后重试"
    console.error("获取项目列表失败:", err)
    // 重置状态以确保UI正常显示
    projects.value = []
    totalProjects.value = 0
    totalPages.value = 0
  } finally {
    loading.value = false
  }
}

// 筛选切换时重置页码为1
const handleCompetitionChange = (value: string) => {
  selectedCompetition.value = value
  current.value = 1
  fetchProjects()
}

const handleTechChange = (value: string) => {
  selectedTech.value = value
  current.value = 1
  fetchProjects()
}

// 监听总页数变化：若当前页码>总页数，自动跳至最后一页
watch(totalPages, (newTotal) => {
  if (current.value > newTotal && newTotal > 0) {
    current.value = newTotal
  }
})

// 标签颜色映射
const getTagBg = (tag: string): string => {
  const tagBgMap: Record<string, string> = {
    物联网: "bg-blue-100",
    传感器: "bg-green-100",
    嵌入式: "bg-purple-100",
    机器人: "bg-yellow-100",
    计算机视觉: "bg-red-100",
    语音识别: "bg-indigo-100",
    移动端: "bg-pink-100",
    "React Native": "bg-cyan-100",
    "Node.js": "bg-orange-100",
    机器学习: "bg-purple-100",
    Python: "bg-green-100",
    TensorFlow: "bg-blue-100",
    后端: "bg-blue-100",
    Java: "bg-yellow-100",
    开源: "bg-gray-100",
    硬件: "bg-green-100",
    Arduino: "bg-red-100",
    教育: "bg-orange-100",
    VR: "bg-pink-100",
    Unity: "bg-purple-100",
    Web前端: "bg-blue-100",
    大数据: "bg-green-100",
    可视化: "bg-yellow-100",
  }
  return tagBgMap[tag] || "bg-gray-100"
}

const getTagText = (tag: string): string => {
  const tagTextMap: Record<string, string> = {
    物联网: "text-blue-800",
    传感器: "text-green-800",
    嵌入式: "text-purple-800",
    机器人: "text-yellow-800",
    计算机视觉: "text-red-800",
    语音识别: "text-indigo-800",
    移动端: "text-pink-800",
    "React Native": "text-cyan-800",
    "Node.js": "text-orange-800",
    机器学习: "text-purple-800",
    Python: "text-green-800",
    TensorFlow: "text-blue-800",
    后端: "text-blue-800",
    Java: "text-yellow-800",
    开源: "text-gray-800",
    硬件: "text-green-800",
    Arduino: "text-red-800",
    教育: "text-orange-800",
    VR: "text-pink-800",
    Unity: "text-purple-800",
    Web前端: "text-blue-800",
    大数据: "text-green-800",
    可视化: "text-yellow-800",
  }
  return tagTextMap[tag] || "text-gray-800"
}

// 跳转详情页 - 确保正确传递ID
const goToDetail = (projectId: number) => {
  router.push({ path: `/projectdetail`, query: { id: projectId } })
}

// 组件挂载时获取数据
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
  animation: fadeIn 0.8s ease-in-out;
}
.animate-slide-up {
  animation: slideUp 0.6s ease-out;
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
  .flex.flex-wrap.gap-3 > div {
    width: 100%;
  }
}

/* 4. Font Awesome图标样式 */
.fa {
  display: inline-block;
  font-style: normal;
  font-variant: normal;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
}
</style>
