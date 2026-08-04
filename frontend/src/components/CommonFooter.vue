<template>
  <footer class="common-footer">
    <div class="footer-content">
      <div class="footer-info">
        <h3>智环学创融合协会</h3>
        <p>和一群愿意动手的人，把兴趣做成作品💥</p>
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
          @click.prevent="showWarning('点下面一行图标就行了')"
          class="footer-link"
          >联系我们</a
        >
        <router-link to="/admin/dashboard" class="footer-link admin-login-link"
          >管理员登录</router-link
        >
      </div>

      <!-- 社交图标 -->
      <div class="social-links">
        <!-- 微信 -->
        <a
          href="#"
          @click.prevent="showWarning('还没有微信')"
          class="social-link"
        >
          <i-fa6-brands-weixin class="fa-weixin social-icon" />
        </a>
        <!-- QQ群 -->
        <a
          href="https://qm.qq.com/q/uEOXqCEEVO"
          class="social-link"
          target="_blank"
          rel="noopener noreferrer"
        >
          <i-fa6-brands-qq class="fa-qq social-icon" />
        </a>
        <!-- github目前也没有 -->
        <a
          href="#"
          @click.prevent="showWarning('GitHub地址暂不公开')"
          class="social-link"
        >
          <i-fa6-brands-github class="fa-github social-icon" />
        </a>
        <!-- 这里应该展示邮箱 -->
        <a href="mailto:Wenbin.Lo@outlook.com" class="social-link">
          <i-fa6-solid-envelope class="fa-envelope social-icon" />
        </a>
      </div>
      <div class="footer-copyright">
        <p>&copy; 2025 智环慧新团队. 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
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

  .social-links {
    display: flex;
    gap: 1.5rem;
    margin-bottom: 1rem;
  }

  .social-link {
    color: #9ca3af;
  }

  .social-icon {
    font-size: 1.5rem;
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
