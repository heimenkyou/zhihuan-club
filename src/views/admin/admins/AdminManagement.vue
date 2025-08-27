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

      <el-table :data="adminsList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="180" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            {{ scope.row.role === "super" ? "超级管理员" : "普通管理员" }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
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
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑管理员对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form
        ref="adminFormRef"
        :model="adminForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item prop="username">
          <el-input v-model="adminForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item prop="password" v-if="!editingAdminId">
          <el-input
            v-model="adminForm.password"
            type="password"
            placeholder="请输入密码"
          />
        </el-form-item>
        <el-form-item prop="newPassword" v-if="editingAdminId">
          <el-input
            v-model="adminForm.newPassword"
            type="password"
            placeholder="请输入新密码(选填)"
          />
        </el-form-item>
        <el-form-item prop="role" v-if="isSuperAdmin && !editingAdminId">
          <el-select v-model="adminForm.role" placeholder="请选择角色">
            <el-option label="超级管理员" value="super" />
            <el-option label="普通管理员" value="normal" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElForm } from "element-plus"
import { useAdminStore } from "../../../stores/adminStore"
import {
  getAdmins,
  createAdmin,
  deleteAdmin,
  updateAdmin,
  type Admin,
} from "../../../services/adminService"

const adminStore = useAdminStore()
const isSuperAdmin = adminStore.isSuperAdmin()
const adminsList = ref<Admin[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref("添加管理员")
const editingAdminId = ref<number | null>(null)
const adminFormRef = ref<InstanceType<typeof ElForm> | null>(null)

const adminForm = reactive({
  username: "",
  password: "",
  newPassword: "",
  role: "normal" as "super" | "normal",
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
  Object.keys(adminForm).forEach((key) => {
    if (key === "role") {
      adminForm[key as keyof typeof adminForm] = "normal"
    } else {
      adminForm[key as keyof typeof adminForm] = "super"
    }
  })
  adminForm.role = "normal"
  dialogVisible.value = true
}

// 处理编辑管理员
const handleEditAdmin = (admin: Admin) => {
  dialogTitle.value = "编辑管理员"
  editingAdminId.value = admin.id
  adminForm.username = admin.username
  adminForm.role = admin.role
  adminForm.password = ""
  adminForm.newPassword = ""
  dialogVisible.value = true
}

// 处理删除管理员
const handleDeleteAdmin = async (id: number) => {
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
          const params: Partial<Admin> = {
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

// 初始加载
onMounted(() => {
  loadAdminsList()
})
</script>

<style scoped>
.admin-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
