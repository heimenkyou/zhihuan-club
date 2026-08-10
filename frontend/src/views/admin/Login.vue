<template>
  <main class="login-page">
    <aside class="brand-panel" aria-label="智环学创融合协会">
      <router-link to="/" class="brand-mark" aria-label="返回协会首页">
        <span class="brand-icon"><i-fa6-solid-code /></span>
        <span>智环学创融合协会</span>
      </router-link>

      <div class="brand-copy">
        <p class="brand-eyebrow">ZHihuan Innovation Club</p>
        <h1>把想法写成<br />真实的作品。</h1>
        <p class="brand-description">在这里动手、交流，与伙伴一起把每一次尝试变成进步。</p>
      </div>

      <div class="code-scene" aria-hidden="true">
        <div class="scene-glow"></div>
        <div class="code-window">
          <div class="window-bar"><span></span><span></span><span></span></div>
          <div class="code-lines">
            <i></i><i></i><i></i><i></i><i></i>
          </div>
          <div class="code-symbols">&lt; / &gt;</div>
        </div>
        <div class="orbit orbit-one"></div>
        <div class="orbit orbit-two"></div>
        <span class="scene-dot dot-one"></span>
        <span class="scene-dot dot-two"></span>
        <span class="scene-dot dot-three"></span>
      </div>

      <p class="brand-footer">智环学创融合协会 · 管理后台</p>
    </aside>

    <section class="login-panel">
      <div class="login-card">
        <div class="mobile-brand">
          <span class="brand-icon"><i-fa6-solid-code /></span>
          <span>智环学创融合协会</span>
        </div>
        <p class="login-eyebrow">ADMIN CONSOLE</p>
        <h2>欢迎回来</h2>
        <p class="login-subtitle">登录以继续访问您的账户</p>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="rules"
          class="login-form"
          label-width="0px"
          label-position="top"
        >
          <el-form-item prop="username" label="用户名">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              :disabled="loading"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              :disabled="loading"
              size="large"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <div class="form-options">
            <el-checkbox v-model="rememberPassword" :disabled="loading">记住密码</el-checkbox>
          </div>
          <el-button
            type="primary"
            class="login-btn"
            @click="handleLogin"
            :loading="loading"
          >
            登录后台
          </el-button>
          <router-link to="/" class="back-home">
            <i-fa6-solid-arrow-left />
            返回首页
          </router-link>
        </el-form>
      </div>
    </section>
  </main>
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
  .login-page {
    display: grid;
    grid-template-columns: minmax(420px, 44%) 1fr;
    min-height: 100vh;
    background: #f6f8fc;
  }

  .brand-panel {
    position: relative;
    display: flex;
    min-height: 100vh;
    flex-direction: column;
    overflow: hidden;
    padding: 44px clamp(40px, 6vw, 88px);
    color: #fff;
    background: linear-gradient(148deg, #172554 0%, #1d4ed8 53%, #38bdf8 135%);
  }

  .brand-panel::before,
  .brand-panel::after {
    position: absolute;
    border: 1px solid rgb(255 255 255 / 14%);
    border-radius: 50%;
    content: "";
  }

  .brand-panel::before { width: 580px; height: 580px; top: -280px; right: -260px; }
  .brand-panel::after { width: 460px; height: 460px; bottom: -285px; left: -250px; }

  .brand-mark, .mobile-brand { display: inline-flex; align-items: center; gap: 10px; font-size: 17px; font-weight: 700; color: inherit; }
  .brand-icon { display: inline-flex; align-items: center; justify-content: center; width: 32px; height: 32px; border-radius: 9px; background: rgb(255 255 255 / 16%); }
  .brand-copy { position: relative; z-index: 1; margin-top: clamp(105px, 16vh, 175px); }
  .brand-eyebrow, .login-eyebrow { margin: 0 0 16px; font-size: 11px; font-weight: 700; letter-spacing: .16em; }
  .brand-eyebrow { color: #bfdbfe; }
  .brand-copy h1 { margin: 0; font-size: clamp(38px, 3.5vw, 58px); line-height: 1.2; letter-spacing: -.04em; }
  .brand-description { max-width: 330px; margin: 22px 0 0; color: #dbeafe; font-size: 16px; line-height: 1.8; }
  .brand-footer { position: relative; z-index: 1; margin: auto 0 0; color: rgb(219 234 254 / 75%); font-size: 13px; }

  .code-scene { position: absolute; right: clamp(32px, 6vw, 90px); bottom: 64px; width: min(430px, 72%); height: 310px; }
  .scene-glow { position: absolute; inset: 45px 4% 0; border-radius: 50%; background: radial-gradient(ellipse, rgb(125 211 252 / 32%) 0%, transparent 67%); filter: blur(12px); }
  .code-window { position: absolute; z-index: 1; right: 7%; bottom: 25px; width: 78%; height: 190px; padding: 20px 22px; border: 1px solid rgb(255 255 255 / 26%); border-radius: 18px; background: linear-gradient(135deg, rgb(30 58 138 / 82%), rgb(15 23 42 / 55%)); box-shadow: 0 24px 52px rgb(15 23 42 / 27%); backdrop-filter: blur(8px); transform: rotate(-5deg); }
  .window-bar { display: flex; gap: 6px; }
  .window-bar span { width: 7px; height: 7px; border-radius: 50%; background: #93c5fd; }
  .window-bar span:nth-child(2) { opacity: .7; }
  .window-bar span:nth-child(3) { opacity: .4; }
  .code-lines { display: grid; gap: 12px; margin-top: 27px; }
  .code-lines i { display: block; height: 7px; border-radius: 99px; background: linear-gradient(90deg, #7dd3fc 0 38%, rgb(255 255 255 / 22%) 38% 62%, transparent 62%); }
  .code-lines i:nth-child(2) { width: 72%; background: linear-gradient(90deg, #c4b5fd 0 35%, rgb(255 255 255 / 22%) 35% 68%, transparent 68%); }
  .code-lines i:nth-child(3) { width: 88%; }
  .code-lines i:nth-child(4) { width: 58%; background: linear-gradient(90deg, #fcd34d 0 38%, rgb(255 255 255 / 22%) 38% 72%, transparent 72%); }
  .code-lines i:nth-child(5) { width: 76%; }
  .code-symbols { position: absolute; right: -35px; bottom: -30px; display: grid; width: 82px; height: 82px; place-items: center; border-radius: 24px; background: #fff; box-shadow: 0 14px 28px rgb(15 23 42 / 18%); color: #2563eb; font-family: ui-monospace, monospace; font-size: 21px; font-weight: 700; transform: rotate(9deg); }
  .orbit { position: absolute; border: 1px solid rgb(186 230 253 / 28%); border-radius: 50%; }
  .orbit-one { width: 250px; height: 250px; left: 0; bottom: 0; }
  .orbit-two { width: 355px; height: 355px; right: -50px; top: -72px; }
  .scene-dot { position: absolute; width: 9px; height: 9px; border-radius: 50%; background: #bae6fd; box-shadow: 0 0 20px #7dd3fc; }
  .dot-one { top: 56px; left: 11%; }.dot-two { top: 8px; right: 12%; }.dot-three { bottom: 5px; left: 29%; }

  .login-panel { display: flex; align-items: center; justify-content: center; padding: 48px 32px; background: radial-gradient(circle at 78% 17%, #dbeafe 0, transparent 27%), radial-gradient(circle at 18% 83%, #e0f2fe 0, transparent 26%), #f8fafc; }
  .login-card { width: min(100%, 420px); padding: 12px; }
  .mobile-brand { display: none; color: #1e3a8a; }
  .login-eyebrow { color: #2563eb; }
  .login-card h2 { margin: 0; color: #172554; font-size: 34px; line-height: 1.25; letter-spacing: -.03em; }
  .login-subtitle { margin: 12px 0 35px; color: #64748b; font-size: 15px; }
  .login-form :deep(.el-form-item) { margin-bottom: 23px; }
  .login-form :deep(.el-form-item__label) { padding: 0 0 8px; color: #334155; font-size: 14px; font-weight: 600; line-height: 1.4; }
  .login-form :deep(.el-input__wrapper) { min-height: 47px; padding: 1px 14px; border: 1px solid #dbe3ef; border-radius: 9px; background: #fff; box-shadow: none; transition: border-color .2s, box-shadow .2s; }
  .login-form :deep(.el-input__wrapper:hover), .login-form :deep(.el-input.is-focus .el-input__wrapper) { border-color: #60a5fa; box-shadow: 0 0 0 3px rgb(59 130 246 / 10%); }
  .form-options { display: flex; align-items: center; margin: 1px 0 30px; }
  .form-options :deep(.el-checkbox__label) { color: #64748b; }
  .back-home { display: flex; align-items: center; justify-content: center; gap: 7px; width: fit-content; margin: 27px auto 0; color: #64748b; font-size: 14px; font-weight: 500; transition: color .2s; }
  .back-home :deep(svg) { font-size: 12px; }
  .back-home:hover { color: #1d4ed8; }
  .login-btn { width: 100%; height: 48px; border-radius: 9px; font-size: 16px; font-weight: 600; box-shadow: 0 10px 18px rgb(37 99 235 / 22%); }

  @media (max-width: 900px) {
    .login-page { grid-template-columns: minmax(310px, 39%) 1fr; }
    .brand-panel { padding: 36px; }
    .brand-copy { margin-top: 120px; }
    .code-scene { right: 18px; width: 90%; }
  }
  @media (max-width: 680px) {
    .login-page { display: block; min-height: 100dvh; }
    .brand-panel { display: none; }
    .login-panel { min-height: 100dvh; padding: 32px 24px; background: radial-gradient(circle at 88% 4%, #dbeafe 0, transparent 27%), #f8fafc; }
    .login-card { padding: 0; }
    .mobile-brand { display: inline-flex; margin-bottom: 72px; font-size: 16px; }
    .mobile-brand .brand-icon { background: #dbeafe; }
    .login-card h2 { font-size: 31px; }
  }
</style>
