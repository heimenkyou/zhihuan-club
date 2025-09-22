<template>
  <div class="award-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>奖项管理</span>
          <el-button 
            type="primary" 
            @click="openAddDialog" 
            class="add-btn"
            :size="isMobile ? 'small' : 'default'"
          >
            {{ isMobile ? '添加' : '添加奖项' }}
          </el-button>
        </div>
      </template>

      <!-- 搜索输入框 - 移动端适配 -->
      <div class="mb-4">
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

      <!-- 多条件筛选栏 - 移动端适配 -->
      <div class="bg-white rounded-xl shadow-md p-4 mb-4">
        <h3 class="text-lg font-bold text-dark mb-3">奖项筛选</h3>

        <!-- 竞赛级别筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛级别</label
          >
          <div class="flex flex-wrap gap-1">
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
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
                'px-3 py-1 rounded-full text-xs transition-all',
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
                'px-3 py-1 rounded-full text-xs transition-all',
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
                'px-3 py-1 rounded-full text-xs transition-all',
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
          <div class="flex flex-wrap gap-1">
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
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
                'px-3 py-1 rounded-full text-xs transition-all',
                filter.awardLevel === '一等奖'
                  ? 'bg-yellow-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '一等奖'"
            >
              一等奖
            </button>
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
                filter.awardLevel === '二等奖'
                  ? 'bg-gray-400 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '二等奖'"
            >
              二等奖
            </button>
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
                filter.awardLevel === '三等奖'
                  ? 'bg-orange-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '三等奖'"
            >
              三等奖
            </button>
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
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
                'px-3 py-1 rounded-full text-xs transition-all',
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
          <div class="flex flex-wrap gap-1">
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
                filter.year === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = ''"
            >
              全部
            </button>
            <button
              v-for="year in availableYears"
              :key="year"
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
                filter.year === year.toString()
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = year.toString()"
            >
              {{ year }}年
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
            :size="isMobile ? 'small' : 'default'"
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
        
        <!-- 赛道筛选 -->
        <div
          class="mb-3"
          v-if="filter.competitionName && currentCompetitionTracks.length > 0"
        >
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >赛道</label
          >
          <div class="flex flex-wrap gap-1">
            <button
              :class="[
                'px-3 py-1 rounded-full text-xs transition-all',
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
                'px-3 py-1 rounded-full text-xs transition-all',
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
        <div class="mt-3">
          <el-button 
            @click="resetFilter" 
            class="reset-btn"
            :size="isMobile ? 'small' : 'default'"
            style="width: 100%"
          >
            重置筛选
          </el-button>
        </div>
      </div>

      <!-- 排序栏 - 移动端适配 -->
      <div class="sort-container mb-4 flex flex-col sm:flex-row items-start sm:items-center justify-start gap-2">
        <span class="text-sm text-gray-600">排序方式：</span>
        <div class="flex flex-col sm:flex-row items-start sm:items-center space-y-2 sm:space-y-0 sm:space-x-2 w-full sm:w-auto">
          <el-select
            v-model="sortBy"
            placeholder="排序依据"
            :size="isMobile ? 'small' : 'default'"
            @change="handleSortChange"
            style="width: 100%"
          >
            <el-option label="竞赛级别" value="competitionLevel" />
            <el-option label="获奖日期" value="awardDate" />
            <el-option label="竞赛名称" value="competitionName" />
          </el-select>
          <el-select
            v-model="sortOrder"
            placeholder="排序顺序"
            :size="isMobile ? 'small' : 'default'"
            @change="handleSortChange"
            style="width: 100%"
          >
            <el-option label="降序" value="desc" />
            <el-option label="升序" value="asc" />
          </el-select>
        </div>
      </div>

      <!-- 奖项数据表格 - 移动端适配 -->
      <div class="table-container">
        <el-table
          :data="filteredAwardsData"
          style="width: 100%"
          v-loading="loading"
          :class="{ 'mobile-table': isMobile }"
        >
          <el-table-column prop="id" label="ID" :width="isMobile ? 50 : 80" />
          <el-table-column prop="competitionName" label="奖项名称" :width="isMobile ? 120 : 200" />
          <el-table-column prop="competitionLevel" label="级别" :width="isMobile ? 80 : 120" />
          <el-table-column prop="competitionTrack" label="赛道" :width="isMobile ? 100 : 150">
            <template #default="scope">
              {{ scope.row.competitionTrack || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="awardLevel" label="等级" :width="isMobile ? 80 : 120" />
          <el-table-column prop="winners" label="获奖人员" :width="isMobile ? 100 : 120">
            <template #default="scope">
              {{ scope.row.winners?.join('') || '' }}
            </template>
          </el-table-column>
          <el-table-column prop="year" label="年份" :width="isMobile ? 70 : 120" />
          <el-table-column label="操作" :width="isMobile ? 120 : 180" fixed="right">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                  type="primary"
                  :size="isMobile ? 'small' : 'default'"
                  @click="openEditDialog(scope.row)"
                  style="margin-bottom: 5px; width: 100%"
                  v-if="isMobile"
                >
                  编辑
                </el-button>
                <el-button
                  type="primary"
                  :size="isMobile ? 'small' : 'default'"
                  @click="openEditDialog(scope.row)"
                  v-else
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  :size="isMobile ? 'small' : 'default'"
                  @click="deleteAward(scope.row.id)"
                  style="width: 100%"
                  v-if="isMobile"
                >
                  删除
                </el-button>
                <el-button
                  type="danger"
                  :size="isMobile ? 'small' : 'default'"
                  @click="deleteAward(scope.row.id)"
                  v-else
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 - 移动端适配 -->
      <div class="pagination-container" v-if="totalCount > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="isMobile ? [5, 10, 20] : [10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :small="isMobile"
        />
      </div>

      <!-- 添加/编辑对话框 - 移动端适配 -->
      <el-dialog 
        v-model="dialogVisible" 
        :title="dialogTitle" 
        :width="isMobile ? '95%' : '600px'"
        :fullscreen="isMobile"
      >
        <el-form
          ref="awardFormRef"
          :model="awardForm"
          :rules="rules"
          label-width="100px"
          :label-position="isMobile ? 'top' : 'right'"
        >
          <el-form-item label="奖项名称" prop="competitionName">
            <el-input
              v-model="awardForm.competitionName"
              placeholder="请输入奖项名称"
            />
          </el-form-item>
          <el-form-item label="赛道" prop="competitionTrack">
            <el-input
              v-model="awardForm.competitionTrack"
              placeholder="请输入赛道名称(选填)"
            />
          </el-form-item>
          <el-form-item label="竞赛级别" prop="competitionLevel">
            <el-select
              v-model="awardForm.competitionLevel"
              placeholder="请选择竞赛级别"
              style="width: 100%"
            >
              <el-option label="国家级" value="国家级" />
              <el-option label="省级" value="省级" />
              <el-option label="校级" value="校级" />
            </el-select>
          </el-form-item>
          <el-form-item label="获奖等级" prop="awardLevel">
            <el-select
              v-model="awardForm.awardLevel"
              placeholder="请选择或输入获奖等级"
              allow-create
              filterable
              default-first-option
              style="width: 100%"
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
          <el-form-item label="获奖人员" prop="winners">
            <div class="winners-tags-container">
              <el-tag
                v-for="(winner, index) in winnersList"
                :key="index"
                closable
                :disable-transitions="false"
                @close="removeWinner(index)"
                :size="isMobile ? 'small' : 'default'"
              >
                {{ winner }}
              </el-tag>
              <el-input
                v-model="newWinnerName"
                placeholder="输入获奖人员姓名，按回车添加"
                class="winners-input"
                @keyup.enter.native="addWinner"
                :validate-event="false"
                :size="isMobile ? 'small' : 'default'"
              />
            </div>
          </el-form-item>
          <el-form-item label="获得年份" prop="year">
            <el-input
              v-model.number="awardForm.year"
              placeholder="年份将根据日期自动填充"
              type="number"
              :readonly="true"
            />
          </el-form-item>
          <el-form-item label="获奖日期" prop="awardDate">
            <el-date-picker
              v-model="awardForm.awardDate"
              type="month"
              placeholder="请选择获奖日期"
              format="YYYY-MM"
              value-format="YYYY-MM"
              style="width: 100%"
              :size="isMobile ? 'small' : 'default'"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button 
              @click="dialogVisible = false"
              :size="isMobile ? 'large' : 'default'"
              style="width: 100%"
              v-if="isMobile"
            >
              取消
            </el-button>
            <el-button @click="dialogVisible = false" v-else>
              取消
            </el-button>
            <el-button 
              type="primary" 
              @click="submitForm"
              :size="isMobile ? 'large' : 'default'"
              style="width: 100%; margin-top: 10px"
              v-if="isMobile"
            >
              确定
            </el-button>
            <el-button type="primary" @click="submitForm" v-else>
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue'
  import { ElMessage, ElMessageBox, ElForm } from 'element-plus'
  import {
    getAwards,
    createAward,
    updateAward,
    deleteAward as deleteAwardApi,
    type AwardItem,
  } from '../../../services/adminService'

  // 检测是否为移动端
  const isMobile = computed(() => {
    return window.innerWidth <= 768
  })

  // 表单引用
  const awardFormRef = ref<InstanceType<typeof ElForm> | null>(null)

  // 搜索与筛选
  const searchKeyword = ref('')
  const filter = reactive({
    competitionLevel: '',
    awardLevel: '',
    year: '',
    competitionName: '',
    competitionTrack: '',
  })

  // 奖项数据与加载状态
  const awardsData = ref<AwardItem[]>([])
  const loading = ref(false)
  const dialogVisible = ref(false)
  const dialogTitle = ref('添加奖项')
  const currentAwardId = ref<number | null>(null)
  const currentPage = ref(1)
  const pageSize = ref(isMobile.value ? 5 : 10) // 移动端默认显示更少数据
  const winnersList = ref<string[]>([])
  const newWinnerName = ref('')

  // 可用年份列表
  const availableYears = computed(() => {
    const currentYear = new Date().getFullYear()
    return [currentYear, currentYear - 1, currentYear - 2, currentYear - 3, currentYear - 4]
  })

  // 当前竞赛项目的赛道列表
  const currentCompetitionTracks = computed(() => {
    if (!filter.competitionName) return []

    const tracks = new Set<string>()
    awardsData.value.forEach(award => {
      if (
        award.competitionName === filter.competitionName &&
        award.competitionTrack &&
        award.competitionTrack !== ''
      ) {
        tracks.add(award.competitionTrack)
      }
    })
    return Array.from(tracks).sort()
  })

  // 排序配置
  const sortBy = ref('competitionLevel')
  const sortOrder = ref('desc')

  // 奖项表单
  const awardForm = reactive<Partial<AwardItem>>({
    competitionName: '',
    competitionLevel: '国家级',
    competitionTrack: '',
    awardLevel: '一等奖',
    winners: [],
    year: new Date().getFullYear(),
    awardDate: new Date().toISOString().slice(0, 7),
  })

  // 表单验证规则
  const rules = {
    competitionName: [
      { required: true, message: '请输入奖项名称', trigger: 'blur' },
    ],
    competitionLevel: [
      { required: true, message: '请选择竞赛级别', trigger: 'blur' },
    ],
    awardLevel: [
      { required: true, message: '请选择获奖等级', trigger: 'blur' },
    ],
    winners: [{ required: true, message: '请输入获奖人员', trigger: 'blur' }],
    year: [{ required: true, message: '请输入获得年份', trigger: 'blur' }],
    awardDate: [{ required: true, message: '请选择获奖日期', trigger: 'blur' }],
  }

  // 竞赛级别优先级
  const competitionLevelPriority: Record<string, number> = {
    国家级: 1,
    省级: 2,
    校级: 3,
  }
  
  // 奖项级别优先级
  const awardLevelPriority: Record<string, number> = {
    一等奖: 1,
    金牌: 2,
    二等奖: 3,
    银牌: 4,
    三等奖: 5,
    铜牌: 6,
    优秀奖: 7,
    其他: 8,
  }

  // 排序变化处理
  const handleSortChange = () => {
    currentPage.value = 1
  }

  // 筛选、排序、分页后的数据
  const filteredAwardsData = computed(() => {
    // 1. 筛选逻辑
    const filtered = awardsData.value.filter(award => {
      const matchesKeyword =
        !searchKeyword.value ||
        award.competitionName?.includes(searchKeyword.value) ||
        award.winners?.some(w => w.includes(searchKeyword.value)) ||
        award.awardLevel?.includes(searchKeyword.value)

      const matchesLevel =
        !filter.competitionLevel ||
        award.competitionLevel === filter.competitionLevel

      const awardLevelMap: Record<string, string[]> = {
        一等奖: ['一等奖', '金牌'],
        二等奖: ['二等奖', '银牌'],
        三等奖: ['三等奖', '铜牌'],
      }

      const matchesAwardLevel =
        !filter.awardLevel ||
        (filter.awardLevel in awardLevelMap &&
          awardLevelMap[filter.awardLevel].includes(award.awardLevel)) ||
        award.awardLevel === filter.awardLevel

      const matchesYear = !filter.year || award.year?.toString() === filter.year

      const matchesCompetitionName =
        !filter.competitionName ||
        award.competitionName?.includes(filter.competitionName)

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

    // 2. 排序逻辑
    filtered.sort((a, b) => {
      let compareResult = 0
      switch (sortBy.value) {
        case 'competitionLevel': {
          if (sortOrder.value === 'desc') {
            const levelA = competitionLevelPriority[a.competitionLevel] || 0
            const levelB = competitionLevelPriority[b.competitionLevel] || 0
            compareResult = levelA - levelB

            if (compareResult === 0) {
              const awardA = awardLevelPriority[a.awardLevel || '其他'] || 0
              const awardB = awardLevelPriority[b.awardLevel || '其他'] || 0
              compareResult = awardA - awardB
            }
          } else {
            const levelA = competitionLevelPriority[a.competitionLevel] || 0
            const levelB = competitionLevelPriority[b.competitionLevel] || 0
            compareResult = levelB - levelA

            if (compareResult === 0) {
              const awardA = awardLevelPriority[a.awardLevel || '其他'] || 0
              const awardB = awardLevelPriority[b.awardLevel || '其他'] || 0
              compareResult = awardB - awardA
            }
          }
          break
        }

        case 'awardDate': {
          const dateA = new Date(a.awardDate || '').getTime() || 0
          const dateB = new Date(b.awardDate || '').getTime() || 0
          compareResult =
            sortOrder.value === 'desc'
              ? dateB - dateA
              : dateA - dateB
          break
        }

        case 'competitionName': {
          const nameA = a.competitionName || ''
          const nameB = b.competitionName || ''
          compareResult =
            sortOrder.value === 'desc'
              ? nameB.localeCompare(nameA)
              : nameA.localeCompare(nameB)
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
    return awardsData.value.filter(award => {
      const matchesKeyword =
        !searchKeyword.value ||
        award.competitionName?.includes(searchKeyword.value) ||
        award.winners?.some(w => w.includes(searchKeyword.value)) ||
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

  // 竞赛名称列表
  const competitionNames = computed(() => {
    const names = new Set<string>()
    awardsData.value.forEach(award => {
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
      ElMessage.error('获取奖项信息失败')
      console.error('获取奖项信息失败:', error)
      awardsData.value = []
    } finally {
      loading.value = false
    }
  }

  // 搜索事件
  const handleSearch = () => {
    currentPage.value = 1
  }

  // 重置筛选
  const resetFilter = () => {
    searchKeyword.value = ''
    filter.competitionLevel = ''
    filter.awardLevel = ''
    filter.year = ''
    filter.competitionName = ''
    filter.competitionTrack = ''
    sortBy.value = 'competitionLevel'
    sortOrder.value = 'desc'
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
    dialogTitle.value = '添加奖项'
    currentAwardId.value = null
    awardForm.competitionName = ''
    awardForm.competitionLevel = '国家级'
    awardForm.awardLevel = '一等奖'
    awardForm.winners = []
    awardForm.year = new Date().getFullYear()
    const now = new Date()
    awardForm.awardDate = `${now.getFullYear()}-${String(
      now.getMonth() + 1
    ).padStart(2, '0')}`
    winnersList.value = []
    dialogVisible.value = true
  }

  // 打开编辑对话框
  const openEditDialog = (row: AwardItem) => {
    dialogTitle.value = '编辑奖项'
    currentAwardId.value = row.id
    Object.assign(awardForm, row)
    winnersList.value = [...(row.winners || [])]
    dialogVisible.value = true
  }

  // 添加获奖人员
  const addWinner = () => {
    const name = newWinnerName.value.trim()
    if (name && !winnersList.value.includes(name)) {
      winnersList.value.push(name)
      newWinnerName.value = ''
    } else if (winnersList.value.includes(name)) {
      ElMessage.warning('该获奖人员已存在')
    }
  }

  // 删除获奖人员
  const removeWinner = (index: number) => {
    winnersList.value.splice(index, 1)
  }

  // 提交表单
  const submitForm = async () => {
    if (!awardFormRef.value) return

    try {
      awardForm.winners = winnersList.value
        .map(name => name.trim())
        .filter(Boolean)

      if (!awardForm.awardDate || typeof awardForm.awardDate !== 'string') {
        return
      }
      if (!awardForm.awardDate.endsWith('-01')) {
        awardForm.awardDate += '-01'
      }
      awardForm.year = Number(awardForm.awardDate?.slice(0, 4))

      await awardFormRef.value.validate()
      loading.value = true

      if (currentAwardId.value) {
        await updateAward(currentAwardId.value, {
          competitionName: awardForm.competitionName || '',
          competitionLevel: awardForm.competitionLevel || '',
          competitionTrack: awardForm.competitionTrack || '',
          awardLevel: awardForm.awardLevel || '一等奖',
          winners: awardForm.winners || [],
          year: awardForm.year || new Date().getFullYear(),
          awardDate:
            awardForm.awardDate || new Date().toISOString().slice(0, 7),
        })
        ElMessage.success('更新奖项成功')
      } else {
        await createAward(awardForm as Omit<AwardItem, 'id'>)
        ElMessage.success('添加奖项成功')
      }

      dialogVisible.value = false
      loadAwards()
    } catch (error) {
      ElMessage.error(currentAwardId.value ? '更新奖项失败' : '添加奖项失败')
      console.error('提交表单失败:', error)
    } finally {
      loading.value = false
    }
  }

  // 删除奖项
  const deleteAward = async (id: number) => {
    try {
      await ElMessageBox.confirm('确定要删除这个奖项吗？', '确认删除', {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })

      loading.value = true
      await deleteAwardApi(id)
      ElMessage.success('删除奖项成功')
      loadAwards()
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('删除奖项失败')
        console.error('删除奖项失败:', error)
      }
    } finally {
      loading.value = false
    }
  }

  // 页面加载时初始化数据
  onMounted(() => {
    loadAwards()
  })
  
  // 监听获奖日期变化，自动更新年份
  watch(
    () => awardForm.awardDate,
    (newDate) => {
      if (newDate && typeof newDate === 'string' && newDate.length >= 4) {
        awardForm.year = Number(newDate.slice(0, 4))
      }
    }
  )
  
  watch(
    () => filter.competitionName,
    () => {
      filter.competitionTrack = ''
    }
  )
  
  // 监听窗口大小变化
  const handleResize = () => {
    // 根据屏幕大小调整分页大小
    if (isMobile.value && pageSize.value > 10) {
      pageSize.value = 5
    } else if (!isMobile.value && pageSize.value === 5) {
      pageSize.value = 10
    }
  }
  
  onMounted(() => {
    window.addEventListener('resize', handleResize)
  })
</script>

<style scoped>
  .award-container {
    padding: 20px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .add-btn {
    margin-top: 0;
  }
  
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
  
  /* 获奖人员标签样式 */
  .winners-tags-container {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    padding: 5px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    min-height: 32px;
  }
  
  .winners-tags-container .el-tag {
    margin: 5px 5px 5px 0;
  }
  
  .winners-input {
    flex: 1;
    min-width: 100px;
    margin: 5px 0;
    border: none;
    box-shadow: none;
  }
  
  .winners-input .el-input__inner {
    border: none;
    box-shadow: none;
    padding: 0;
    height: auto;
    line-height: normal;
  }
  
  .winners-input:focus-within {
    box-shadow: none;
  }
  
  .action-buttons {
    display: flex;
    flex-direction: column;
  }
  
  .dialog-footer {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  /* 移动端样式优化 */
  @media (max-width: 768px) {
    .award-container {
      padding: 10px;
    }
    
    .card-header {
      font-size: 16px;
    }
    
    .sort-container {
      padding: 8px 10px;
    }
    
    .pagination-container {
      margin-top: 10px;
      justify-content: center;
    }
    
    /* 表格在移动端的优化显示 */
    .table-container {
      overflow-x: auto;
    }
    
    :deep(.el-table) {
      font-size: 12px;
    }
    
    :deep(.el-table th),
    :deep(.el-table td) {
      padding: 8px 0;
    }
    
    :deep(.el-table .el-button) {
      font-size: 12px;
      padding: 5px 10px;
    }
    
    :deep(.el-dialog) {
      --el-dialog-content-font-size: 14px;
    }
    
    :deep(.el-form-item__label) {
      font-size: 14px;
      padding-bottom: 5px;
    }
    
    :deep(.el-select) {
      width: 100%;
    }
  }
  
  @media (max-width: 480px) {
    .award-container {
      padding: 5px;
    }
    
    :deep(.el-table th),
    :deep(.el-table td) {
      padding: 5px 0;
      font-size: 11px;
    }
    
    :deep(.el-table .el-button) {
      font-size: 11px;
      padding: 4px 8px;
    }
    
    :deep(.el-pagination) {
      font-size: 12px;
    }
    
    :deep(.el-pagination .el-pagination__total),
    :deep(.el-pagination .el-pagination__jump) {
      font-size: 12px;
    }
    
    :deep(.el-dialog__header) {
      padding: 15px;
    }
    
    :deep(.el-dialog__body) {
      padding: 15px;
    }
    
    :deep(.el-dialog__footer) {
      padding: 15px;
    }
    
    :deep(.el-tag) {
      font-size: 12px;
      padding: 0 8px;
    }
  }
</style>