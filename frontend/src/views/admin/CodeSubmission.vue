<!-- 暂时无用！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ -->
<!-- 暂时无用！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ -->
<!-- 暂时无用！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ -->
<!-- 暂时无用！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ -->
<!-- 暂时无用！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ -->
<!-- 暂时无用！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！ -->

<template>
  <div class="code-submission-container">
    <el-card class="mb-4">
      <template #header>
        <div class="card-header">
          <span>代码提交</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
        class="demo-ruleForm"
      >
        <el-form-item label="学号" prop="studentId">
          <el-input
            v-model="formData.studentId"
            placeholder="请输入学号"
            maxlength="32"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入姓名"
            maxlength="32"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            placeholder="请输入项目描述"
            :rows="4"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>提交记录</span>
        </div>
      </template>
      <el-table
        v-if="!codeSubmissionStore.isLoading"
        :data="codeSubmissionStore.submissionList"
        stripe
        style="width: 100%"
      >
        <template #empty>
          <div v-if="!codeSubmissionStore.isLoading" class="empty-text">
            暂无提交记录
          </div>
        </template>
      </el-table>
      <div v-else class="loading-container">
        <el-empty description="加载中..." />
      </div>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="studentId" label="学号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column
        prop="description"
        label="项目描述"
        :show-overflow-tooltip="true"
      />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="scope">
          <el-button
            type="primary"
            size="small"
            @click="handleEdit(scope.row)"
            :disabled="!isSuperAdmin"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(scope.row.id)"
            :disabled="!isSuperAdmin"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" title="编辑代码提交记录" width="50%">
      <el-form
        ref="editFormRef"
        :model="editFormData"
        :rules="editRules"
        label-width="80px"
      >
        <el-form-item label="学号" prop="studentId">
          <el-input
            v-model="editFormData.studentId"
            placeholder="请输入学号"
            maxlength="32"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="editFormData.name"
            placeholder="请输入姓名"
            maxlength="32"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="editFormData.description"
            type="textarea"
            placeholder="请输入项目描述"
            :rows="4"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEditSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { useAdminStore } from '@/stores/adminStore'
  import { useCodeSubmissionStore } from '@/stores/codeSubmissionStore'
  import {
    submitCode,
    getCodeSubmissions,
    updateCodeSubmission,
    deleteCodeSubmission,
    type CodeSubmission,
    type CodeSubmissionReq,
  } from '@/services/codeSubmissionService'

  const adminStore = useAdminStore()
  const codeSubmissionStore = useCodeSubmissionStore()
  const isSuperAdmin = adminStore.isSuperAdmin()

  // 表单数据
  const formData = reactive<CodeSubmissionReq>({
    studentId: '',
    name: '',
    description: '',
  })

  // 表单验证规则
  const rules = reactive({
    studentId: [
      { required: true, message: '请输入学号', trigger: 'blur' },
      {
        min: 1,
        max: 32,
        message: '学号长度在 1 到 32 个字符',
        trigger: 'blur',
      },
    ],
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      {
        min: 1,
        max: 32,
        message: '姓名长度在 1 到 32 个字符',
        trigger: 'blur',
      },
    ],
    description: [
      { required: true, message: '请输入项目描述', trigger: 'blur' },
      {
        min: 1,
        max: 1000,
        message: '项目描述长度在 1 到 1000 个字符',
        trigger: 'blur',
      },
    ],
  })

  // 分页数据
  const pagination = reactive({
    current: 1,
    size: 10,
    total: 0,
  })

  // 编辑对话框
  const dialogVisible = ref(false)
  const editFormData = reactive<CodeSubmissionReq>({
    studentId: '',
    name: '',
    description: '',
  })
  const currentSubmissionId = ref(0)

  // 编辑表单验证规则
  const editRules = reactive({
    studentId: [
      { required: true, message: '请输入学号', trigger: 'blur' },
      {
        min: 1,
        max: 32,
        message: '学号长度在 1 到 32 个字符',
        trigger: 'blur',
      },
    ],
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      {
        min: 1,
        max: 32,
        message: '姓名长度在 1 到 32 个字符',
        trigger: 'blur',
      },
    ],
    description: [
      { required: true, message: '请输入项目描述', trigger: 'blur' },
      {
        min: 1,
        max: 1000,
        message: '项目描述长度在 1 到 1000 个字符',
        trigger: 'blur',
      },
    ],
  })

  // 表单引用
  const formRef = ref()
  const editFormRef = ref()

  // 加载提交记录
  const loadSubmissions = async () => {
    try {
      codeSubmissionStore.setLoading(true)
      codeSubmissionStore.clearError()

      const params = {
        current: pagination.current,
        size: pagination.size,
      }
      const result = await getCodeSubmissions(params)
      const records = result.data.records || []
      codeSubmissionStore.setSubmissions(records)
      pagination.total = result.data.total || 0
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      codeSubmissionStore.setError(errorMessage)
      ElMessage.error(`加载提交记录失败: ${errorMessage}`)
      console.error('加载提交记录失败:', error)
    } finally {
      codeSubmissionStore.setLoading(false)
    }
  }

  // 提交表单
  const handleSubmit = async () => {
    if (!formRef.value) return

    try {
      await formRef.value.validate()
      await submitCode(formData)
      ElMessage.success('代码提交成功')
      // 重置表单
      handleReset()
      // 刷新列表
      loadSubmissions()
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      codeSubmissionStore.setError(errorMessage)
      ElMessage.error(`代码提交失败: ${errorMessage}`)
      console.error('代码提交失败:', error)
    }
  }

  // 重置表单
  const handleReset = () => {
    formData.studentId = ''
    formData.name = ''
    formData.description = ''
    if (formRef.value) {
      formRef.value.resetFields()
    }
  }

  // 编辑提交记录
  const handleEdit = (row: CodeSubmission) => {
    currentSubmissionId.value = row.id
    editFormData.studentId = row.studentId
    editFormData.name = row.name
    editFormData.description = row.description
    dialogVisible.value = true
  }

  // 提交编辑
  const handleEditSubmit = async () => {
    if (!editFormRef.value) return

    try {
      await editFormRef.value.validate()
      await updateCodeSubmission(currentSubmissionId.value, editFormData)
      ElMessage.success('编辑成功')
      dialogVisible.value = false
      // 刷新列表
      loadSubmissions()
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      codeSubmissionStore.setError(errorMessage)
      ElMessage.error(`编辑失败: ${errorMessage}`)
      console.error('编辑失败:', error)
    }
  }

  // 删除提交记录
  const handleDelete = async (id: number) => {
    try {
      await ElMessageBox.confirm('确定要删除这条提交记录吗？', '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      await deleteCodeSubmission(id)
      // 从store中删除记录
      codeSubmissionStore.removeSubmission(id)
      ElMessage.success('删除成功')
      // 刷新列表
      loadSubmissions()
    } catch (error) {
      // 用户取消删除时不显示错误信息
      if (error !== 'cancel') {
        const errorMessage = error instanceof Error ? error.message : '未知错误'
        codeSubmissionStore.setError(errorMessage)
        ElMessage.error(`删除失败: ${errorMessage}`)
        console.error('删除失败:', error)
      }
    }
  }

  // 分页大小变化
  const handleSizeChange = (size: number) => {
    pagination.size = size
    loadSubmissions()
  }

  // 分页当前页变化
  const handleCurrentChange = (current: number) => {
    pagination.current = current
    loadSubmissions()
  }

  // 初始化加载数据
  loadSubmissions()
</script>

<style scoped>
  .code-submission-container {
    padding: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
</style>
