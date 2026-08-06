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
        label-width="0px"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            :disabled="loading"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            :disabled="loading"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberPassword" :disabled="loading">
            记住密码
          </el-checkbox>
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

<script setup>
import { Lock, User } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { login } from "@/services/adminService";
import { useAdminStore } from "@/stores/adminStore";

const router = useRouter();
const adminStore = useAdminStore();
const loginFormRef = ref(null);
const loginForm = reactive({
	username: "",
	password: "",
});
const rememberPassword = ref(false);
const loading = ref(false);
const rules = {
	username: [
		{ required: true, message: "请输入用户名", trigger: "blur" },
		{
			min: 3,
			max: 20,
			message: "用户名长度应在3-20个字符之间",
			trigger: "blur",
		},
	],
	password: [
		{ required: true, message: "请输入密码", trigger: "blur" },
		{ min: 5, message: "密码长度不能少于5位", trigger: "blur" },
		{ max: 20, message: "密码长度不能超过20位", trigger: "blur" },
	],
};

/**
 * 提交登录表单，并在状态写入完成后跳转后台首页。
 */
const handleLogin = async () => {
	loginFormRef.value?.validate(async (valid) => {
		if (valid) {
			if (loading.value) return;
			loading.value = true;
			try {
				const adminInfo = await login(loginForm);
				adminStore.login(adminInfo, adminInfo.token);

				if (rememberPassword.value) {
					localStorage.setItem("rememberedUsername", loginForm.username);
					localStorage.setItem("rememberedPassword", loginForm.password);
				} else {
					localStorage.removeItem("rememberedUsername");
					localStorage.removeItem("rememberedPassword");
				}

				ElMessage.success("登录成功，正在跳转...");

				await router.push("/admin/dashboard");
			} finally {
				loading.value = false;
			}
		}
	});
};

/**
 * 恢复用户选择保存的登录凭据。
 */
onMounted(() => {
	const rememberedUsername = localStorage.getItem("rememberedUsername");
	const rememberedPassword = localStorage.getItem("rememberedPassword");

	if (rememberedUsername && rememberedPassword) {
		loginForm.username = rememberedUsername;
		loginForm.password = rememberedPassword;
		rememberPassword.value = true;
	}
});
</script>

<style scoped>
  .login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f5f7fa;
    background-image: linear-gradient(120deg, #f5f7fa 0%, #e4e8f0 100%);
  }
  .login-card {
    width: 400px;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    overflow: hidden;
  }
  .card-header {
    display: flex;
    justify-content: center;
    font-size: 20px;
    font-weight: bold;
    color: #333;
    padding: 16px 0;
  }
  .login-form {
    padding: 30px 20px;
  }
  .login-btn {
    width: 100%;
    height: 40px;
    font-size: 16px;
    margin-top: 10px;
  }
  @media (max-width: 768px) {
    .login-container {
      padding: 16px;
      box-sizing: border-box;
    }
    .login-card {
      width: 100%;
      max-width: 400px;
    }
  }
</style>
