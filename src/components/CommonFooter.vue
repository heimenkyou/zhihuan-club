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
        <a href="#" class="footer-link">联系我们</a>
      </div>

      <!-- 社交图标 -->
      <div style="display: flex; gap: 1.5rem; margin-bottom: 1rem">
        <!-- 微信 -->
        <a
          href="#"
          @click.prevent="showWarning('还没有微信')"
          style="color: #9ca3af"
        >
          <i class="fa fa-weixin" style="font-size: 1.5rem"></i>
        </a>
        <!-- QQ群 -->
        <a
          href="https://qm.qq.com/q/KiJWZAQ1C6"
          style="color: #9ca3af"
          target="_blank"
          rel="noopener noreferrer"
        >
          <i class="fa fa-qq" style="font-size: 1.5rem"></i>
        </a>
        <!-- github目前也没有 -->
        <a
          href="#"
          @click.prevent="showWarning('GitHub地址暂不公开')"
          style="color: #9ca3af"
        >
          <i class="fa fa-github" style="font-size: 1.5rem"></i>
        </a>
        <!-- 这里应该展示邮箱 -->
        <a href="mailto:Wenbin.Lo@outlook.com" style="color: #9ca3af">
          <i class="fa fa-envelope" style="font-size: 1.5rem"></i>
        </a>
      </div>
      <div class="footer-copyright">
        <p>&copy; 2024-2025 智环学创融合协会. 保留所有权利.</p>
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
</style>
