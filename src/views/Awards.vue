<template>
  <div class="awards-page min-h-screen font-sans text-dark bg-gray-50">
    <CommonNavbar />

    <!-- 主要内容区域 -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 mt-16">
      <div class="max-w-6xl mx-auto">
        <!-- 页面标题和简介 -->
        <div class="text-center mb-12 animate-fade-in">
          <h1
            class="text-[clamp(1.5rem,4vw,3rem)] font-bold text-dark mb-4 leading-tight"
          >
            社团奖项展示
          </h1>
          <p class="text-gray-600 max-w-2xl mx-auto text-sm sm:text-base">
            回顾社团成员在各类科技竞赛中取得的荣誉与成就，见证我们的成长与进步
          </p>
        </div>

        <!-- 奖项分类导航 -->
        <div
          class="tab-container flex overflow-x-auto gap-2 pb-4 mb-10 scrollbar-hide px-2 sm:px-0"
        >
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-md hover:shadow-lg transition-all duration-300"
            :class="
              activeTab === 'all'
                ? 'bg-primary text-white'
                : 'bg-white text-dark shadow-sm'
            "
            @click="handleTabClick('all')"
          >
            <i class="fa fa-trophy"></i>
            <span>全部奖项</span>
          </button>
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-sm hover:shadow-md transition-all duration-300"
            :class="
              activeTab === 'national'
                ? 'bg-primary text-white'
                : 'bg-white text-dark'
            "
            @click="handleTabClick('national')"
          >
            <i class="fa fa-star text-yellow-500"></i>
            <span>国家级</span>
          </button>
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-sm hover:shadow-md transition-all duration-300"
            :class="
              activeTab === 'provincial'
                ? 'bg-primary text-white'
                : 'bg-white text-dark'
            "
            @click="handleTabClick('provincial')"
          >
            <i class="fa fa-map-marker text-red-500"></i>
            <span>省级</span>
          </button>
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-sm hover:shadow-md transition-all duration-300"
            :class="
              activeTab === 'school'
                ? 'bg-primary text-white'
                : 'bg-white text-dark'
            "
            @click="handleTabClick('school')"
          >
            <i class="fa fa-university text-indigo-700"></i>
            <span>校级</span>
          </button>
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-sm hover:shadow-md transition-all duration-300"
            :class="
              activeTab === '2023'
                ? 'bg-primary text-white'
                : 'bg-white text-dark'
            "
            @click="handleTabClick('2023')"
          >
            <i class="fa fa-calendar text-green-500"></i>
            <span>2023年</span>
          </button>
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-sm hover:shadow-md transition-all duration-300"
            :class="
              activeTab === '2022'
                ? 'bg-primary text-white'
                : 'bg-white text-dark'
            "
            @click="handleTabClick('2022')"
          >
            <i class="fa fa-calendar text-green-500"></i>
            <span>2022年</span>
          </button>
          <button
            class="tab-button px-5 py-2 rounded-full whitespace-nowrap font-medium flex items-center gap-2 shadow-sm hover:shadow-md transition-all duration-300"
            :class="
              activeTab === '2021'
                ? 'bg-primary text-white'
                : 'bg-white text-dark'
            "
            @click="handleTabClick('2021')"
          >
            <i class="fa fa-calendar text-green-500"></i>
            <span>2021年</span>
          </button>
        </div>

        <!-- 奖项统计卡片 -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
          <div class="bg-white rounded-xl shadow-lg p-6 animate-slide-up">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-xl font-bold text-dark">总奖项数</h3>
              <div
                class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center"
              >
                <i class="fa fa-trophy text-2xl text-primary"></i>
              </div>
            </div>
            <p class="text-4xl font-bold text-primary">{{ totalAwards }}</p>
            <p class="text-gray-500 text-sm mt-2">近三年累计获得</p>
          </div>
          <div
            class="bg-white rounded-xl shadow-lg p-6 animate-slide-up"
            style="animation-delay: 0.2s"
          >
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-xl font-bold text-dark">国家级奖项</h3>
              <div
                class="w-12 h-12 bg-yellow-100 rounded-full flex items-center justify-center"
              >
                <i class="fa fa-star text-2xl text-yellow-500"></i>
              </div>
            </div>
            <p class="text-4xl font-bold text-yellow-500">
              {{ nationalAwardsCount }}
            </p>
            <p class="text-gray-500 text-sm mt-2">包括一等、二等、三等奖</p>
          </div>
          <div
            class="bg-white rounded-xl shadow-lg p-6 animate-slide-up"
            style="animation-delay: 0.4s"
          >
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-xl font-bold text-dark">挑战杯奖项</h3>
              <div
                class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center"
              >
                <i class="fa fa-fire text-2xl text-red-500"></i>
              </div>
            </div>
            <p class="text-4xl font-bold text-red-500">
              {{ challengeCupAwardsCount }}
            </p>
            <p class="text-gray-500 text-sm mt-2">校级以上奖项</p>
          </div>
        </div>

        <!-- 加载状态 -->
        <div
          v-if="loading"
          class="flex justify-center items-center py-12 bg-white rounded-xl shadow-sm"
        >
          <div
            class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"
          ></div>
        </div>

        <!-- 错误提示 -->
        <div
          v-else-if="error"
          class="text-center py-12 bg-white rounded-xl shadow-sm"
        >
          <i class="fa fa-exclamation-circle text-red-500 text-4xl mb-4"></i>
          <p class="text-red-500 mb-2">{{ error }}</p>
          <button
            @click="fetchAwards()"
            class="px-4 py-2 bg-primary text-white rounded-full hover:bg-primary/90 transition-colors"
          >
            重试
          </button>
        </div>

        <!-- 奖项列表 -->
        <div v-else class="space-y-10">
          <!-- 按年份分组渲染奖项 -->
          <section
            v-for="(year, index) in sortedYears"
            :key="year"
            class="animate-slide-up"
            :style="{ animationDelay: `${0.1 * index}s` }"
          >
            <!-- 年份标题栏 -->
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-2xl font-bold text-dark">{{ year }}年度奖项</h2>
              <span
                class="bg-primary/10 text-primary px-3 py-1 rounded-full text-sm"
              >
                {{
                  groupedAwards[year].national.length +
                  groupedAwards[year].provincial.length +
                  groupedAwards[year].school.length
                }}个奖项
              </span>
            </div>

            <!-- 按奖项级别分组渲染 -->
            <div class="space-y-4">
              <!-- 国家级奖项组 -->
              <div
                v-if="groupedAwards[year].national.length"
                class="bg-white rounded-xl shadow-md overflow-hidden"
              >
                <div class="bg-blue-900 text-white p-4">
                  <h3 class="text-lg font-semibold flex items-center gap-2">
                    <i class="fa fa-star"></i> 国家级奖项
                  </h3>
                </div>
                <div
                  class="award-grid grid grid-cols-1 md:grid-cols-2 gap-4 p-4"
                >
                  <div
                    v-for="award in groupedAwards[year].national"
                    :key="award.id"
                    class="award-card cursor-pointer"
                    @click="goToProjectDetail"
                  >
                    <h4 class="font-semibold text-dark mb-1 line-clamp-2">
                      {{ award.title }}
                    </h4>
                    <p class="text-gray-500 text-sm mb-2">
                      作品：{{ award.workName }}
                    </p>
                    <p class="text-gray-400 text-xs">
                      <i class="fa fa-calendar-o mr-1"></i>{{ award.date }}
                    </p>
                  </div>
                </div>
              </div>

              <!-- 省级奖项组 -->
              <div
                v-if="groupedAwards[year].provincial.length"
                class="bg-white rounded-xl shadow-md overflow-hidden"
              >
                <div class="bg-blue-800 text-white p-4">
                  <h3 class="text-lg font-semibold flex items-center gap-2">
                    <i class="fa fa-map-marker"></i> 省级奖项
                  </h3>
                </div>
                <div
                  class="award-grid grid grid-cols-1 md:grid-cols-2 gap-4 p-4"
                >
                  <div
                    v-for="award in groupedAwards[year].provincial"
                    :key="award.id"
                    class="award-card cursor-pointer"
                    @click="goToProjectDetail"
                  >
                    <h4 class="font-semibold text-dark mb-1 line-clamp-2">
                      {{ award.title }}
                    </h4>
                    <p class="text-gray-500 text-sm mb-2">
                      作品：{{ award.workName }}
                    </p>
                    <p class="text-gray-400 text-xs">
                      <i class="fa fa-calendar-o mr-1"></i>{{ award.date }}
                    </p>
                  </div>
                </div>
              </div>

              <!-- 校级奖项组 -->
              <div
                v-if="groupedAwards[year].school.length"
                class="bg-white rounded-xl shadow-md overflow-hidden"
              >
                <div class="bg-blue-700 text-white p-4">
                  <h3 class="text-lg font-semibold flex items-center gap-2">
                    <i class="fa fa-university"></i> 校级奖项
                  </h3>
                </div>
                <div
                  class="award-grid grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 p-4"
                >
                  <div
                    v-for="award in groupedAwards[year].school"
                    :key="award.id"
                    class="award-card cursor-pointer"
                    @click="goToProjectDetail"
                  >
                    <h4 class="font-semibold text-dark mb-1 line-clamp-2">
                      {{ award.title }}
                    </h4>
                    <p class="text-gray-500 text-sm mb-2">
                      作品：{{ award.workName }}
                    </p>
                    <p class="text-gray-400 text-xs">
                      <i class="fa fa-calendar-o mr-1"></i>{{ award.date }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 空状态提示 -->
          <div
            v-if="sortedYears.length === 0"
            class="text-center py-12 bg-white rounded-xl shadow-sm"
          >
            <i class="fa fa-trophy text-gray-300 text-4xl mb-4"></i>
            <p class="text-gray-500">暂无该分类下的奖项数据</p>
          </div>

          <!-- 分页控件 -->
          <div class="flex justify-center mt-10">
            <nav class="flex items-center gap-1 sm:gap-2">
              <button
                class="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-full border border-gray-300 text-gray-500 hover:border-primary hover:text-primary transition-colors"
                :disabled="activePage === 1"
              >
                <i class="fa fa-angle-left text-xs sm:text-base"></i>
              </button>
              <button
                class="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-full text-sm"
                :class="
                  activePage === 1
                    ? 'bg-primary text-white'
                    : 'border border-gray-300 text-gray-700 hover:border-primary hover:text-primary transition-colors'
                "
                @click="handlePageChange(1)"
              >
                1
              </button>
              <button
                class="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-full text-sm"
                :class="
                  activePage === 2
                    ? 'bg-primary text-white'
                    : 'border border-gray-300 text-gray-700 hover:border-primary hover:text-primary transition-colors'
                "
                @click="handlePageChange(2)"
              >
                2
              </button>
              <button
                class="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-full text-sm"
                :class="
                  activePage === 3
                    ? 'bg-primary text-white'
                    : 'border border-gray-300 text-gray-700 hover:border-primary hover:text-primary transition-colors'
                "
                @click="handlePageChange(3)"
              >
                3
              </button>
              <button
                class="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-full border border-gray-300 text-gray-500 hover:border-primary hover:text-primary transition-colors"
                :disabled="activePage === 3"
              >
                <i class="fa fa-angle-right text-xs sm:text-base"></i>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from "vue"
import CommonNavbar from "../components/CommonNavbar.vue"
import { useRouter } from "vue-router"
// 移除未使用的 api 导入

// 定义奖项数据结构
interface Award {
  id: number
  title: string // 奖项名称
  workName: string // 作品名称
  level: "national" | "provincial" | "school" // 奖项级别
  year: "2023" | "2022" | "2021" // 年份
  date: string // 具体日期
}

// 响应式状态管理
const router = useRouter()
const activeTab = ref("all") // 当前选中分类
const activePage = ref(1) // 当前页码
const awards = ref<Award[]>([]) // 奖项数据
const loading = ref(true) // 加载状态
const error = ref<string | null>(null) // 错误信息

// 首先需要导入adminService中的getAwards函数
import { getAwards } from "../services/adminService"

// 从API获取奖项数据
const fetchAwards = async () => {
  loading.value = true
  error.value = null
  try {
    // 参考AwardManagement.vue中的请求方式
    // 设置参数，这里我们只获取所有奖项，不做分页和搜索
    const params = {
      current: 1,
      size: 1000, // 设置一个较大的值以获取所有奖项
      keyword: "",
    }

    // 使用adminService中的getAwards函数
    const data = await getAwards(params)

    // 确保数据格式正确
    if (data && typeof data === "object" && Array.isArray(data.records)) {
      // 从返回的分页数据中提取records数组
      awards.value = data.records
    } else {
      // 如果响应不是预期格式，使用空数组
      awards.value = []
      console.warn("API响应格式不符合预期，使用空数组")
    }
  } catch (err) {
    error.value = "获取奖项数据失败，请检查API服务是否运行"
    console.error("Failed to fetch awards:", err)
    // 发生错误时，确保awards.value是数组
    awards.value = []
  } finally {
    loading.value = false
  }
}

// 分类筛选逻辑
const filteredAwards = computed(() => {
  return awards.value.filter((award) => {
    if (activeTab.value === "all") return true
    if (["national", "provincial", "school"].includes(activeTab.value)) {
      return award.level === activeTab.value
    }
    if (["2023", "2022", "2021"].includes(activeTab.value)) {
      return award.year === activeTab.value
    }
    return true
  })
})

// 数据分组：按年份→级别分组
const groupedAwards = computed(() => {
  return filteredAwards.value.reduce((acc, award) => {
    if (!acc[award.year]) {
      acc[award.year] = {
        national: [] as Award[],
        provincial: [] as Award[],
        school: [] as Award[],
      }
    }
    // 检查level是否为有效类型
    const validLevels = ['national', 'provincial', 'school'];
    const level = validLevels.includes(award.level) ? award.level : 'school'; // 默认使用school
    acc[award.year][level].push(award);
    return acc;
  }, {} as Record<string, { national: Award[]; provincial: Award[]; school: Award[] }>)
})

// 年份排序：按降序排列
const sortedYears = computed(() => {
  return Object.keys(groupedAwards.value).sort(
    (a, b) => parseInt(b) - parseInt(a)
  )
})

// 统计信息
const totalAwards = computed(() => awards.value.length)
const nationalAwardsCount = computed(
  () => awards.value.filter((award) => award.level === "national").length
)
const challengeCupAwardsCount = computed(
  () => awards.value.filter((award) => award.title.includes("挑战杯")).length
)

// 事件处理
const handleTabClick = (tab: string) => {
  activeTab.value = tab
  activePage.value = 1
  window.scrollTo({ top: 300, behavior: "smooth" })
}

const handlePageChange = (page: number) => {
  activePage.value = page
  window.scrollTo({ top: 300, behavior: "smooth" })
}

const goToProjectDetail = () => {
  router.push("/projectdetailtest")
}

// 页面加载时获取数据
onMounted(() => {
  fetchAwards()

  nextTick(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("animate-slide-up")
            observer.unobserve(entry.target)
          }
        })
      },
      { root: null, rootMargin: "0px", threshold: 0.1 }
    )

    document
      .querySelectorAll("section")
      .forEach((section) => observer.observe(section))
  })
})
</script>

