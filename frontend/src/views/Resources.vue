<template>
  <div class="resources-page min-h-screen bg-gray-50 font-sans">
    <!-- 通用导航栏 -->
    <CommonNavbar />

    <!-- 主内容区（适配导航栏高度） -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 mt-16">
      <!-- 页面标题 -->
      <div class="text-center mb-8 animate-slide-up">
        <h1 class="text-3xl font-bold text-gray-900 mb-4">资源墙</h1>
        <p class="text-gray-600">在这里分享和发现优质学习资源</p>
      </div>

      <!-- 筛选区 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-8 animate-slide-up">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"
              >资源分类</label
            >
            <select
              v-model="selectedCategory"
              @change="filterResources"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">全部分类</option>
              <option value="algorithm">算法竞赛</option>
              <option value="project">项目开发</option>
              <option value="design">设计创作</option>
              <option value="tool">工具软件</option>
              <option value="book">书籍资料</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"
              >资源类型</label
            >
            <select
              v-model="selectedType"
              @change="filterResources"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">全部类型</option>
              <option value="book">书籍</option>
              <option value="website">网站</option>
              <option value="video">视频</option>
              <option value="tool">工具</option>
              <option value="code">代码</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"
              >搜索资源</label
            >
            <input
              type="text"
              v-model="searchKeyword"
              @input="filterResources"
              placeholder="搜索资源名称..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>
      </div>

      <!-- 资源卡片网格 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-if="filteredResources.length === 0"
          class="col-span-full text-center text-gray-500 py-8"
        >
          暂无匹配的资源
        </div>

        <!-- 资源卡片 -->
        <div
          v-for="resource in filteredResources"
          :key="resource.id"
          class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow cursor-pointer hover-lift"
          @click="showResourceDetail(resource)"
        >
          <img
            :src="resource.image"
            :alt="resource.name"
            class="w-full h-48 object-cover rounded-t-lg"
          />
          <div class="p-4">
            <h3 class="font-semibold text-lg mb-2 truncate">
              {{ resource.name }}
            </h3>
            <p class="text-gray-600 text-sm line-clamp-2">
              {{ resource.description }}
            </p>
            <div class="mt-3 flex gap-2 flex-wrap">
              <span
                v-for="type in resource.types"
                :key="type"
                class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded"
              >
                {{ getTypeName(type) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 资源详情模态框 -->
    <div
      class="resource-modal"
      :class="{ active: isModalOpen }"
      @click.self="closeModal"
    >
      <div
        class="bg-white rounded-lg p-6 max-w-2xl mx-4 max-h-[90vh] overflow-y-auto"
      >
        <div class="flex justify-between items-start mb-4">
          <h3 class="text-xl font-bold" v-if="currentResource">
            {{ currentResource.name }}
          </h3>
          <button
            @click="closeModal"
            class="w-10 h-10 flex items-center justify-center rounded-full border border-gray-200 bg-white text-gray-600 hover:bg-gray-100 hover:text-red-500 transition-all duration-200 shadow-sm hover:shadow-md"
            aria-label="关闭详情"
          >
            <el-icon><close /></el-icon>
          </button>
        </div>

        <div v-if="currentResource" class="space-y-4">
          <img
            :src="currentResource.image"
            :alt="currentResource.name"
            class="w-full h-64 object-cover rounded-lg"
          />
          <div>
            <h4 class="font-semibold text-gray-900 mb-2">资源信息</h4>
            <div class="grid grid-cols-2 gap-4 text-sm">
              <div>
                <span class="text-gray-600">分类：</span>
                <span
                  class="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs"
                >
                  {{ getCategoryName(currentResource.category) }}
                </span>
              </div>
              <div>
                <span class="text-gray-600">类型：</span>
                <span
                  v-for="type in currentResource.types"
                  :key="type"
                  class="bg-green-100 text-green-800 px-2 py-1 rounded text-xs mr-1"
                >
                  {{ getTypeName(type) }}
                </span>
              </div>
              <div>
                <span class="text-gray-600">作者：</span>
                <span>{{ currentResource.author }}</span>
              </div>
            </div>
          </div>
          <div>
            <h4 class="font-semibold text-gray-900 mb-2">资源介绍</h4>
            <p class="text-gray-700">{{ currentResource.description }}</p>
          </div>
          <div v-if="currentResource.link">
            <a
              :href="currentResource.link"
              target="_blank"
              rel="noopener noreferrer"
              class="inline-flex items-center px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
            >
              <el-icon size="16" class="mr-2"><Link /></el-icon>
              访问资源
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue"
import CommonNavbar from "@/components/CommonNavbar.vue"

// 响应式状态
const resources = ref([
  {
    id: 1,
    name: "代码随想录",
    category: "algorithm",
    types: ["book", "website"],
    description:
      "优秀的算法学习书籍，配套网站提供丰富题解和学习路线，适合竞赛和面试准备。",
    image: "https://placehold.co/300x200/4F46E5/FFFFFF?text=CodeRecord",
    link: "https://programmercarl.com",
    author: "Carl",
  },
  {
    id: 2,
    name: "LeetCode",
    category: "algorithm",
    types: ["website"],
    description:
      "全球知名算法刷题平台，提供海量题目和在线评测，适合算法学习和面试准备。",
    image: "https://placehold.co/300x200/10B981/FFFFFF?text=LeetCode",
    link: "https://leetcode.cn",
    author: "LeetCode",
  },
  {
    id: 3,
    name: "Python项目实战",
    category: "project",
    types: ["video", "code"],
    description: "通过实战项目学习Python，涵盖Web开发、数据分析、爬虫等方向。",
    image: "https://placehold.co/300x200/F59E0B/FFFFFF?text=PythonProj",
    link: "",
    author: "张老师",
  },
  {
    id: 4,
    name: "算法竞赛入门经典",
    category: "algorithm",
    types: ["book"],
    description: "经典算法竞赛入门书籍，系统讲解常用数据结构和算法。",
    image: "https://placehold.co/300x200/EF4444/FFFFFF?text=AlgIntro",
    link: "",
    author: "刘汝佳",
  },
  {
    id: 5,
    name: "GitHub",
    category: "tool",
    types: ["website", "tool"],
    description: "全球最大代码托管平台，学习版本控制和开源项目协作的必备工具。",
    image: "https://placehold.co/300x200/1F2937/FFFFFF?text=GitHub",
    link: "https://github.com",
    author: "GitHub",
  },
  {
    id: 6,
    name: "VS Code",
    category: "tool",
    types: ["tool"],
    description: "微软免费代码编辑器，支持多语言和丰富插件，适合开发使用。",
    image: "https://placehold.co/300x200/007ACC/FFFFFF?text=VSCode",
    link: "https://code.visualstudio.com",
    author: "Microsoft",
  },
])

const selectedCategory = ref("")
const selectedType = ref("")
const searchKeyword = ref("")
const isModalOpen = ref(false)
const currentResource = ref(null)

// 辅助函数
const getTypeName = type => {
  const typeMap = {
    book: "书籍",
    website: "网站",
    video: "视频",
    tool: "工具",
    code: "代码",
  }
  return typeMap[type] || type
}

const getCategoryName = category => {
  const categoryMap = {
    algorithm: "算法竞赛",
    project: "项目开发",
    design: "设计创作",
    tool: "工具软件",
    book: "书籍资料",
  }
  return categoryMap[category] || category
}

// 筛选资源：关键修复【资源类型匹配逻辑】
const filteredResources = computed(() => {
  return resources.value.filter((resource) => {
    const matchCategory =
      !selectedCategory.value || resource.category === selectedCategory.value
    // 修复点：检查【资源的types数组】是否包含【选中的类型】（原逻辑写反了）
    const matchType =
      !selectedType.value ||
      resource.types.includes(selectedType.value)
    const matchSearch = !searchKeyword.value
      ? true
      : resource.name
          .toLowerCase()
          .includes(searchKeyword.value.toLowerCase()) ||
        resource.description
          .toLowerCase()
          .includes(searchKeyword.value.toLowerCase())
    return matchCategory && matchType && matchSearch
  })
})

// 模态框控制
const showResourceDetail = resource => {
  currentResource.value = resource
  isModalOpen.value = true
  document.body.style.overflow = "hidden"
}

const closeModal = () => {
  isModalOpen.value = false
  currentResource.value = null
  document.body.style.overflow = "auto"
}

// 筛选触发（计算属性自动响应，无需额外函数）
const filterResources = () => {}
</script>

<style scoped>
.resources-page {
  padding-top: 0 !important;
}

@media (max-width: 768px) {
  main {
    margin-top: 94px !important; /* 适配移动端导航高度 */
  }
}

.animate-slide-up {
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hover-lift:hover {
  transform: translateY(-2px);
  transition: transform 0.2s ease;
}

.resource-modal {
  display: none;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.resource-modal.active {
  display: flex;
  align-items: center;
  justify-content: center;
}

.fas {
  display: inline-block;
  font-style: normal;
  font-variant: normal;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
}
</style>
