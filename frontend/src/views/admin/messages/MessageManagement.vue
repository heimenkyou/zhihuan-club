<template>
  <div class="message-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>留言板管理</span>
          <div class="card-actions">
            <el-button
              type="danger"
              @click="handleBatchDelete"
              v-if="selectedRows.length > 0"
              :size="isMobile ? 'small' : 'default'">
              批量删除
            </el-button>
          </div>
        </div>
      </template>
      <el-form :model="searchForm" class="search-form">
        <el-row :gutter="isMobile ? 10 : 20">
          <el-col :span="24" v-if="isMobile">
            <el-form-item label="搜索">
              <el-input
                v-model="searchForm.keyword"
                placeholder="请输入姓名或留言内容"
                suffix-icon="Search"
                @keyup.enter="handleSearch"
                :clearable="isMobile" />
            </el-form-item>
          </el-col>
          <el-col :span="16" v-else>
            <el-form-item label="搜索">
              <el-input
                v-model="searchForm.keyword"
                placeholder="请输入姓名或留言内容"
                suffix-icon="Search"
                @keyup.enter="handleSearch" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="isMobile">
            <div class="mobile-search-buttons">
              <el-button 
                type="primary" 
                @click="handleSearch" 
                class="search-btn"
                style="width: 100%; margin-bottom: 10px"
                :size="isMobile ? 'large' : 'default'">
                搜索
              </el-button>
              <el-button 
                @click="resetForm" 
                style="width: 100%"
                :size="isMobile ? 'large' : 'default'">
                重置
              </el-button>
            </div>
          </el-col>
          <el-col :span="8" v-else>
            <el-button type="primary" @click="handleSearch" class="search-btn">
              搜索
            </el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
      <div class="table-container">
        <el-table
          :data="messagesData.records"
          style="width: 100%"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          border
          stripe
          :class="{ 'mobile-table': isMobile }">
          <el-table-column type="selection" :width="isMobile ? 40 : 55" />
          <el-table-column prop="id" label="ID" :width="isMobile ? 60 : 80" />
          <el-table-column prop="nickname" label="姓名" :width="isMobile ? 80 : 100" />
          <el-table-column prop="content" label="留言内容" :width="isMobile ? 150 : 500">
            <template #default="scope">
              <div class="content-cell" :title="scope.row.content">
                {{ scope.row.content }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="likeCount" label="点赞数" :width="isMobile ? 70 : 80" />
          <el-table-column prop="createTime" label="创建时间" :width="isMobile ? 120 : 180" />
          <el-table-column label="操作" :width="isMobile ? 100 : 150" fixed="right">
            <template #default="scope">
              <el-button
                type="danger"
                :size="isMobile ? 'small' : 'small'"
                @click="deleteMessage(scope.row.id)">
                {{ isMobile ? '删除' : '删除' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 移动端分页优化 -->
      <div class="pagination-container" v-if="messagesData.total > 0">
        <!-- 移动端滑动分页 -->
        <div class="mobile-pagination" v-if="isMobile">
          <div class="page-numbers-wrapper">
            <div 
              class="page-numbers-container" 
              ref="pageContainerRef"
              @scroll="onPageScroll"
              @touchstart="onTouchStart"
              @touchmove="onTouchMove"
              @touchend="onTouchEnd">
              <div class="page-numbers" ref="pageNumbersRef">
                <el-button
                  v-for="page in totalPages"
                  :key="page"
                  :class="{ active: page === messagesData.current }"
                  @click="goToPage(page)"
                  size="small"
                  text>
                  {{ page }}
                </el-button>
              </div>
            </div>
            <div class="scroll-indicator" v-if="showScrollIndicator">
              <div class="scroll-thumb" :style="{ width: scrollThumbWidth, transform: `translateX(${scrollThumbPosition})` }"></div>
            </div>
          </div>
        </div>
        
        <!-- 桌面端分页 -->
        <el-pagination
          v-else
          v-model:current-page="messagesData.current"
          v-model:page-size="messagesData.size"
          :page-sizes="isMobile ? [5, 10, 20] : [10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="messagesData.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :small="isMobile" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { getAdminMessages, deleteAdminMessage } from '@/services/adminService'

// 检测是否为移动端
const isMobile = computed(() => {
  return window.innerWidth <= 768
})

const searchForm = reactive({
  keyword: "",
})
const messagesData = ref({
  current: 1,
  size: isMobile.value ? 5 : 10,
  total: 0,
  pages: 0,
  records: [],
})
const loading = ref(false)
const selectedRows = ref([])
const pageContainerRef = ref(null)
const pageNumbersRef = ref(null)
const showScrollIndicator = ref(false)
const scrollThumbWidth = ref('0px')
const scrollThumbPosition = ref('0px')

// 触摸滑动相关变量
const touchStartX = ref(0)
const scrollStart = ref(0)

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(messagesData.value.total / messagesData.value.size) || 1
})

// 加载留言数据
const loadMessages = async () => {
  loading.value = true
  try {
    const params = {
      current: messagesData.value.current,
      size: messagesData.value.size,
      keyword: searchForm.keyword,
    }
    const data = await getAdminMessages(params)
    messagesData.value = data
  } catch (error) {
    ElMessage.error("获取留言信息失败")
    console.error("获取留言信息失败:", error)
  } finally {
    loading.value = false
    // 数据加载完成后滚动到当前页
    nextTick(() => {
      scrollToCurrentPage()
      updateScrollIndicator()
    })
  }
}

// 搜索
const handleSearch = () => {
  messagesData.value.current = 1
  loadMessages()
}

// 重置表单
const resetForm = () => {
  searchForm.keyword = ""
  messagesData.value.current = 1
  loadMessages()
}

// 分页大小变化
const handleSizeChange = size => {
  messagesData.value.size = size
  messagesData.value.current = 1
  loadMessages()
}

// 当前页变化
const handleCurrentChange = current => {
  messagesData.value.current = current
  loadMessages()
}

// 跳转到指定页
const goToPage = page => {
  messagesData.value.current = page
  loadMessages()
}

// 滚动到当前页
const scrollToCurrentPage = () => {
  if (!isMobile.value || !pageContainerRef.value || !pageNumbersRef.value) return

  nextTick(() => {
    const container = pageContainerRef.value
    const buttons = pageNumbersRef.value.querySelectorAll('.el-button')
    const currentPageButton = buttons[messagesData.value.current - 1]

    if (currentPageButton) {
      const containerWidth = container.offsetWidth
      const buttonLeft = currentPageButton.offsetLeft
      const buttonWidth = currentPageButton.offsetWidth
      const scrollLeft = buttonLeft - (containerWidth / 2) + (buttonWidth / 2)

      container.scrollTo({
        left: Math.max(0, scrollLeft),
        behavior: 'smooth'
      })
    }
  })
}

// 更新滚动指示器
const updateScrollIndicator = () => {
  if (!isMobile.value || !pageContainerRef.value || !pageNumbersRef.value) return
  
  const container = pageContainerRef.value
  const content = pageNumbersRef.value
  
  if (content.scrollWidth <= container.offsetWidth) {
    showScrollIndicator.value = false
    return
  }
  
  showScrollIndicator.value = true
  const scrollRatio = container.scrollLeft / (content.scrollWidth - container.offsetWidth)
  const thumbRatio = container.offsetWidth / content.scrollWidth
  
  scrollThumbWidth.value = `${Math.max(20, thumbRatio * 100)}%`
  scrollThumbPosition.value = `${scrollRatio * 100}%`
}

// 处理页面滚动
const onPageScroll = () => {
  updateScrollIndicator()
}

// 触摸事件处理
const onTouchStart = e => {
  if (!pageContainerRef.value) return
  touchStartX.value = e.touches[0].clientX
  scrollStart.value = pageContainerRef.value.scrollLeft
}

const onTouchMove = e => {
  if (!pageContainerRef.value) return
  e.preventDefault()
  const touchX = e.touches[0].clientX
  const diff = touchStartX.value - touchX
  pageContainerRef.value.scrollLeft = scrollStart.value + diff
}

const onTouchEnd = () => {
  // 触摸结束后的处理
}

// 删除留言
const deleteMessage = async id => {
  try {
    await ElMessageBox.confirm("确定要删除这条留言吗？", "确认删除", {
      type: "warning",
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    loading.value = true
    await deleteAdminMessage(id)
    ElMessage.success("删除留言成功")
    loadMessages()
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除留言失败")
      console.error("删除留言失败:", error)
    }
  } finally {
    loading.value = false
  }
}

// 处理选择变化
const handleSelectionChange = rows => {
  selectedRows.value = rows
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm("确定要删除选中的留言吗？", "确认删除", {
      type: "warning",
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    loading.value = true
    await Promise.all(selectedRows.value.map(row => deleteAdminMessage(row.id)))
    ElMessage.success("批量删除成功")
    await loadMessages()
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("批量删除失败")
      console.error("批量删除失败:", error)
    }
  } finally {
    loading.value = false
  }
}

// 监听窗口大小变化
const handleResize = () => {
  // 根据屏幕大小调整分页大小
  if (isMobile.value && messagesData.value.size > 10) {
    messagesData.value.size = 5
  } else if (!isMobile.value && messagesData.value.size === 5) {
    messagesData.value.size = 10
  }
  updateScrollIndicator()
}

onMounted(() => {
  loadMessages()
  window.addEventListener('resize', handleResize)
  
  // 初始化滚动指示器
  setTimeout(() => {
    updateScrollIndicator()
  }, 100)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.message-container {
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

.content-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mobile-search-buttons {
  display: flex;
  flex-direction: column;
}

/* 移动端分页样式 */
.mobile-pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0 10px;
}

.page-numbers-wrapper {
  flex: 1;
  position: relative;
}

.page-numbers-container {
  overflow-x: auto;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  padding: 5px 0;
}

.page-numbers-container::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

.page-numbers {
  display: flex;
  gap: 5px;
  padding: 5px;
  width: max-content;
}

.page-numbers :deep(.el-button) {
  min-width: 32px;
  height: 32px;
  padding: 8px;
  font-size: 14px;
  border-radius: 50%;
  flex-shrink: 0;
}

.page-numbers :deep(.el-button.active) {
  background-color: #409eff;
  color: white;
  border-color: #409eff;
}

/* 滚动指示器 */
.scroll-indicator {
  height: 3px;
  background-color: #e4e7ed;
  border-radius: 2px;
  margin: 0 10px;
  position: relative;
}

.scroll-thumb {
  position: absolute;
  height: 100%;
  background-color: #409eff;
  border-radius: 2px;
  transition: transform 0.2s ease;
}

/* 移动端样式优化 */
@media (max-width: 768px) {
  .message-container {
    padding: 10px;
  }
  
  .card-header {
    font-size: 16px;
  }
  
  .search-form {
    margin-bottom: 10px;
  }
  
  .pagination-container {
    margin-top: 10px;
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
  
  .content-cell {
    white-space: normal;
    word-break: break-all;
  }
  
  .page-numbers :deep(.el-button) {
    min-width: 30px;
    height: 30px;
    font-size: 13px;
  }
  
  .scroll-indicator {
    margin: 5px 15px 0 15px;
  }
}

@media (max-width: 480px) {
  .message-container {
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
  
  .page-numbers :deep(.el-button) {
    min-width: 28px;
    height: 28px;
    font-size: 12px;
  }
}
</style>
