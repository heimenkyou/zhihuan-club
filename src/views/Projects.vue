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

      <!-- 项目列表：循环「分页后的项目」（关键修改） -->
      <div
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
            @click="currentPage = totalPages"
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
          <img
            :src="project.image"
            :alt="project.name"
            class="w-full h-48 object-cover"
            loading="lazy"
          />
          <div class="p-6">
            <div class="flex justify-between items-start mb-4">
              <h3 class="text-xl font-bold text-gray-800">
                {{ project.name }}
              </h3>
              <span
                class="bg-accent/10 text-accent text-xs px-2 py-1 rounded-full font-medium"
              >
                {{ project.award }}
              </span>
            </div>
            <p class="text-gray-600 mb-6 line-clamp-3">
              {{ project.description }}
            </p>
            <div class="flex flex-wrap gap-2 mb-6">
              <span
                v-for="tag in project.tags"
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

      <!-- 动态分页（关键修改：关联真实项目数） -->
      <div class="mt-12 text-center" v-if="totalPages > 0">
        <nav class="inline-flex rounded-md shadow-sm" aria-label="Pagination">
          <!-- 上一页：当前页>1时可点击 -->
          <button
            @click="currentPage--"
            :disabled="currentPage === 1"
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
                (page >= currentPage - 1 && page <= currentPage + 1)
              "
              @click="currentPage = page"
              :class="[
                'relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium',
                currentPage === page
                  ? 'bg-primary text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-50',
              ]"
            >
              {{ page }}
            </button>

            <!-- 省略号：第1页和当前页-2之间，或最后1页和当前页+2之间 -->
            <span
              v-else-if="
                (page === currentPage - 2 && currentPage > 3) ||
                (page === currentPage + 2 && currentPage < totalPages - 2)
              "
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700"
            >
              ...
            </span>
          </template>

          <!-- 下一页：当前页<总页数时可点击 -->
          <button
            @click="currentPage++"
            :disabled="currentPage === totalPages"
            class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span class="sr-only">下一页</span>
            <i class="fa fa-chevron-right"></i>
          </button>
        </nav>

        <!-- 分页信息：显示当前页/总页数/总项目数 -->
        <p class="mt-4 text-sm text-gray-500">
          第 {{ currentPage }} 页 / 共 {{ totalPages }} 页（总计
          {{ filteredProjects.length }} 个项目）
        </p>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue"
import { useRouter } from "vue-router"
import CommonNavbar from "../components/CommonNavbar.vue"

// 类型定义
interface Project {
  id: number
  name: string
  image: string
  award: string
  description: string
  tags: string[]
  competitionType: "all" | "algorithm" | "innovation" | "hardware"
  techStack: "all" | "frontend" | "backend" | "mobile" | "ai"
}

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

// 响应式状态：筛选+分页（关键新增分页状态）
const selectedCompetition = ref("all")
const selectedTech = ref("all")
const router = useRouter()
const pageSize = ref(4) // 每页显示4个项目（可自定义）
const currentPage = ref(1) // 当前页码（默认第1页）

