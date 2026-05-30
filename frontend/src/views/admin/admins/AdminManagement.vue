<template>
  <div class="admin-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>管理员管理</span>
          <el-button
            v-if="isSuperAdmin"
            type="primary"
            size="small"
            @click="handleAddAdmin"
          >
            添加管理员
          </el-button>
        </div>
      </template>

      <!-- 移动端适配：使用响应式表格 -->
      <div class="table-container">
        <el-table 
          :data="adminsList" 
          style="width: 100%"
          :class="{ 'mobile-table': isMobile }"
        >
          <el-table-column prop="id" label="ID" :width="isMobile ? 60 : 80" />
          <el-table-column prop="username" label="用户名" :width="isMobile ? 120 : 180" />
          <el-table-column prop="role" label="角色" :width="isMobile ? 100 : 100">
            <template #default="scope">
              <span class="role-tag" :class="scope.row.role">
                {{ scope.row.role === "super" ? "超级管理员" : "普通管理员" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" :width="isMobile ? 150 : 180" />
          <el-table-column label="操作" :width="isMobile ? 150 : 200" fixed="right">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleEditAdmin(scope.row)"
                >
                  编辑
                </el-button>
                <el-button
                  v-if="isSuperAdmin && scope.row.role !== 'super'"
                  type="danger"
                  size="small"
                  @click="handleDeleteAdmin(scope.row.id)"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 添加/编辑管理员对话框 - 移动端适配 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      :width="isMobile ? '90%' : '500px'"
      :fullscreen="isMobile"
    >
      <el-form
        ref="adminFormRef"
        :model="adminForm"
        :rules="rules"
        label-width="100px"
        :label-position="isMobile ? 'top' : 'right'"
      >
        <el-form-item prop="username" :label="isMobile ? '用户名' : undefined">
          <el-input 
            v-model="adminForm.username" 
            placeholder="请输入用户名" 
            :clearable="isMobile"
          />
        </el-form-item>
        <el-form-item 
          prop="password" 
          v-if="!editingAdminId"
          :label="isMobile ? '密码' : undefined"
        >
          <el-input
            v-model="adminForm.password"
            type="password"
            placeholder="请输入密码"
            :show-password="isMobile"
          />
        </el-form-item>
        <el-form-item 
          prop="newPassword" 
          v-if="editingAdminId"
          :label="isMobile ? '新密码' : undefined"
        >
          <el-input
            v-model="adminForm.newPassword"
            type="password"
            placeholder="请输入新密码(选填)"
            :show-password="isMobile"
          />
        </el-form-item>
        <el-form-item 
          prop="role" 
          v-if="isSuperAdmin && !editingAdminId"
          :label="isMobile ? '角色' : undefined"
        >
          <el-select 
            v-model="adminForm.role" 
            placeholder="请选择角色"
            :style="{ width: '100%' }"
          >
            <el-option label="超级管理员" value="super" />
            <el-option label="普通管理员" value="normal" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" :size="isMobile ? 'large' : 'default'">
            取消
          </el-button>
          <el-button 
            type="primary" 
            @click="handleSubmit" 
            :size="isMobile ? 'large' : 'default'"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue"
import { ElMessage } from "element-plus"
import { useAdminStore } from "@/stores/adminStore"
import {
  getAdmins,
  createAdmin,
  deleteAdmin,
  updateAdmin,
} from "@/services/adminService"

const adminStore = useAdminStore()
const isSuperAdmin = adminStore.isSuperAdmin()
const adminsList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref("添加管理员")
const editingAdminId = ref(null)
const adminFormRef = ref(null)

// 检测是否为移动端
const isMobile = computed(() => {
  return window.innerWidth <= 768
})

const adminForm = reactive({
  username: "",
  password: "",
  newPassword: "",
  role: "normal",
})

const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, message: "用户名长度不能少于3位", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能少于6位", trigger: "blur" },
  ],
  newPassword: [{ min: 6, message: "密码长度不能少于6位", trigger: "blur" }],
}

