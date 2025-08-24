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
            >
              导出数据
            </el-button>
          </div>
        </div>
      </template>
      <el-form :model="searchForm" class="search-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="姓名">
              <el-input v-model="searchForm.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="学号">
              <el-input
                v-model="searchForm.studentId"
                placeholder="请输入学号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="部门">
              <el-select
                v-model="searchForm.department"
                placeholder="请选择部门"
              >
                <el-option label="技术部" value="技术部" />
                <el-option label="宣传部" value="宣传部" />
                <el-option label="组织部" value="组织部" />
                <el-option label="外联部" value="外联部" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
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
      <el-table
        :data="applicationsData.records"
        style="width: 100%"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        stripe
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="major" label="专业" width="180" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="QQNumber" label="QQ号" width="150" />
        <el-table-column prop="department" label="部门" width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="viewDetail(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteApplication(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="applicationsData.current"
          v-model:page-size="applicationsData.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="applicationsData.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="dialogVisible" title="报名详情" width="600px">
      <el-form :model="currentApplication" label-width="100px">
        <el-form-item label="姓名">
          <span>{{ currentApplication?.name }}</span>
        </el-form-item>
        <el-form-item label="学号">
          <span>{{ currentApplication?.studentId }}</span>
        </el-form-item>
        <el-form-item label="班级">
          <span>{{ currentApplication?.className }}</span>
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
        <el-form-item label="个人介绍">
          <span>{{ currentApplication?.introduction }}</span>
        </el-form-item>
        <el-form-item label="申请理由">
          <span>{{ currentApplication?.reason }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import {
  getApplications,
  deleteApplication as apiDeleteApplication,
  type ApplicationPageData,
} from "../../../services/adminService"
import type { joinForm } from "../../../services/applicationsService"

const searchForm = reactive<{
  name: string
  studentId: string
  department: string
}>({
  name: "",
  studentId: "",
  department: "",
})
const applicationsData = ref<ApplicationPageData<joinForm>>({
  current: 1,
  size: 10,
  total: 0,
  pages: 0,
  records: [],
})
const dialogVisible = ref(false)
const currentApplication = ref<joinForm | null>(null)
const loading = ref(false)
const selectedRows = ref<joinForm[]>([])

// 加载报名数据
const loadApplications = async () => {
  loading.value = true
  try {
    const params = {
      current: applicationsData.value.current,
      size: applicationsData.value.size,
      name: searchForm.name,
      studentId: searchForm.studentId,
    }
    const data = await getApplications(params)
    applicationsData.value = data
  } catch (error) {
    ElMessage.error("获取报名信息失败")
    console.error("获取报名信息失败:", error)
  } finally {
    loading.value = false
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
  currentApplication.value = { ...row }
  dialogVisible.value = true
}

// 删除报名信息
const deleteApplication = async (id: number) => {
  try {
    await ElMessageBox.confirm("确定要删除这条报名信息吗？", "确认删除", {
      type: "warning",
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
  selectedRows.value = rows
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm("确定要删除选中的报名信息吗？", "确认删除", {
      type: "warning",
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
const exportApplications = () => {
  // 实现导出逻辑，可以调用后端API或前端生成Excel
  ElMessage.info("导出功能开发中")
}

// 初始加载数据
onMounted(() => {
  loadApplications()
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
</style>
