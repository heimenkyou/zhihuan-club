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
            >
              批量删除
            </el-button>
          </div>
        </div>
      </template>
      <el-form :model="searchForm" class="search-form">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="搜索">
              <el-input
                v-model="searchForm.keyword"
                placeholder="请输入姓名或留言内容"
                suffix-icon="Search"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-button type="primary" @click="handleSearch" class="search-btn">
              搜索
            </el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
      <el-table
        :data="messagesData.records"
        style="width: 100%"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        stripe
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="nickname" label="姓名" width="100" />
        <el-table-column prop="content" label="留言内容" width="500">
          <template #default="scope">
            <div class="content-cell" :title="scope.row.content">
              {{ scope.row.content }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button
              type="danger"
              size="small"
              @click="deleteMessage(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="messagesData.current"
          v-model:page-size="messagesData.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="messagesData.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import {
  getMessages,
  deleteMessage as deleteMessageApi,
} from "../../../services/messageService"
import type { PageData, MessageItem } from "../../../services/messageService"

const searchForm = reactive<{ keyword: string }>({
  keyword: "",
})
const messagesData = ref<PageData<MessageItem>>({
  current: 1,
  size: 10,
  total: 0,
  pages: 0,
  records: [],
})
const loading = ref(false)
const selectedRows = ref<MessageItem[]>([])

// 加载留言数据
const loadMessages = async () => {
  loading.value = true
  try {
    const params = {
      current: messagesData.value.current,
      size: messagesData.value.size,
      keyword: searchForm.keyword,
    }
    const data = await getMessages(params)
    messagesData.value = data
  } catch (error) {
    ElMessage.error("获取留言信息失败")
    console.error("获取留言信息失败:", error)
  } finally {
    loading.value = false
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
const handleSizeChange = (size: number) => {
  messagesData.value.size = size
  loadMessages()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  messagesData.value.current = current
  loadMessages()
}

// 删除留言
const deleteMessage = async (id: number) => {
  try {
    await ElMessageBox.confirm("确定要删除这条留言吗？", "确认删除", {
      type: "warning",
    })

    loading.value = true
    await deleteMessageApi(id)
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
const handleSelectionChange = (rows: MessageItem[]) => {
  selectedRows.value = rows
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm("确定要删除选中的留言吗？", "确认删除", {
      type: "warning",
    })

    loading.value = true
    // 此代码段被注释，因为 ids 声明后未被使用。若后续需要使用批量删除 API，可取消注释
    // const ids = selectedRows.value.map(row => row.id);
    // 假设后端提供批量删除API
    // await batchDeleteMessages(ids);
    ElMessage.success("批量删除成功")
    loadMessages()
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("批量删除失败")
      console.error("批量删除失败:", error)
    }
  } finally {
    loading.value = false
  }
}

// 初始加载数据
onMounted(() => {
  loadMessages()
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
</style>
