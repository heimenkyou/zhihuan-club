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
      
      <!-- 移动端搜索表单优化 -->
      <el-form :model="searchForm" class="search-form">
        <el-row :gutter="isMobile ? 10 : 20">
          <el-col :span="24" v-if="isMobile">
            <el-form-item label="姓名">
              <el-input v-model="searchForm.name" placeholder="请输入姓名" @keyup.enter="handleSearch" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-else>
            <el-form-item label="姓名">
              <el-input v-model="searchForm.name" placeholder="请输入姓名" @keyup.enter="handleSearch" />
            </el-form-item>
          </el-col>
          
          <el-col :span="24" v-if="isMobile">
            <el-form-item label="学号">
              <el-input
                v-model="searchForm.studentId"
                placeholder="请输入学号"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-else>
            <el-form-item label="学号">
              <el-input
                v-model="searchForm.studentId"
                placeholder="请输入学号"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="24" v-if="isMobile">
            <el-form-item label="专业">
              <el-select
                v-model="searchForm.majors"
                placeholder="请选择专业（可多选）"
                multiple
                collapse-tags
                style="width: 100%"
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
          <el-col :span="12" v-else>
            <el-form-item label="专业">
              <el-select
                v-model="searchForm.majors"
                placeholder="请选择专业（可多选）"
                multiple
                collapse-tags
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
          
          <el-col :span="24" v-if="isMobile">
            <el-form-item label="第一志愿部门">
              <el-select
                v-model="searchForm.department"
                placeholder="请选择部门"
                style="width: 100%"
                @change="handleSearch"
              >
                <el-option label="技术部" value="技术部" />
                <el-option label="宣传部" value="宣传部" />
                <el-option label="组织部" value="组织部" />
                <el-option label="外联部" value="外联部" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-else>
            <el-form-item label="第一志愿部门">
              <el-select
                v-model="searchForm.department"
                placeholder="请选择部门"
                @change="handleSearch"
              >
                <el-option label="算法部" value="算法部" />
                <el-option label="项目竞赛部" value="项目竞赛部" />
                <el-option label="综合管理部" value="综合管理部" />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="24" v-if="isMobile">
            <el-form-item label="第二志愿部门">
              <el-select
                v-model="searchForm.secondDepartment"
                placeholder="请选择部门"
                style="width: 100%"
                @change="handleSearch"
              >
                <el-option label="技术部" value="技术部" />
                <el-option label="宣传部" value="宣传部" />
                <el-option label="组织部" value="组织部" />
                <el-option label="外联部" value="外联部" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-else>
            <el-form-item label="第二志愿部门">
              <el-select
                v-model="searchForm.secondDepartment"
                placeholder="请选择部门"
                @change="handleSearch"
              >
                <el-option label="算法部" value="算法部" />
                <el-option label="项目竞赛部" value="项目竞赛部" />
                <el-option label="综合管理部" value="综合管理部" />
                <el-option label="无" value="无" />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="24" v-if="isMobile">
            <div class="mobile-search-buttons">
              <el-button 
                type="primary" 
                @click="handleSearch" 
                class="search-btn"
                style="width: 100%; margin-bottom: 10px"
              >
                搜索
              </el-button>
              <el-button 
                @click="resetForm" 
                style="width: 100%; margin-bottom: 10px"
              >
                重置
              </el-button>
              <el-button
                type="danger"
                @click="handleBatchDelete"
                v-if="selectedRows.length > 0"
                style="width: 100%"
              >
                批量删除
              </el-button>
            </div>
          </el-col>
          <el-col :span="24" v-else>
            <el-button type="primary" @click="handleSearch" class="search-btn">
              搜索
            </el-button>
            <el-button @click="resetForm">重置</el-button>
            <el-button
              type="danger"
              @click="handleBatchDelete"
              v-if="selectedRows.length > 0"
            >
              批量删除
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 移动端表格优化 -->
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
            ref="tableRef"
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
            <el-table-column label="操作" :width="isMobile ? 70 : 200" fixed="right">
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
        <!-- 水平滚动指示器 -->
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

    <!-- 详情对话框 - 移动端适配 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="报名详情" 
      :width="isMobile ? '90%' : '600px'"
      :fullscreen="false"
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
          style="width: 100%"
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