<style scoped>
.awards-page {
  background-color: #f8fafc;
}

/* 自定义颜色 */
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
.text-dark {
  color: #1e293b;
}

/* 动画样式 */
.animate-fade-in {
  animation: fadeIn 0.8s ease-in-out;
}
.animate-slide-up {
  animation: slideUp 0.6s ease-out;
  opacity: 0;
  animation-fill-mode: forwards;
}
.animate-scale-in {
  animation: scaleIn 0.5s ease-out;
  opacity: 0;
  animation-fill-mode: forwards;
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
@keyframes scaleIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

/* 分类标签移动端适配 */
.tab-container {
  scrollbar-width: none;
  -ms-overflow-style: none;
}
.tab-container::-webkit-scrollbar {
  display: none;
}

/* 标签按钮响应式调整 */
@media (max-width: 640px) {
  .tab-button {
    padding: 0.375rem 0.75rem !important;
    font-size: 0.8125rem !important;
    gap: 0.5rem !important;
  }
  .tab-button .fa {
    display: none !important;
  }
}

/* 奖项卡片适配 */
@media (max-width: 640px) {
  .award-grid {
    grid-template-columns: 1fr !important;
  }
  .award-card {
    padding: 0.75rem !important;
  }
  .award-card h4 {
    font-size: 0.875rem !important;
    line-height: 1.4 !important;
  }
  .award-card p {
    font-size: 0.75rem !important;
  }
}

/* 奖项卡片基础样式 */
.award-card {
  border: 1px solid #f1f5f9;
  border-radius: 0.5rem;
  padding: 1rem;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}
.award-card:hover {
  background-color: #f8fafc;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 其他样式补充 */
button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
