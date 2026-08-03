<template>
  <div class="application-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报名信息管理</span>
          <div class="card-actions">
            <el-button
              type="primary"
              icon="Download"
              @click="exportApplications"
              :size="isMobile ? 'small' : 'default'"
            >
              {{ isMobile ? '' : '导出数据' }}
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索 -->
      <el-form :model="searchForm" class="search-form">
        <el-row :gutter="isMobile ? 10 : 20">
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="姓名">
              <el-input v-model="searchForm.name" placeholder="请输入姓名" @keyup.enter="handleSearch" />
            </el-form-item>
          </el-col>
          
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="学号">
              <el-input
                v-model="searchForm.studentId"
                placeholder="请输入学号"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="QQ号">
              <el-input
                v-model="searchForm.QQNumber"
                placeholder="请输入QQ号"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="专业">
              <el-select
                v-model="searchForm.majors"
                placeholder="请选择专业（可多选）"
                multiple
                collapse-tags
                class="full-width-control"
                @change="handleSearch"
                :loading="majorLoading"
              >
                <el-option
                  v-for="major in majorOptions"
                  :key="major"
                  :label="major"
                  :value="major"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="第一志愿部门">
              <el-select
                v-model="searchForm.department"
                placeholder="请选择部门"
                :class="{ 'full-width-control': isMobile }"
                @change="handleSearch"
              >
                <el-option
                  v-for="department in primaryDepartmentOptions"
                  :key="department"
                  :label="department"
                  :value="department"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="第二志愿部门">
              <el-select
                v-model="searchForm.secondDepartment"
                placeholder="请选择部门"
                :class="{ 'full-width-control': isMobile }"
                @change="handleSearch"
              >
                <el-option
                  v-for="department in secondaryDepartmentOptions"
                  :key="department"
                  :label="department"
                  :value="department"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="isMobile ? 24 : 12">
            <el-form-item label="部门匹配">
              <el-radio-group
                v-model="searchForm.matchAllDepartments"
                @change="handleSearch"
                :class="{ 'full-width-control': isMobile }"
              >
                <el-radio :label="false">匹配任一志愿</el-radio>
                <el-radio :label="true">同时匹配两个志愿</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          
          <el-col :span="24">
            <div :class="isMobile ? 'mobile-search-buttons' : 'desktop-search-buttons'">
              <el-button
                type="primary"
                @click="handleSearch"
                :class="isMobile ? 'mobile-full-button mobile-button-gap' : 'search-btn'"
              >
                搜索
              </el-button>
              <el-button
                @click="resetForm"
                :class="{ 'mobile-full-button mobile-button-gap': isMobile }"
              >
                重置
              </el-button>
              <el-button
                type="danger"
                @click="handleBatchDelete"
                v-if="selectedRows.length > 0"
                :class="{ 'mobile-full-button': isMobile }"
              >
                批量删除
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 列表 -->
      <div class="table-container-wrapper">
        <div 
          class="table-container" 
          ref="tableContainerRef"
          @scroll="onTableScroll"
          @touchstart="onTouchStart"
          @touchmove="onTouchMove"
          @touchend="onTouchEnd">
          <el-table
            :data="applicationsData.records"
            style="width: 100%"
            v-loading="loading"
            @selection-change="handleSelectionChange"
            border
            stripe
            :class="{ 'mobile-table': isMobile }"
            :max-height="isMobile ? '400' : undefined"
          >
            <el-table-column type="selection" :width="isMobile ? 35 : 55" />
            <el-table-column prop="name" label="姓名" :width="isMobile ? 60 : 100" />
            <el-table-column prop="studentId" label="学号" :width="isMobile ? 80 : 120" />
            <el-table-column prop="major" label="专业" :width="isMobile ? 90 : 180" />
            <el-table-column prop="phone" label="电话" :width="isMobile ? 90 : 150" />
            <el-table-column prop="QQNumber" label="QQ号" :width="isMobile ? 90 : 150" />
            <el-table-column prop="department" label="部门" :width="isMobile ? 60 : 100" />
            <el-table-column
              prop="secondDepartment"
              label="第二志愿"
              :width="isMobile ? 70 : 120"
            />
            <el-table-column label="操作" :width="isMobile ? undefined : 170" :fixed="isMobile ? false : 'right'">
              <template #default="scope">
                <div :class="isMobile ? 'mobile-button-group' : 'desktop-button-group'">
                  <el-button
                    type="primary"
                    :size="isMobile ? 'small' : 'default'"
                    @click="viewDetail(scope.row)"
                  >
                    {{ isMobile ? '查看' : '查看详情' }}
                  </el-button>
                  <el-button
                    type="danger"
                    :size="isMobile ? 'small' : 'default'"
                    @click="deleteApplication(scope.row.id)"
                  >
                    删除
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 滚动指示器 -->
        <div class="horizontal-scroll-indicator" v-if="showScrollIndicator">
          <div class="scroll-thumb" :style="{ width: scrollThumbWidth, transform: `translateX(${scrollThumbPosition})` }"></div>
        </div>
      </div>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="applicationsData.current"
          v-model:page-size="applicationsData.size"
          :page-sizes="isMobile ? [5, 10, 20] : [10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="applicationsData.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :small="isMobile"
        />
      </div>
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="报名详情" 
      :width="isMobile ? '100%' : '720px'"
      class="admin-dialog"
    >
      <el-form 
        :model="currentApplication || {}" 
        label-width="100px"
        :label-position="isMobile ? 'top' : 'right'"
      >
        <el-form-item label="姓名">
          <span>{{ currentApplication?.name }}</span>
        </el-form-item>
        <el-form-item label="学号">
          <span>{{ currentApplication?.studentId }}</span>
        </el-form-item>
        <el-form-item label="专业">
          <span>{{ currentApplication?.major }}</span>
        </el-form-item>
        <el-form-item label="电话">
          <span>{{ currentApplication?.phone }}</span>
        </el-form-item>
        <el-form-item label="QQ号">
          <span>{{ currentApplication?.QQNumber }}</span>
        </el-form-item>
        <el-form-item label="部门">
          <span>{{ currentApplication?.department }}</span>
        </el-form-item>
        <el-form-item label="第二志愿部门">
          <span>{{ currentApplication?.secondDepartment || "无" }}</span>
        </el-form-item>
        <el-form-item label="兴趣方向">
          <span>{{ currentApplication?.interests?.join("、") || "无" }}</span>
        </el-form-item>
        <el-form-item label="申请理由">
          <span class="text-content">{{ currentApplication?.reason }}</span>
        </el-form-item>
        <el-form-item label="个人介绍">
          <span class="text-content">{{ currentApplication?.introduction }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button 
          @click="dialogVisible = false"
          :size="isMobile ? 'large' : 'default'"
          :class="{ 'mobile-full-button': isMobile }"
          v-if="isMobile"
        >
          关闭
        </el-button>
        <el-button @click="dialogVisible = false" v-else>
          关闭
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import {
  getApplications,
  deleteApplication as apiDeleteApplication,
  getApplicationMajors,
} from "@/services/adminService"
import * as XLSX from "xlsx"

const primaryDepartmentOptions = ["算法部", "项目竞赛部", "综合管理部"]
const secondaryDepartmentOptions = [...primaryDepartmentOptions, "无"]
const viewportWidth = ref(window.innerWidth)
const isMobile = computed(() => viewportWidth.value <= 768)

const searchForm = reactive({
  name: "",
  studentId: "",
  department: "",
  secondDepartment: "",
  majors: [],
  QQNumber: "",
  matchAllDepartments: false,
})

const applicationsData = ref({
  current: 1,
  size: isMobile.value ? 5 : 10,
  total: 0,
  pages: 0,
  records: [],
})

const dialogVisible = ref(false)
const currentApplication = ref(null)
const loading = ref(false)
const selectedRows = ref([])
const majorOptions = ref([])
const majorLoading = ref(false)

const tableContainerRef = ref(null)
const showScrollIndicator = ref(false)
const scrollThumbWidth = ref('0px')
const scrollThumbPosition = ref('0px')

const touchStartX = ref(0)
const scrollStart = ref(0)

/**
 * 加载专业筛选项。
 *
 * @returns {Promise<void>}
 */
const loadMajorOptions = async () => {
  majorLoading.value = true
  try {
    const majors = await getApplicationMajors()
    majorOptions.value = majors
  } catch (error) {
    ElMessage.error("获取专业选项失败")
    console.error("获取专业选项失败:", error)
  } finally {
    majorLoading.value = false
  }
}

/**
 * 加载报名分页数据。
 *
 * @returns {Promise<void>}
 */
const loadApplications = async () => {
  loading.value = true
  try {
    const params = {
      current: applicationsData.value.current,
      size: applicationsData.value.size,
      name: searchForm.name,
      studentId: searchForm.studentId,
      department: searchForm.department,
      secondDepartment: searchForm.secondDepartment,
      majors: searchForm.majors.length > 0 ? searchForm.majors : undefined,
      QQNumber: searchForm.QQNumber,
      matchAllDepartments: searchForm.matchAllDepartments,
    }
    const data = await getApplications(params)
    applicationsData.value = data
  } catch (error) {
    ElMessage.error("获取报名信息失败")
    console.error("获取报名信息失败:", error)
  } finally {
    loading.value = false
    nextTick(() => {
      updateScrollIndicator()
    })
  }
}

/**
 * 按当前条件重新查询报名列表。
 */
const handleSearch = () => {
  applicationsData.value.current = 1
  loadApplications()
}

/**
 * 重置筛选表单并刷新列表。
 */
const resetForm = () => {
  searchForm.name = ""
  searchForm.studentId = ""
  searchForm.department = ""
  searchForm.secondDepartment = ""
  searchForm.majors = []
  searchForm.QQNumber = ""
  searchForm.matchAllDepartments = false
  applicationsData.value.current = 1
  loadApplications()
}

/**
 * 处理分页大小变化。
 *
 * @param {number} size
 */
const handleSizeChange = size => {
  applicationsData.value.size = size
  loadApplications()
}

/**
 * 处理页码变化。
 *
 * @param {number} current
 */
const handleCurrentChange = current => {
  applicationsData.value.current = current
  loadApplications()
}

/**
 * 打开报名详情弹窗。
 *
 * @param {any} row
 */
const viewDetail = row => {
  currentApplication.value = { ...row, id: row.id }
  dialogVisible.value = true
}

/**
 * 删除指定报名记录。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
const deleteApplication = async id => {
  try {
    await ElMessageBox.confirm("确定要删除这条报名信息吗？", "确认删除", {
      type: "warning",
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    loading.value = true
    await apiDeleteApplication(id)
    ElMessage.success("删除报名信息成功")
    loadApplications()
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除报名信息失败")
      console.error("删除报名信息失败:", error)
    }
  } finally {
    loading.value = false
  }
}

/**
 * 同步表格选中行。
 *
 * @param {any[]} rows
 */
const handleSelectionChange = rows => {
  selectedRows.value = rows.map(row => ({ ...row, id: row.id }))
}

/**
 * 批量删除当前选中的报名记录。
 *
 * @returns {Promise<void>}
 */
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm("确定要删除选中的报名信息吗？", "确认删除", {
      type: "warning",
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    loading.value = true
    const ids = selectedRows.value.map(row => row.id)
    for (const id of ids) {
      await apiDeleteApplication(id)
    }
    ElMessage.success("批量删除成功")
    loadApplications()
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("批量删除失败")
      console.error("批量删除失败:", error)
    }
  } finally {
    loading.value = false
  }
}