// 加载管理员列表
const loadAdminsList = async () => {
  try {
    const data = await getAdmins()
    adminsList.value = data
  } catch (error) {
    ElMessage.error("加载管理员列表失败")
    console.error("加载管理员列表失败:", error)
  }
}

// 处理添加管理员
const handleAddAdmin = () => {
  dialogTitle.value = "添加管理员"
  editingAdminId.value = null
  // 重置表单
  adminForm.username = ""
  adminForm.password = ""
  adminForm.newPassword = ""
  adminForm.role = "normal"
  dialogVisible.value = true
}

// 处理编辑管理员
const handleEditAdmin = admin => {
  dialogTitle.value = "编辑管理员"
  editingAdminId.value = admin.id
  adminForm.username = admin.username
  adminForm.role = admin.role
  adminForm.password = ""
  adminForm.newPassword = ""
  dialogVisible.value = true
}

// 处理删除管理员
const handleDeleteAdmin = async id => {
  try {
    await deleteAdmin(id)
    ElMessage.success("删除管理员成功")
    loadAdminsList()
  } catch (error) {
    ElMessage.error("删除管理员失败")
    console.error("删除管理员失败:", error)
  }
}

// 处理提交表单
const handleSubmit = async () => {
  adminFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        if (editingAdminId.value) {
          // 更新管理员
          const params = {
            username: adminForm.username,
          }
          if (adminForm.newPassword) {
            params.password = adminForm.newPassword
          }
          await updateAdmin(editingAdminId.value, params)
          ElMessage.success("更新管理员成功")
        } else {
          // 添加管理员
          const newAdmin = {
            username: adminForm.username,
            password: adminForm.password,
            role: adminForm.role,
            createTime: new Date().toISOString(),
            updateTime: new Date().toISOString(),
          }
          await createAdmin(newAdmin)
          ElMessage.success("添加管理员成功")
        }
        dialogVisible.value = false
        loadAdminsList()
      } catch (error) {
        ElMessage.error(
          editingAdminId.value ? "更新管理员失败" : "添加管理员失败"
        )
        console.error("提交管理员表单失败:", error)
      }
    }
  })
}

// 监听窗口大小变化
const handleResize = () => {
  // 响应式处理会在computed中自动更新
}

onMounted(() => {
  loadAdminsList()
  window.addEventListener('resize', handleResize)
})

// 清理事件监听器
// 在Vue 3.5+中，可以使用onUnmounted
// onUnmounted(() => {
//   window.removeEventListener('resize', handleResize)
// })
</script>

<style scoped>
.admin-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.card-header span {
  font-size: 18px;
  font-weight: bold;
}

.table-container {
  overflow-x: auto;
}

/* 移动端表格样式 */
.mobile-table :deep(.el-table__row) {
  display: flex;
  flex-direction: column;
}

.mobile-table :deep(.el-table__cell) {
  display: flex;
  justify-content: space-between;
  padding: 8px 12px !important;
  border-bottom: 1px solid #eee;
}

.mobile-table :deep(.el-table__cell)::before {
  content: attr(label);
  font-weight: bold;
  margin-right: 10px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.action-buttons .el-button {
  margin: 0;
}

.role-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.role-tag.super {
  background-color: #fef0f0;
  color: #f56c6c;
}

.role-tag.normal {
  background-color: #ecf5ff;
  color: #409eff;
}

.dialog-footer {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.dialog-footer .el-button {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-management {
    padding: 10px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .card-header .el-button {
    width: 100%;
  }
  
  /* 表格在移动端的优化显示 */
  :deep(.el-table) {
    font-size: 14px;
  }
  
  :deep(.el-table th) {
    font-size: 14px;
  }
  
  :deep(.el-table td) {
    padding: 8px 0;
  }
}

@media (max-width: 480px) {
  .admin-management {
    padding: 5px;
  }
  
  :deep(.el-table th),
  :deep(.el-table td) {
    padding: 6px 4px;
    font-size: 12px;
  }
  
  .role-tag {
    font-size: 10px;
    padding: 2px 6px;
  }
}
</style>