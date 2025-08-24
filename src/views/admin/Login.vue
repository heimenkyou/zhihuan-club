<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>管理员登录</span>
        </div>
      </template>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from "vue"
import { ElMessage } from "element-plus"
import { useRouter } from "vue-router"
import { useAdminStore } from "../../stores/adminStore"

const router = useRouter()
const adminStore = useAdminStore()
// 创建表单引用
import { ElForm } from "element-plus"
const loginFormRef = ref<InstanceType<typeof ElForm> | null>(null)
const loginForm = reactive<{ username: string; password: string }>({
  username: "",
  password: "",
})
const loading = ref(false)
const rules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, message: "密码长度不能少于5位", trigger: "blur" }, // 修改为5位
  ],
}

const handleLogin = async () => {
  // 验证表单
  loginFormRef.value?.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 实际项目中应调用真实的登录API
        // 这里为了演示，假设用户名和密码都是'admin'
        if (loginForm.username === "admin" && loginForm.password === "admin") {
          adminStore.login({ username: loginForm.username })
          ElMessage.success("登录成功")
          router.push("/admin/dashboard")
        } else {
          ElMessage.error("用户名或密码错误")
        }
      } catch (error) {
        ElMessage.error("登录失败，请重试")
        console.error("Login error:", error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}
.login-card {
  width: 400px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.card-header {
  display: flex;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
}
.login-form {
  padding: 20px;
}
.login-btn {
  width: 100%;
}
</style>