/**
 * 导出当前筛选条件下的报名数据。
 *
 * @returns {Promise<void>}
 */
const exportApplications = async () => {
  try {
    loading.value = true
    ElMessage.info("正在导出数据，请稍候...")

    const allData = await getApplications({
      current: 1,
      size: 1000,
      name: searchForm.name,
      studentId: searchForm.studentId,
      department: searchForm.department,
      secondDepartment: searchForm.secondDepartment,
      majors: searchForm.majors.length > 0 ? searchForm.majors : undefined,
    })

    const exportData = allData.records.map((item) => ({
      姓名: item.name,
      学号: item.studentId,
      专业: item.major,
      电话: item.phone,
      QQ号: item.QQNumber,
      部门: item.department,
      第二志愿部门: item.secondDepartment || "无",
      兴趣方向: item.interests?.join("、") || "无",
      申请理由: item.reason,
      个人介绍: item.introduction,
    }))

    const ws = XLSX.utils.json_to_sheet(exportData)

    ws["!cols"] = [
      { wch: 10 },
      { wch: 15 },
      { wch: 20 },
      { wch: 15 },
      { wch: 15 },
      { wch: 10 },
      { wch: 15 },
      { wch: 30 },
      { wch: 40 },
      { wch: 60 },
    ]

    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, "报名信息")

    XLSX.writeFile(wb, `报名信息_${new Date().toLocaleDateString()}.xlsx`)

    ElMessage.success("导出成功")
  } catch (error) {
    ElMessage.error("导出失败，请重试")
    console.error("导出失败:", error)
  } finally {
    loading.value = false
  }
}

