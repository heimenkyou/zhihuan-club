<template>
  <div class="award-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>奖项管理</span>
          <el-button type="primary" @click="openAddDialog" class="add-btn">
            添加奖项
          </el-button>
        </div>
      </template>

      <!-- 搜索输入框 -->
      <div class="mb-6">
        <div class="relative">
          <input
            type="text"
            v-model="searchKeyword"
            placeholder="搜索奖项名称、获奖人员..."
            class="w-full px-4 py-2 pl-10 bg-white rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all"
            @input="handleSearch"
          />
          <i
            class="fa fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
          ></i>
        </div>
      </div>

      <!-- 多条件筛选栏 -->
      <div class="bg-white rounded-xl shadow-md p-6 mb-8">
        <h3 class="text-lg font-bold text-dark mb-4">奖项筛选</h3>

        <!-- 竞赛级别筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛级别</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = ''"
            >
              全部
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === '国家级'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = '国家级'"
            >
              国家级
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === '省级'
                  ? 'bg-blue-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = '省级'"
            >
              省级
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === '校级'
                  ? 'bg-blue-400 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = '校级'"
            >
              校级
            </button>
          </div>
        </div>

        <!-- 奖项级别筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >奖项级别</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = ''"
            >
              全部
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '一等奖'
                  ? 'bg-yellow-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '一等奖'"
            >
              一等奖(金牌)
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '二等奖'
                  ? 'bg-gray-400 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '二等奖'"
            >
              二等奖(银牌)
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '三等奖'
                  ? 'bg-orange-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '三等奖'"
            >
              三等奖(铜奖)
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '优秀奖'
                  ? 'bg-green-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '优秀奖'"
            >
              优秀奖
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '其他'
                  ? 'bg-purple-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '其他'"
            >
              其他
            </button>
          </div>
        </div>

        <!-- 竞赛年份筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛年份</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = ''"
            >
              全部
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2025'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2025'"
            >
              2025年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2024'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2024'"
            >
              2024年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2023'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2023'"
            >
              2023年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2022'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2022'"
            >
              2022年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2021'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2021'"
            >
              2021年
            </button>
          </div>
        </div>

        <!-- 竞赛项目筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛项目</label
          >
          <el-select
            v-model="filter.competitionName"
            placeholder="请选择竞赛项目"
            class="w-full"
            filterable
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="name in competitionNames"
              :key="name"
              :label="name"
              :value="name"
            />
          </el-select>
        </div>
        <!-- 新增：赛道筛选（当选择了竞赛项目且该项目有赛道时显示） -->
        <div
          class="mb-1"
          v-if="filter.competitionName && currentCompetitionTracks.length > 0"
        >
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >赛道</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionTrack === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionTrack = ''"
            >
              全部
            </button>
            <button
              v-for="track in currentCompetitionTracks"
              :key="track"
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionTrack === track
                  ? 'bg-teal-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionTrack = track"
            >
              {{ track }}
            </button>
          </div>
        </div>
        <!-- 重置按钮 -->
        <div class="mt-4">
          <el-button @click="resetFilter" class="reset-btn">重置筛选</el-button>
        </div>
      </div>

      <!-- 排序栏：两个下拉框（排序依据 + 升序/降序）+ 向左对齐 -->
      <div class="sort-container mb-6 flex items-center justify-start">
        <span class="text-sm text-gray-600 mr-4">排序方式：</span>
        <div class="flex items-center space-x-4">
          <!-- 第一个下拉框：排序依据 -->
          <el-select
            v-model="sortBy"
            placeholder="选择排序依据"
            size="small"
            @change="handleSortChange"
            style="width: 160px"
          >
            <el-option label="按竞赛级别" value="competitionLevel" />
            <el-option label="按获奖日期" value="awardDate" />
            <el-option label="按竞赛名称" value="competitionName" />
          </el-select>
          <!-- 第二个下拉框：升序/降序 -->
          <el-select
            v-model="sortOrder"
            placeholder="选择排序顺序"
            size="small"
            @change="handleSortChange"
            style="width: 120px"
          >
            <el-option label="降序" value="desc" />
            <el-option label="升序" value="asc" />
          </el-select>
        </div>
      </div>

      <!-- 奖项数据表格 -->
      <el-table
        :data="filteredAwardsData"
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="competitionName" label="奖项名称" width="200" />
        <el-table-column prop="competitionLevel" label="竞赛级别" width="120" />
        <!-- 新增赛道列 -->
        <el-table-column prop="competitionTrack" label="赛道" width="150">
          <template #default="scope">
            {{ scope.row.competitionTrack || "" }}
          </template>
        </el-table-column>
        <el-table-column prop="awardLevel" label="获奖等级" width="120" />
        <el-table-column prop="winners" label="获奖人员" width="120">
          <template #default="scope">
            {{ scope.row.winners?.join("、") || "" }}
          </template>
        </el-table-column>
        <el-table-column prop="year" label="获得年份" width="120" />
        <el-table-column prop="awardDate" label="获奖日期" width="100" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="openEditDialog(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteAward(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container" v-if="totalCount > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 添加/编辑对话框 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
        <el-form
          ref="awardFormRef"
          :model="awardForm"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item prop="competitionName">
            <el-input
              v-model="awardForm.competitionName"
              placeholder="请输入奖项名称"
            />
          </el-form-item>
          <el-form-item prop="competitionTrack">
            <el-input
              v-model="awardForm.competitionTrack"
              placeholder="请输入赛道名称(选填)"
            />
          </el-form-item>
          <el-form-item prop="competitionLevel">
            <el-select
              v-model="awardForm.competitionLevel"
              placeholder="请选择竞赛级别"
            >
              <el-option label="国家级" value="国家级" />
              <el-option label="省级" value="省级" />
              <el-option label="校级" value="校级" />
            </el-select>
          </el-form-item>
          <el-form-item prop="awardLevel">
            <el-select
              v-model="awardForm.awardLevel"
              placeholder="请选择或输入获奖等级"
              allow-create
              filterable
              default-first-option
            >
              <el-option label="一等奖" value="一等奖" />
              <el-option label="金牌" value="金牌" />
              <el-option label="二等奖" value="二等奖" />
              <el-option label="银牌" value="银牌" />
              <el-option label="三等奖" value="三等奖" />
              <el-option label="铜牌" value="铜牌" />
              <el-option label="优秀奖" value="优秀奖" />
            </el-select>
          </el-form-item>
          <el-form-item prop="winners">
            <el-input
              v-model="winnersText"
              placeholder="请输入获奖人员，用逗号分隔"
            />
          </el-form-item>
          <el-form-item prop="year">
            <el-input
              v-model.number="awardForm.year"
              placeholder="请输入获得年份"
              type="number"
            />
          </el-form-item>
          <el-form-item prop="awardDate">
            <el-date-picker
              v-model="awardForm.awardDate"
              type="month"
              placeholder="请选择获奖日期"
              format="YYYY-MM"
              value-format="YYYY-MM"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted, watch } from "vue"
