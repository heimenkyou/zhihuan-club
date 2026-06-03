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

      <!-- 搜索 -->
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

      <!-- 筛选 -->
      <div class="bg-white rounded-xl shadow-md p-4 mb-4">
        <h3 class="text-lg font-bold text-dark mb-3">奖项筛选</h3>

        <!-- 竞赛级别 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛级别</label
          >
          <div class="flex flex-wrap gap-1">
            <button
              v-for="option in competitionLevelOptions"
              :key="option.value || 'all'"
              :class="[
                filterChipClass,
                filter.competitionLevel === option.value
                  ? option.activeClass
                  : filterChipInactiveClass,
              ]"
              @click="filter.competitionLevel = option.value"
            >
              {{ option.label }}
            </button>
          </div>
        </div>

        <!-- 奖项级别 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >奖项级别</label
          >
          <div class="flex flex-wrap gap-1">
            <button
              v-for="option in awardLevelOptions"
              :key="option.value || 'all'"
              :class="[
                filterChipClass,
                filter.awardLevel === option.value
                  ? option.activeClass
                  : filterChipInactiveClass,
              ]"
              @click="filter.awardLevel = option.value"
            >
              {{ option.label }}
            </button>
          </div>
        </div>

        <!-- 竞赛年份 -->
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

        <!-- 竞赛项目 -->
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
        
        <!-- 赛道 -->
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
                filterChipClass,
                filter.competitionTrack === ''
                  ? 'bg-primary text-white'
                  : filterChipInactiveClass,
              ]"
              @click="filter.competitionTrack = ''"
            >
              全部
            </button>
            <button
              v-for="track in currentCompetitionTracks"
              :key="track"
              :class="[
                filterChipClass,
                filter.competitionTrack === track
                  ? 'bg-teal-500 text-white'
                  : filterChipInactiveClass,
              ]"
              @click="filter.competitionTrack = track"
            >
              {{ track }}
            </button>
          </div>
        </div>
        
        <!-- 重置 -->
        <div class="mt-3">
          <el-button 
            @click="resetFilter" 
            class="reset-btn"
            :size="isMobile ? 'small' : 'default'"
            :class="{ 'mobile-full-button': isMobile }"
          >
            重置筛选
          </el-button>
        </div>
      </div>

      <!-- 排序 -->
      <div class="sort-container mb-4 flex flex-col sm:flex-row items-start sm:items-center justify-start gap-2">
        <span class="text-sm text-gray-600">排序方式：</span>
        <div class="flex flex-col sm:flex-row items-start sm:items-center space-y-2 sm:space-y-0 sm:space-x-2 w-full sm:w-auto">
          <el-select
            v-model="sortBy"
            placeholder="排序依据"
            :size="isMobile ? 'small' : 'default'"
            @change="handleSortChange"
            class="full-width-control"
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
            class="full-width-control"
          >
            <el-option label="降序" value="desc" />
            <el-option label="升序" value="asc" />
          </el-select>
        </div>
      </div>

      <!-- 列表 -->
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
              {{ scope.row.winners?.join('、') || '' }}
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
                  :class="{ 'mobile-action-button': isMobile }"
                >
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  :size="isMobile ? 'small' : 'default'"
                  @click="deleteAward(scope.row.id)"
                  :class="{ 'mobile-action-button': isMobile }"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
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

      <!-- 编辑弹窗 -->
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
              class="full-width-control"
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
              class="full-width-control"
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
              class="full-width-control"
              :size="isMobile ? 'small' : 'default'"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button
              @click="dialogVisible = false"
              :size="isMobile ? 'large' : 'default'"
              :class="{ 'mobile-full-button': isMobile }"
            >
              取消
            </el-button>
            <el-button
              type="primary"
              @click="submitForm"
              :size="isMobile ? 'large' : 'default'"
              :class="{ 'mobile-full-button': isMobile }"
            >
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
  import { ref, reactive, computed, onMounted, onBeforeUnmount, watch } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {
    getAwards,
    createAward,
    updateAward,
    deleteAward as deleteAwardApi,
  } from '@/services/adminService'

  const filterChipClass = 'px-3 py-1 rounded-full text-xs transition-all'
  const filterChipInactiveClass = 'bg-gray-100 text-gray-700 hover:bg-gray-200'
  const competitionLevelOptions = [
    { label: '全部', value: '', activeClass: 'bg-primary text-white' },
    { label: '国家级', value: '国家级', activeClass: 'bg-blue-600 text-white' },
    { label: '省级', value: '省级', activeClass: 'bg-blue-500 text-white' },
    { label: '校级', value: '校级', activeClass: 'bg-blue-400 text-white' },
  ]
  const awardLevelOptions = [
    { label: '全部', value: '', activeClass: 'bg-primary text-white' },
    { label: '一等奖', value: '一等奖', activeClass: 'bg-yellow-500 text-white' },
    { label: '二等奖', value: '二等奖', activeClass: 'bg-gray-400 text-white' },
    { label: '三等奖', value: '三等奖', activeClass: 'bg-orange-600 text-white' },
    { label: '优秀奖', value: '优秀奖', activeClass: 'bg-green-500 text-white' },
    { label: '其他', value: '其他', activeClass: 'bg-purple-500 text-white' },
  ]
  const awardLevelMap = {
    一等奖: ['一等奖', '金牌'],
    二等奖: ['二等奖', '银牌'],
    三等奖: ['三等奖', '铜牌'],
  }
  const competitionLevelPriority = {
    国家级: 1,
    省级: 2,
    校级: 3,
  }
  const awardLevelPriority = {
    一等奖: 1,
    金牌: 2,
    二等奖: 3,
    银牌: 4,
    三等奖: 5,
    铜牌: 6,
    优秀奖: 7,
    其他: 8,
  }

  const viewportWidth = ref(window.innerWidth)
  const isMobile = computed(() => viewportWidth.value <= 768)
  const awardFormRef = ref(null)
  const searchKeyword = ref('')
  const filter = reactive({
    competitionLevel: '',
    awardLevel: '',
    year: '',
    competitionName: '',
    competitionTrack: '',
  })
  const awardsData = ref([])
  const loading = ref(false)
  const dialogVisible = ref(false)
  const dialogTitle = ref('添加奖项')
  const currentAwardId = ref(null)
  const currentPage = ref(1)
  const pageSize = ref(isMobile.value ? 5 : 10)
  const winnersList = ref([])
  const newWinnerName = ref('')

  /**
   * 生成可选年份列表。
   *
   * @returns {number[]}
   */
  const availableYears = computed(() => {
    const currentYear = new Date().getFullYear()
    return [currentYear, currentYear - 1, currentYear - 2, currentYear - 3, currentYear - 4]
  })

  /**
   * 返回当前竞赛名称下的赛道列表。
   *
   * @returns {string[]}
   */
  const currentCompetitionTracks = computed(() => {
    if (!filter.competitionName) return []

    const tracks = new Set()
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

  const sortBy = ref('competitionLevel')
  const sortOrder = ref('desc')

  const awardForm = reactive({
    competitionName: '',
    competitionLevel: '国家级',
    competitionTrack: '',
    awardLevel: '一等奖',
    winners: [],
    year: new Date().getFullYear(),
    awardDate: new Date().toISOString().slice(0, 7),
  })

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

  /**
   * 重置奖项表单到默认值。
   */
  const resetAwardForm = () => {
    awardForm.competitionName = ''
    awardForm.competitionLevel = '国家级'
    awardForm.competitionTrack = ''
    awardForm.awardLevel = '一等奖'
    awardForm.winners = []
    awardForm.year = new Date().getFullYear()
    const now = new Date()
    awardForm.awardDate = `${now.getFullYear()}-${String(
      now.getMonth() + 1
    ).padStart(2, '0')}`
    winnersList.value = []
    newWinnerName.value = ''
  }

  /**
   * 判断奖项是否命中当前筛选条件。
   *
   * @param {any} award
   * @returns {boolean}
   */
  const matchesAwardFilters = award => {
    const matchesKeyword =
      !searchKeyword.value ||
      award.competitionName?.includes(searchKeyword.value) ||
      award.winners?.some(winner => winner.includes(searchKeyword.value)) ||
      award.awardLevel?.includes(searchKeyword.value)

    const matchesLevel =
      !filter.competitionLevel ||
      award.competitionLevel === filter.competitionLevel

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
  }

  /**
   * 按当前排序方式排序奖项列表。
   *
   * @param {any[]} awards
   * @returns {any[]}
   */
  const sortAwards = awards => {
    return [...awards].sort((a, b) => {
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
          compareResult = sortOrder.value === 'desc' ? dateB - dateA : dateA - dateB
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
  }

  /**
   * 切换排序后回到第一页。
   */
  const handleSortChange = () => {
    currentPage.value = 1
  }

  /**
   * 返回筛选、排序、分页后的奖项列表。
   *
   * @returns {any[]}
   */
  const filteredAwardsData = computed(() => {
    const filtered = awardsData.value.filter(matchesAwardFilters)
    const sorted = sortAwards(filtered)
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    return sorted.slice(start, end)
  })

  /**
   * 返回筛选后的奖项总数。
   *
   * @returns {number}
   */
  const totalCount = computed(() => {
    return awardsData.value.filter(matchesAwardFilters).length
  })

  /**
   * 返回竞赛名称选项。
   *
   * @returns {string[]}
   */
  const competitionNames = computed(() => {
    const names = new Set()
    awardsData.value.forEach(award => {
      if (award.competitionName) names.add(award.competitionName)
    })
    return Array.from(names).sort()
  })

  /**
   * 加载奖项列表。
   *
   * @returns {Promise<void>}
   */
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

  /**
   * 根据当前条件刷新第一页。
   */
  const handleSearch = () => {
    currentPage.value = 1
  }

  /**
   * 重置筛选条件与排序方式。
   */
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

  /**
   * 处理分页大小变化。
   *
   * @param {number} size
   */
  const handleSizeChange = size => {
    pageSize.value = size
    currentPage.value = 1
  }

  /**
   * 处理页码变化。
   *
   * @param {number} current
   */
  const handleCurrentChange = current => {
    currentPage.value = current
  }

  /**
   * 打开新增奖项对话框。
   */
  const openAddDialog = () => {
    dialogTitle.value = '添加奖项'
    currentAwardId.value = null
    resetAwardForm()
    dialogVisible.value = true
  }

  /**
   * 打开编辑奖项对话框。
   *
   * @param {any} row
   */
  const openEditDialog = row => {
    dialogTitle.value = '编辑奖项'
    currentAwardId.value = row.id
    Object.assign(awardForm, row)
    winnersList.value = [...(row.winners || [])]
    dialogVisible.value = true
  }

  /**
   * 添加获奖人员标签。
   */
  const addWinner = () => {
    const name = newWinnerName.value.trim()
    if (name && !winnersList.value.includes(name)) {
      winnersList.value.push(name)
      newWinnerName.value = ''
    } else if (winnersList.value.includes(name)) {
      ElMessage.warning('该获奖人员已存在')
    }
  }

  /**
   * 移除指定获奖人员。
   *
   * @param {number} index
   */
  const removeWinner = index => {
    winnersList.value.splice(index, 1)
  }

  /**
   * 提交奖项表单。
   *
   * @returns {Promise<void>}
   */
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

      const payload = {
        competitionName: awardForm.competitionName || '',
        competitionLevel: awardForm.competitionLevel || '',
        competitionTrack: awardForm.competitionTrack || '',
        awardLevel: awardForm.awardLevel || '一等奖',
        winners: awardForm.winners || [],
        year: awardForm.year || new Date().getFullYear(),
        awardDate:
          awardForm.awardDate || new Date().toISOString().slice(0, 7),
      }

      if (currentAwardId.value) {
        await updateAward(currentAwardId.value, payload)
        ElMessage.success('更新奖项成功')
      } else {
        await createAward(payload)
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

  /**
   * 删除指定奖项。
   *
   * @param {number} id
   * @returns {Promise<void>}
   */
  const deleteAward = async id => {
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

  /**
   * 同步获奖日期对应的年份。
   */
  watch(
    () => awardForm.awardDate,
    (newDate) => {
      if (newDate && typeof newDate === 'string' && newDate.length >= 4) {
        awardForm.year = Number(newDate.slice(0, 4))
      }
    }
  )

  /**
   * 切换竞赛名称时清空赛道筛选。
   */
  watch(
    () => filter.competitionName,
    () => {
      filter.competitionTrack = ''
    }
  )

  /**
   * 处理窗口尺寸变化。
   */
  const handleResize = () => {
    viewportWidth.value = window.innerWidth
    if (isMobile.value && pageSize.value > 10) {
      pageSize.value = 5
    } else if (!isMobile.value && pageSize.value === 5) {
      pageSize.value = 10
    }
  }

  /**
   * 初始化页面与监听器。
   */
  onMounted(() => {
    loadAwards()
    window.addEventListener('resize', handleResize)
  })

  /**
   * 清理窗口监听器。
   */
  onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize)
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

  .full-width-control {
    width: 100%;
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

  .mobile-action-button {
    width: 100%;
  }
  
  .dialog-footer {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .mobile-full-button {
    width: 100%;
  }
  
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
