<template>
  <div class="admin-profile">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人资料</span>
          <el-button type="primary" @click="handleEdit" v-if="!isEditing">
            <el-icon><i-ep-edit /></el-icon>
            编辑资料
          </el-button>
          <div v-else class="edit-actions">
            <el-button @click="cancelEdit">取消</el-button>
            <el-button type="primary" @click="saveProfile" :loading="saving">
              保存
            </el-button>
          </div>
        </div>
      </template>
      
      <el-form
        ref="profileFormRef"
        :model="profileForm"
        :rules="profileRules"
        label-width="100px"
        v-loading="loading"
      >
        <el-form-item label="用户名">
          <el-input v-model="profileForm.username" :disabled="!isEditing" />
        </el-form-item>
        
        <el-form-item label="角色">
          <el-tag :type="roleTagType">
            {{ roleText }}
          </el-tag>
        </el-form-item>
        
        <el-form-item label="创建时间">
          <el-input :value="formatDate(profileForm.createTime)" disabled />
        </el-form-item>
        
        <el-form-item label="更新时间">
          <el-input :value="formatDate(profileForm.updateTime)" disabled />
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 修改密码卡片 -->
    <el-card class="password-card">
      <template #header>
        <span>修改密码</span>
      </template>
      
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="120px"
      >
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
            placeholder="请输入新密码"
          />
        </el-form-item>
        
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入新密码"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="changePassword" :loading="changingPassword">
            修改密码
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAdminStore } from '@/stores/adminStore'
import { updateCurrentAdmin } from '@/services/adminService'

const adminStore = useAdminStore()
const loading = ref(false)
const saving = ref(false)
const changingPassword = ref(false)
const isEditing = ref(false)

const profileFormRef = ref(null)
const passwordFormRef = ref(null)

const profileForm = ref({
  id: 0,
  username: '',
  role: '',
  createTime: '',
  updateTime: '',
})

const originalProfile = ref({
  id: 0,
  username: '',
  role: '',
  createTime: '',
  updateTime: '',
})

const passwordForm = ref({
  newPassword: '',
  confirmPassword: '',
})

const profileRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    {
      min: 3,
      max: 20,
      message: '用户名长度应在3-20个字符之间',
      trigger: 'blur',
    },
  ],
}

const passwordRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

const roleTagType = computed(() => {
  return profileForm.value.role === 'super' ? 'danger' : 'primary'
})

const roleText = computed(() => {
  return profileForm.value.role === 'super' ? '超级管理员' : '管理员'
})

/**
 * 格式化时间字段，避免后台原始时间字符串直接暴露给界面。
 *
 * @param {string} dateString
 * @returns {string}
 */
const formatDate = dateString => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

/**
 * 加载当前管理员资料；若 store 为空则主动向后端补拉一次。
 */
const loadProfile = async () => {
  loading.value = true

  if (!adminStore.userInfo) {
    try {
      await adminStore.fetchUserInfo()
    } catch (error) {
      loading.value = false
      throw error
    }
  }

  if (adminStore.userInfo) {
    profileForm.value = {
      id: adminStore.userInfo.id,
      username: adminStore.userInfo.username,
      role: adminStore.userInfo.role,
      createTime: adminStore.userInfo.createTime,
      updateTime: adminStore.userInfo.updateTime,
    }
    originalProfile.value = { ...profileForm.value }
  }

  loading.value = false
}

/**
 * 进入编辑态，允许当前管理员修改用户名。
 */
const handleEdit = () => {
  isEditing.value = true
}

/**
 * 放弃本次编辑并恢复到加载时的原始资料。
 */
const cancelEdit = () => {
  profileForm.value = { ...originalProfile.value }
  isEditing.value = false
  profileFormRef.value?.clearValidate()
}

/**
 * 保存个人资料，并同步更新 store 中的用户名显示。
 */
const saveProfile = async () => {
  if (!profileFormRef.value) return

  await profileFormRef.value.validate()
  saving.value = true

  try {
    await updateCurrentAdmin({
      username: profileForm.value.username,
    })

    ElMessage.success('个人资料更新成功')
    originalProfile.value = { ...profileForm.value }
    isEditing.value = false

    if (adminStore.userInfo) {
      adminStore.userInfo.username = profileForm.value.username
    }
  } finally {
    saving.value = false
  }
}

/**
 * 修改当前管理员密码，并复用现有更新管理员接口落库。
 */
const changePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate()

  await ElMessageBox.confirm('确定要修改密码吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })

  changingPassword.value = true

  try {
    await updateCurrentAdmin({
      username: profileForm.value.username,
      password: passwordForm.value.newPassword,
    })

    ElMessage.success('密码修改成功')
    passwordFormRef.value.resetFields()
  } finally {
    changingPassword.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.admin-profile {
  padding: 20px;
  max-width: 760px;
  margin: 0 auto;
}

.profile-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-actions {
  display: flex;
  gap: 10px;
}

.password-card {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .admin-profile {
    padding: 10px;
    max-width: none;
  }
  
  .card-header {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
}
</style>
