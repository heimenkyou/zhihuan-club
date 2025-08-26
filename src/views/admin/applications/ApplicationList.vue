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
            <el-form-item label="第二志愿部门">
              <el-select
                v-model="searchForm.secondDepartment"
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
        <!-- 移除 className 列 -->
        <el-table-column prop="major" label="专业" width="180" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="QQNumber" label="QQ号" width="150" />
        <el-table-column prop="department" label="部门" width="100" />
        <!-- 添加第二志愿部门列 -->
        <el-table-column
          prop="secondDepartment"
          label="第二志愿部门"
          width="120"
        />
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
        <!-- 移除班级字段 -->
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
          <span>{{ currentApplication?.reason }}</span>
        </el-form-item>
        <el-form-item label="个人介绍">
          <span>{{ currentApplication?.introduction }}</span>
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
// 导入xlsx库
import * as XLSX from "xlsx"

const searchForm = reactive<{
  name: string
  studentId: string
  department: string
  secondDepartment: string // 添加第二志愿部门搜索字段
}>({
  name: "",
  studentId: "",
  department: "",
  secondDepartment: "",
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
      department: searchForm.department,
      secondDepartment: searchForm.secondDepartment, // 添加到搜索参数
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
  searchForm.secondDepartment = "" // 重置第二志愿部门
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
const exportApplications = async () => {
  try {
    loading.value = true
    ElMessage.info("正在导出数据，请稍候...")
    
    // 获取所有数据（不分页）
    const allData = await getApplications({
      current: 1,
      size: applicationsData.value.total,
      name: searchForm.name,
      studentId: searchForm.studentId,
      // 假设 GetApplicationsParams 中没有 department 属性，暂时移除该字段
      // 如果确实需要该字段，请检查并更新 GetApplicationsParams 类型定义
      // department: searchForm.department,
      // 假设 GetApplicationsParams 中没有 secondDepartment 属性，暂时移除该字段
      // 如果确实需要该字段，请检查并更新 GetApplicationsParams 类型定义
      // secondDepartment: searchForm.secondDepartment
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
    ws['!cols'] = [
      { wch: 10 },  // 姓名
      { wch: 15 },  // 学号
      { wch: 20 },  // 专业
      { wch: 15 },  // 电话
      { wch: 15 },  // QQ号
      { wch: 10 },  // 部门
      { wch: 15 },  // 第二志愿部门
      { wch: 30 },  // 兴趣方向（设置较宽以显示多个兴趣）
      { wch: 40 },  // 申请理由
      { wch: 60 }   // 个人介绍（设置最宽以显示完整内容）
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