// 项目数据：补全8个项目（关键修改：之前只写了2个）
const projects = ref<Project[]>([
  {
    id: 1,
    name: "智能物联网系统",
    image: "https://images.unsplash.com/photo-1555066931-4365d14bab8c",
    award: "挑战杯校赛金奖",
    description:
      "基于物联网技术的智能环境监测与控制系统，可实时监测温度、湿度、空气质量等环境参数，并实现远程控制。",
    tags: ["物联网", "传感器", "嵌入式"],
    competitionType: "hardware",
    techStack: "backend",
  },
  {
    id: 2,
    name: "智能机器人平台",
    image: "https://images.unsplash.com/photo-1555066931-4365d14bab8c",
    award: "计算机设计大赛省奖",
    description:
      "集成语音识别、计算机视觉和运动控制的智能机器人平台，可实现人机交互、环境感知和自主导航。",
    tags: ["机器人", "计算机视觉", "语音识别"],
    competitionType: "hardware",
    techStack: "ai",
  },
  {
    id: 3,
    name: "校园助手App",
    image: "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04",
    award: "互联网+校赛银奖",
    description:
      "为大学生打造的一站式校园服务平台，集成课程表、成绩查询、校园活动、失物招领等功能。",
    tags: ["移动端", "React Native", "Node.js"],
    competitionType: "innovation",
    techStack: "mobile",
  },
  {
    id: 4,
    name: "智能推荐系统",
    image: "https://images.unsplash.com/photo-1542744094-3a31f272c490",
    award: "人工智能大赛校奖",
    description:
      "基于机器学习算法的个性化推荐系统，可根据用户行为和偏好进行智能推荐，提高用户体验和转化率。",
    tags: ["机器学习", "Python", "TensorFlow"],
    competitionType: "algorithm",
    techStack: "ai",
  },
  {
    id: 5,
    name: "轻量级Web框架",
    image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71",
    award: "开源项目",
    description:
      "自主开发的轻量级Web应用框架，具有高性能、易扩展、开发效率高等特点，适用于各类Web应用开发。",
    tags: ["后端", "Java", "开源"],
    competitionType: "innovation",
    techStack: "backend",
  },
  {
    id: 6,
    name: "智能硬件开发平台",
    image: "https://images.unsplash.com/photo-1498050108023-c5249f4df085",
    award: "硬件创新大赛省奖",
    description:
      "面向教育和创客群体的智能硬件开发平台，集成多种传感器和执行器，支持图形化编程和代码编程。",
    tags: ["硬件", "Arduino", "教育"],
    competitionType: "hardware",
    techStack: "backend",
  },
  {
    id: 7,
    name: "VR教育应用",
    image: "https://images.unsplash.com/photo-1586281380349-632531db7ed4",
    award: "创意设计大赛校奖",
    description:
      "基于虚拟现实技术的沉浸式教育应用，为用户提供身临其境的学习体验，提高学习效率和兴趣。",
    tags: ["VR", "Unity", "教育"],
    competitionType: "innovation",
    techStack: "frontend",
  },
  {
    id: 8,
    name: "数据可视化平台",
    image: "https://images.unsplash.com/photo-1591696205602-2f950c417cb9",
    award: "大数据大赛省奖",
    description:
      "面向企业和科研机构的数据可视化分析平台，支持多种数据格式和图表类型，提供交互式分析功能。",
    tags: ["Web前端", "大数据", "可视化"],
    competitionType: "algorithm",
    techStack: "frontend",
  },
])

// 1. 筛选逻辑：获取所有匹配的项目
const filteredProjects = computed<Project[]>(() => {
  return projects.value.filter((project) => {
    const matchCompetition =
      selectedCompetition.value === "all"
        ? true
        : project.competitionType === selectedCompetition.value
    const matchTech =
      selectedTech.value === "all"
        ? true
        : project.techStack === selectedTech.value
    return matchCompetition && matchTech
  })
})

// 2. 分页逻辑：截取当前页的项目（关键新增）
const paginatedProjects = computed<Project[]>(() => {
  // 移除未使用的 total 变量声明
  // 计算当前页的起始/结束索引（避免索引越界）
  const startIndex = Math.max(0, (currentPage.value - 1) * pageSize.value)
  const endIndex = startIndex + pageSize.value
  return filteredProjects.value.slice(startIndex, endIndex)
})

// 3. 计算总页数（关键新增）
const totalPages = computed(() => {
  return Math.max(1, Math.ceil(filteredProjects.value.length / pageSize.value))
})

// 筛选切换时重置页码为1（避免筛选后页码超出总页数）
const handleCompetitionChange = (value: string) => {
  selectedCompetition.value = value
  currentPage.value = 1
}
const handleTechChange = (value: string) => {
  selectedTech.value = value
  currentPage.value = 1
}

// 监听总页数变化：若当前页码>总页数，自动跳至最后一页
watch(totalPages, (newTotal) => {
  if (currentPage.value > newTotal) {
    currentPage.value = newTotal
  }
})

// 标签颜色映射（不变）
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

// 跳转详情页（不变）
const goToDetail = (projectId: number) => {
  router.push({ path: "/projectdetail", query: { id: projectId } })
}
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