/**
 * 处理窗口尺寸变化。
 */
const handleResize = () => {
  viewportWidth.value = window.innerWidth
  if (isMobile.value && applicationsData.value.size > 20) {
    applicationsData.value.size = 5
    loadApplications()
  } else if (!isMobile.value && applicationsData.value.size === 5) {
    applicationsData.value.size = 10
    loadApplications()
  }
  updateScrollIndicator()
}

/**
 * 同步表格滚动状态。
 */
const onTableScroll = () => {
  updateScrollIndicator()
}

/**
 * 更新水平滚动指示器。
 */
const updateScrollIndicator = () => {
  if (!isMobile.value || !tableContainerRef.value) return
  
  const container = tableContainerRef.value
  
  if (container.scrollWidth <= container.offsetWidth) {
    showScrollIndicator.value = false
    return
  }
  
  showScrollIndicator.value = true
  const scrollRatio = container.scrollLeft / (container.scrollWidth - container.offsetWidth)
  const thumbRatio = container.offsetWidth / container.scrollWidth
  
  scrollThumbWidth.value = `${Math.max(20, thumbRatio * 100)}%`
  scrollThumbPosition.value = `${scrollRatio * 100}%`
}

/**
 * 记录触摸起点。
 *
 * @param {TouchEvent} e
 */