import { ElMessage, ElMessageBox, ElForm } from "element-plus"
import {
  getAwards,
  createAward,
  updateAward,
  deleteAward as deleteAwardApi,
  type AwardItem,
} from "../../../services/adminService"

// 表单引用
const awardFormRef = ref<InstanceType<typeof ElForm> | null>(null)

// 搜索与筛选
const searchKeyword = ref("")
const filter = reactive({
  competitionLevel: "",
  awardLevel: "",
  year: "",
  competitionName: "",
  competitionTrack: "", // 新增赛道筛选字段
})

// 奖项数据与加载状态
const awardsData = ref<AwardItem[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref("添加奖项")
const currentAwardId = ref<number | null>(null)
const currentPage = ref(1)
const pageSize = ref(10)
const winnersText = ref("") // 获奖人员文本（逗号分隔）

// 新增：根据当前选中的竞赛项目动态获取赛道列表
const currentCompetitionTracks = computed(() => {
  if (!filter.competitionName) return []

  const tracks = new Set<string>()
  awardsData.value.forEach((award) => {
    if (
      award.competitionName === filter.competitionName &&
      award.competitionTrack &&
      award.competitionTrack !== ""
    ) {
      tracks.add(award.competitionTrack)
    }
  })
  return Array.from(tracks).sort()
})

// 排序配置：两个下拉框绑定值，默认「按竞赛级别+降序」
const sortBy = ref("competitionLevel") // 排序依据：competitionLevel/awardDate/competitionName
const sortOrder = ref("desc") // 排序顺序：desc(降序)/asc(升序)

// 奖项表单（初始化中文级别）
const awardForm = reactive<Partial<AwardItem>>({
  competitionName: "",
  competitionLevel: "国家级",
  competitionTrack: "",
  awardLevel: "一等奖",
  winners: [],
  year: new Date().getFullYear(),
  awardDate: new Date().toISOString().slice(0, 7), // YYYY-MM格式
})

// 表单验证规则
const rules = {
  competitionName: [
    { required: true, message: "请输入奖项名称", trigger: "blur" },
  ],
  competitionLevel: [
    { required: true, message: "请选择竞赛级别", trigger: "blur" },
  ],
  awardLevel: [{ required: true, message: "请选择获奖等级", trigger: "blur" }],
  winners: [{ required: true, message: "请输入获奖人员", trigger: "blur" }],
  year: [{ required: true, message: "请输入获得年份", trigger: "blur" }],
  awardDate: [{ required: true, message: "请选择获奖日期", trigger: "blur" }],
}

// 修正优先级定义（核心修改）
const competitionLevelPriority: Record<string, number> = {
  国家级: 1, // 级别越高，数值越小
  省级: 2,
  校级: 3,
}
const awardLevelPriority: Record<string, number> = {
  一等奖: 1, // 级别越高，数值越小
  金牌: 2, // 金牌紧随一等奖之后
  二等奖: 3,
  银牌: 4, // 银牌紧随二等奖之后
  三等奖: 5,
  铜牌: 6, // 铜牌紧随三等奖之后
  优秀奖: 7,
  其他: 8, // 级别越低，数值越大（方便升序排序）
}

// 排序依据/顺序变化时，重置页码
const handleSortChange = () => {
  currentPage.value = 1
}

// 筛选、排序、分页后的数据
const filteredAwardsData = computed(() => {
  // 1. 筛选逻辑
  const filtered = awardsData.value.filter((award) => {
    const matchesKeyword =
      !searchKeyword.value ||
      award.competitionName?.includes(searchKeyword.value) ||
      award.winners?.some((w) => w.includes(searchKeyword.value)) ||
      award.awardLevel?.includes(searchKeyword.value)

    const matchesLevel =
      !filter.competitionLevel ||
      award.competitionLevel === filter.competitionLevel

    // 定义奖项级别对应关系映射表
    const awardLevelMap: Record<string, string[]> = {
      一等奖: ["一等奖", "金牌"],
      二等奖: ["二等奖", "银牌"],
      三等奖: ["三等奖", "铜牌"],
    }

    const matchesAwardLevel =
      !filter.awardLevel ||
      // 检查是否在特殊映射关系中
      (filter.awardLevel in awardLevelMap &&
        awardLevelMap[filter.awardLevel].includes(award.awardLevel)) ||
      // 其他奖项级别精确匹配
      award.awardLevel === filter.awardLevel

    const matchesYear = !filter.year || award.year?.toString() === filter.year

    const matchesCompetitionName =
      !filter.competitionName ||
      award.competitionName?.includes(filter.competitionName)

    // 新增：赛道筛选条件
    const matchesTrack =
      !filter.competitionTrack ||
      award.competitionTrack === filter.competitionTrack

    return (
      matchesKeyword &&
      matchesLevel &&
      matchesAwardLevel &&
      matchesYear &&
      matchesCompetitionName &&
      matchesTrack
    )
  })

  // 2. 排序逻辑（分支内独立处理升/降序）
  filtered.sort((a, b) => {
    let compareResult = 0
    switch (sortBy.value) {
      // 分支1：按竞赛级别排序
      case "competitionLevel": {
        if (sortOrder.value === "desc") {
          // 降序：竞赛级别高（数值小）排前，同级别奖项高（数值小）排前
          const levelA = competitionLevelPriority[a.competitionLevel] || 0
          const levelB = competitionLevelPriority[b.competitionLevel] || 0
          compareResult = levelA - levelB

          if (compareResult === 0) {
            const awardA = awardLevelPriority[a.awardLevel || "其他"] || 0
            const awardB = awardLevelPriority[b.awardLevel || "其他"] || 0
            compareResult = awardA - awardB
          }
        } else {
          // 升序：竞赛级别低（数值大）排前，同级别奖项低（数值大）排前
          const levelA = competitionLevelPriority[a.competitionLevel] || 0
          const levelB = competitionLevelPriority[b.competitionLevel] || 0
          compareResult = levelB - levelA

          if (compareResult === 0) {
            const awardA = awardLevelPriority[a.awardLevel || "其他"] || 0
            const awardB = awardLevelPriority[b.awardLevel || "其他"] || 0
            compareResult = awardB - awardA
          }
        }
        break
      }

      // 分支2：按获奖日期排序
      case "awardDate": {
        const dateA = new Date(a.awardDate || "").getTime() || 0
        const dateB = new Date(b.awardDate || "").getTime() || 0
        compareResult =
          sortOrder.value === "desc"
            ? dateB - dateA // 降序：新日期→旧日期
            : dateA - dateB // 升序：旧日期→新日期
        break
      }

      // 分支3：按竞赛名称排序
      case "competitionName": {
        const nameA = a.competitionName || ""
        const nameB = b.competitionName || ""
        compareResult =
          sortOrder.value === "desc"
            ? nameB.localeCompare(nameA) // 降序：Z→A
            : nameA.localeCompare(nameB) // 升序：A→Z
        break
      }
    }
    return compareResult
  })

  // 3. 分页逻辑
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

// 总数据量
const totalCount = computed(() => {
  return awardsData.value.filter((award) => {
    const matchesKeyword =
      !searchKeyword.value ||
      award.competitionName?.includes(searchKeyword.value) ||
      award.winners?.some((w) => w.includes(searchKeyword.value)) ||
      award.awardLevel?.includes(searchKeyword.value)

    const matchesLevel =
      !filter.competitionLevel ||
      award.competitionLevel === filter.competitionLevel

    const matchesAwardLevel =
      !filter.awardLevel || award.awardLevel === filter.awardLevel

    const matchesYear = !filter.year || award.year?.toString() === filter.year

    const matchesCompetitionName =
      !filter.competitionName ||
      award.competitionName?.includes(filter.competitionName)

    // 新增：赛道筛选条件
    const matchesTrack =
      !filter.competitionTrack ||
      award.competitionTrack === filter.competitionTrack

    return (
      matchesKeyword &&
      matchesLevel &&
      matchesAwardLevel &&
      matchesYear &&
      matchesCompetitionName &&
      matchesTrack
    )
  }).length
})

// 竞赛名称列表（去重）
const competitionNames = computed(() => {
  const names = new Set<string>()
  awardsData.value.forEach((award) => {
    if (award.competitionName) names.add(award.competitionName)
  })
  return Array.from(names).sort()
})

// 加载奖项数据
const loadAwards = async () => {
  loading.value = true
  try {
    const data = await getAwards({ keyword: searchKeyword.value })
    awardsData.value = Array.isArray(data) ? data : []
    currentPage.value = 1
  } catch (error) {
    ElMessage.error("获取奖项信息失败")
    console.error("获取奖项信息失败:", error)
    awardsData.value = []
  } finally {
    loading.value = false
  }
}

// 搜索事件（重置页码）
const handleSearch = () => {
  currentPage.value = 1
}

// 重置筛选
const resetFilter = () => {
  searchKeyword.value = ""
  filter.competitionLevel = ""
  filter.awardLevel = ""
  filter.year = ""
  filter.competitionName = ""
  filter.competitionTrack = "" // 重置赛道筛选
  // 重置排序为默认值
  sortBy.value = "competitionLevel"
  sortOrder.value = "desc"
  currentPage.value = 1
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

// 页码变化
const handleCurrentChange = (current: number) => {
  currentPage.value = current
}

// 打开添加对话框
const openAddDialog = () => {
  dialogTitle.value = "添加奖项"
  currentAwardId.value = null
  // 重置表单
  awardForm.competitionName = ""
  awardForm.competitionLevel = "国家级"
  awardForm.awardLevel = "一等奖"
  awardForm.winners = []
  awardForm.year = new Date().getFullYear()
  const now = new Date()
  awardForm.awardDate = `${now.getFullYear()}-${String(
    now.getMonth() + 1
  ).padStart(2, "0")}`
  winnersText.value = ""
  dialogVisible.value = true
}

// 打开编辑对话框（完全不变）
const openEditDialog = (row: AwardItem) => {
  dialogTitle.value = "编辑奖项"
  currentAwardId.value = row.id
  // 填充表单数据
  Object.assign(awardForm, row)
  winnersText.value = row.winners?.join(",") || ""
  dialogVisible.value = true
}

// 提交表单（完全不变）
const submitForm = async () => {
  if (!awardFormRef.value) return

  try {
    // 转换获奖人员为数组
    awardForm.winners = winnersText.value
      .split(",")
      .map((name) => name.trim())
      .filter(Boolean)

    // 处理日期格式（确保为YYYY-MM）
    if (awardForm.awardDate && typeof awardForm.awardDate === "string") {
      awardForm.awardDate = awardForm.awardDate.slice(0, 7) // 保留YYYY-MM
    }

    await awardFormRef.value.validate()
    loading.value = true

    if (currentAwardId.value) {
      // 编辑奖项（传递中文级别）
      await updateAward(currentAwardId.value, {
        competitionName: awardForm.competitionName || "",
        competitionLevel: awardForm.competitionLevel || "", // 中文值
        competitionTrack: awardForm.competitionTrack || "",
        awardLevel: awardForm.awardLevel || "一等奖",
        winners: awardForm.winners || [],
        year: awardForm.year || new Date().getFullYear(),
        awardDate: awardForm.awardDate || new Date().toISOString().slice(0, 7),
      })
      ElMessage.success("更新奖项成功")
    } else {
      // 添加奖项（传递中文级别）
      await createAward(awardForm as Omit<AwardItem, "id">)
      ElMessage.success("添加奖项成功")
    }

    dialogVisible.value = false
    loadAwards()
  } catch (error) {
    ElMessage.error(currentAwardId.value ? "更新奖项失败" : "添加奖项失败")
    console.error("提交表单失败:", error)
  } finally {
    loading.value = false
  }
}

// 删除奖项
const deleteAward = async (id: number) => {
  try {
    await ElMessageBox.confirm("确定要删除这个奖项吗？", "确认删除", {
      type: "warning",
    })

    loading.value = true
    await deleteAwardApi(id)
    ElMessage.success("删除奖项成功")
    loadAwards()
  } catch (error) {
    ElMessage.error("删除奖项失败")
    console.error("删除奖项失败:", error)
  } finally {
    loading.value = false
  }
}

// 页面加载时初始化数据
onMounted(() => {
  loadAwards()
})
watch(
  () => filter.competitionName,
  () => {
    filter.competitionTrack = ""
  }
)
</script>

<style scoped>
.award-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.add-btn {
  margin-top: 0;
}
/* 排序盒子向左对齐，增加内边距 */
.sort-container {
  padding: 10px 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.reset-btn {
  background-color: #6c757d;
  color: white;
}
.reset-btn:hover {
  background-color: #5a6268;
  color: white;
}
:deep(.bg-primary) {
  background-color: #1989fa !important;
  color: white !important;
}
:deep(.bg-primary:hover) {
  background-color: #4096ff !important;
}
</style>