<script lang="ts" setup>
import { ref, reactive, onMounted, computed, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import {
  getApplications,
  deleteApplication as apiDeleteApplication,
  getApplicationMajors,
  type ApplicationPageData,
} from "../../../services/adminService"
import type { joinForm } from "../../../services/applicationsService"
import * as XLSX from "xlsx"

// 检测是否为移动端
const isMobile = computed(() => {
  return window.innerWidth <= 768
})

const searchForm = reactive<{
  name: string
  studentId: string
  department: string
  secondDepartment: string
  majors: string[]
}>({
  name: "",
  studentId: "",
  department: "",
  secondDepartment: "",
  majors: [],
})

const applicationsData = ref<ApplicationPageData<joinForm & { id: number }>>({
  current: 1,
  size: isMobile.value ? 5 : 10, // 移动端默认显示更少数据
  total: 0,
  pages: 0,
  records: [],
})

const dialogVisible = ref(false)
const currentApplication = ref<(joinForm & { id: number }) | null>(null)
const loading = ref(false)
const selectedRows = ref<(joinForm & { id: number })[]>([])
const majorOptions = ref<string[]>([])
const majorLoading = ref(false)

// 表格滚动相关引用
const tableContainerRef = ref<HTMLElement | null>(null)
const tableRef = ref<InstanceType<typeof import('element-plus').ElTable> | null>(null)
const showScrollIndicator = ref(false)
const scrollThumbWidth = ref('0px')
const scrollThumbPosition = ref('0px')

// 触摸滑动相关变量
const touchStartX = ref(0)
const scrollStart = ref(0)

// 加载专业选项
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

// 加载报名数据
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

// 搜索
const handleSearch = () => {
  applicationsData.value.current = 1
  loadApplications()
}

// 重置表单
const resetForm = () => {
  searchForm.name = ""
  searchForm.studentId = ""
  searchForm.department = ""
  searchForm.secondDepartment = ""
  searchForm.majors = []
  applicationsData.value.current = 1
  loadApplications()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  applicationsData.value.size = size
  loadApplications()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  applicationsData.value.current = current
  loadApplications()
}

// 查看详情
const viewDetail = (row: joinForm) => {
  currentApplication.value = { ...row, id: (row as any).id }
  dialogVisible.value = true
}

// 删除报名信息
const deleteApplication = async (id: number) => {
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

// 处理选择变化
const handleSelectionChange = (rows: joinForm[]) => {
  selectedRows.value = rows.map((row) => ({ ...row, id: (row as any).id }))
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm("确定要删除选中的报名信息吗？", "确认删除", {
      type: "warning",
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    loading.value = true
    // 假设每行数据都有id属性
    const ids = selectedRows.value.map((row) => (row as any).id)
    // 循环删除（如果没有批量删除API）
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

// 导出数据
const exportApplications = async () => {
  try {
    loading.value = true
    ElMessage.info("正在导出数据，请稍候...")

    // 获取所有数据（不分页）
    const allData = await getApplications({
      current: 1,
      size: 1000, // 设置一个足够大的值以获取所有数据
      name: searchForm.name,
      studentId: searchForm.studentId,
      department: searchForm.department,
      secondDepartment: searchForm.secondDepartment,
      majors: searchForm.majors.length > 0 ? searchForm.majors : undefined,
    })

    // 准备导出数据
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

    // 创建工作表
    const ws = XLSX.utils.json_to_sheet(exportData)

    // 设置列宽 - 根据内容长度调整各列宽度
    ws["!cols"] = [
      { wch: 10 }, // 姓名
      { wch: 15 }, // 学号
      { wch: 20 }, // 专业
      { wch: 15 }, // 电话
      { wch: 15 }, // QQ号
      { wch: 10 }, // 部门
      { wch: 15 }, // 第二志愿部门
      { wch: 30 }, // 兴趣方向（设置较宽以显示多个兴趣）
      { wch: 40 }, // 申请理由
      { wch: 60 }, // 个人介绍（设置最宽以显示完整内容）
    ]

    // 创建工作簿
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, "报名信息")

    // 导出文件
    XLSX.writeFile(wb, `报名信息_${new Date().toLocaleDateString()}.xlsx`)

    ElMessage.success("导出成功")
  } catch (error) {
    ElMessage.error("导出失败，请重试")
    console.error("导出失败:", error)
  } finally {
    loading.value = false
  }
}

// 监听窗口大小变化
const handleResize = () => {
  // 响应式处理会在computed中自动更新
  // 但需要更新分页大小
  if (isMobile.value && applicationsData.value.size > 20) {
    applicationsData.value.size = 5
    loadApplications()
  }
  updateScrollIndicator()
}

// 处理表格滚动
const onTableScroll = () => {
  updateScrollIndicator()
}

// 更新滚动指示器
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

// 触摸事件处理
const onTouchStart = (e: TouchEvent) => {
  if (!tableContainerRef.value) return
  touchStartX.value = e.touches[0].clientX
  scrollStart.value = tableContainerRef.value.scrollLeft
}

const onTouchMove = (e: TouchEvent) => {
  if (!tableContainerRef.value) return
  e.preventDefault()
  const touchX = e.touches[0].clientX
  const diff = touchStartX.value - touchX
  tableContainerRef.value.scrollLeft = scrollStart.value + diff
}

const onTouchEnd = () => {
  // 触摸结束后的处理
}

onMounted(() => {
  loadMajorOptions()
  loadApplications()
  window.addEventListener('resize', handleResize)
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

.search-btn {
  margin-right: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 表格容器包装器 */
.table-container-wrapper {
  position: relative;
}

/* 表格容器 */
.table-container {
  overflow-x: auto;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  margin-bottom: 5px;
  width: 100%;
  position: relative;
}

/* 隐藏Element Plus表格的内置滚动条 */
:deep(.el-table__body-wrapper::-webkit-scrollbar) {
  display: none;
}

:deep(.el-table__body-wrapper) {
  scrollbar-width: none;
  -ms-overflow-style: none;
}

/* 确保表格占满容器 */
:deep(.el-table) {
  width: 100% ;
  min-width: 100%;
}

/* 确保操作列固定时其他列可以滚动 */
:deep(.el-table__fixed-right) {
  height: 100% ;
  z-index: 10;
}

.table-container::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

/* 水平滚动指示器 */
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

/* 按钮组样式 */
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

/* 移动端样式优化 */
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

/* 文本内容样式 */
.text-content {
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 1.5;
}
</style>