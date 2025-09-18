<template>
  <footer class="common-footer">
    <div class="footer-content">
      <div class="footer-info">
        <h3>智环学创融合协会</h3>
        <p>告别单机学习！组队升级打怪！你的技术合伙人都在这里💥</p>
      </div>
      <div class="footer-links">
        <router-link to="/privacy-policy" class="footer-link"
          >隐私政策</router-link
        >
        <router-link to="/terms-of-service" class="footer-link"
          >服务条款</router-link
        >
        <a
          href="#"
          @click.prevent="showWarning('牢弟点一点下面一行')"
          class="footer-link"
          >联系我们</a
        >
        <router-link to="/admin/login" class="footer-link admin-login-link"
          >管理员登录</router-link
        >
      </div>

      <!-- 社交图标 -->
      <div style="display: flex; gap: 1.5rem; margin-bottom: 1rem">
        <!-- 微信 -->
        <a
          href="#"
          @click.prevent="showWarning('还没有微信')"
          style="color: #9ca3af"
        >
          <font-awesome-icon
            :icon="['fab', 'weixin']"
            class="fa-weixin"
            style="font-size: 1.5rem"
          />
        </a>
        <!-- QQ群 -->
        <a
          href="https://qm.qq.com/q/KiJWZAQ1C6"
          style="color: #9ca3af"
          target="_blank"
          rel="noopener noreferrer"
        >
          <font-awesome-icon
            :icon="['fab', 'qq']"
            class="fa-qq"
            style="font-size: 1.5rem"
          />
        </a>
        <!-- github目前也没有 -->
        <a
          href="#"
          @click.prevent="showWarning('GitHub地址暂不公开')"
          style="color: #9ca3af"
        >
          <font-awesome-icon
            :icon="['fab', 'github']"
            class="fa-github"
            style="font-size: 1.5rem"
          />
        </a>
        <!-- 这里应该展示邮箱 -->
        <a href="mailto:Wenbin.Lo@outlook.com" style="color: #9ca3af">
          <font-awesome-icon
            :icon="['fas', 'envelope']"
            class="fa-envelope"
            style="font-size: 1.5rem"
          />
        </a>
      </div>
      <div class="footer-copyright">
        <p>&copy; 2025 智环慧新团队. 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
  import { showWarning } from '@/utils/notification'
</script>

<style scoped>
  .common-footer {
    background-color: #1e293b;
    color: white;
    padding: 1rem 0;
    /* margin-top: 3rem; */
  }

  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .footer-info {
    text-align: center;
    margin-bottom: 0.2rem;
  }

  .footer-info h3 {
    font-size: 1.5rem;
    margin-bottom: 0.5rem;
  }

  .footer-info p {
    color: #94a3b8;
    font-size: 1rem;
  }

  .footer-links {
    display: flex;
    gap: 2rem;
    margin-bottom: 0.5rem;
    flex-wrap: wrap;
    justify-content: center;
  }

  .footer-link {
    color: #e2e8f0;
    text-decoration: none;
    font-size: 1rem;
    transition: color 0.3s;
  }

  .footer-link:hover {
    color: #3b82f6;
  }

  .footer-copyright {
    text-align: center;
    color: #94a3b8;
    font-size: 0.875rem;
    position: relative;
  }

  /* 隐蔽的管理后台入口样式 */
  .admin-entry {
    position: absolute;
    right: 0;
    bottom: 0;
    color: #475569;
    font-size: 0.75rem;
    opacity: 0.3;
    transition: opacity 0.3s;
    text-decoration: none;
    padding: 0.25rem;
  }

  .admin-entry:hover {
    opacity: 0.7;
    color: #64748b;
  }

  /* 移动端适配 */
  @media (max-width: 768px) {
    .footer-links {
      /* flex-direction: column; */
      gap: 1rem;
      text-align: center;
    }

    .footer-info h3 {
      font-size: 1.25rem;
    }
  }

  /* 管理员登录链接样式 */
  .admin-login-link {
    color: #64748b;
    font-weight: 500;
  }

  .admin-login-link:hover {
    color: #3b82f6;
  }
</style>