const onTouchStart = e => {
  if (!tableContainerRef.value) return
  touchStartX.value = e.touches[0].clientX
  scrollStart.value = tableContainerRef.value.scrollLeft
}

/**
 * 跟随手势滚动表格。
 *
 * @param {TouchEvent} e
 */
const onTouchMove = e => {
  if (!tableContainerRef.value) return
  e.preventDefault()
  const touchX = e.touches[0].clientX
  const diff = touchStartX.value - touchX
  tableContainerRef.value.scrollLeft = scrollStart.value + diff
}

/**
 * 保留触摸结束钩子，便于后续扩展。
 */
const onTouchEnd = () => {}

/**
 * 初始化报名管理页。
 */
onMounted(() => {
  loadMajorOptions()
  loadApplications()
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
.application-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  flex-wrap: wrap;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
}

.full-width-control {
  width: 100%;
}

.search-btn {
  margin-right: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.mobile-full-button {
  width: 100%;
}

.mobile-button-gap {
  margin-bottom: 10px;
}

.table-container-wrapper {
  position: relative;
}

.table-container {
  overflow-x: auto;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
  -ms-overflow-style: none;
  margin-bottom: 5px;
  width: 100%;
  position: relative;
}

:deep(.el-table__body-wrapper::-webkit-scrollbar) {
  display: none;
}

:deep(.el-table__body-wrapper) {
  scrollbar-width: none;
  -ms-overflow-style: none;
}

:deep(.el-table) {
  width: 100% ;
  min-width: 100%;
}

:deep(.el-table__fixed-right) {
  height: 100% ;
  z-index: 10;
}

.table-container::-webkit-scrollbar {
  display: none;
}

.horizontal-scroll-indicator {
  height: 3px;
  background-color: #e4e7ed;
  border-radius: 2px;
  margin: 0 10px 15px 10px;
  position: relative;
}

.horizontal-scroll-indicator .scroll-thumb {
  position: absolute;
  height: 100%;
  background-color: #409eff;
  border-radius: 2px;
  transition: transform 0.2s ease;
}

.desktop-button-group {
  display: flex;
  gap: 8px;
  justify-content: flex-start;
  align-items: center;
}

.mobile-button-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.mobile-button-group .el-button {
  width: 100%;
  margin: 0;
}

:deep(.admin-dialog .el-dialog__body) {
  max-height: 80vh;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .application-container {
    padding: 10px;
  }
  
  .card-header {
    font-size: 16px;
  }
  
  .search-form {
    margin-bottom: 10px;
  }
  
  .mobile-search-buttons {
    display: flex;
    flex-direction: column;
  }
  
  .pagination-container {
    margin-top: 10px;
    justify-content: center;
  }
  
  :deep(.el-table) {
    font-size: 12px;
  }
  
  :deep(.el-table th) {
    padding: 6px 2px;
    line-height: 1.2;
  }
  
  :deep(.el-table td) {
    padding: 4px 2px;
    line-height: 1.2;
  }
  
  :deep(.el-table .el-button) {
    font-size: 11px;
    padding: 3px 6px;
    min-height: 24px;
  }
  
  :deep(.el-dialog) {
    --el-dialog-content-font-size: 13px;
  }

  :deep(.admin-dialog) {
    width: 100% !important;
    max-height: 80vh;
    margin: 0;
  }
  
  :deep(.el-form-item__label) {
    font-size: 13px;
  }
  
  :deep(.el-dialog__title) {
    font-size: 16px;
  }
  
  :deep(.el-form-item) {
    margin-bottom: 12px;
  }
  
  :deep(.el-form-item__content) {
    font-size: 13px;
  }
  
  .horizontal-scroll-indicator {
    margin: 0 15px 10px 15px;
  }
}

@media (max-width: 480px) {
  .application-container {
    padding: 5px;
  }
  
  :deep(.el-table th),
  :deep(.el-table td) {
    padding: 2px 1px;
    font-size: 10px;
  }
  
  :deep(.el-table .el-button) {
    font-size: 10px;
    padding: 2px 4px;
    min-height: 20px;
  }
  
  :deep(.el-pagination) {
    font-size: 12px;
  }
  
  :deep(.el-pagination .el-pagination__total),
  :deep(.el-pagination .el-pagination__jump) {
    font-size: 12px;
  }
  
  :deep(.el-dialog__header) {
    padding: 12px;
  }
  
  :deep(.el-dialog__body) {
    padding: 12px;
  }
  
  :deep(.el-dialog__footer) {
    padding: 12px;
  }
  
  :deep(.el-dialog) {
    --el-dialog-content-font-size: 12px;
  }
  
  :deep(.el-form-item__label) {
    font-size: 12px;
  }
  
  :deep(.el-dialog__title) {
    font-size: 14px;
  }
  
  :deep(.el-form-item__content) {
    font-size: 12px;
  }
  
  :deep(.el-form-item) {
    margin-bottom: 8px;
  }
}

.text-content {
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 1.5;
}
</style>
