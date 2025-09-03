<template>
  <el-dialog 
    v-model="visible" 
    :close-on-click-modal="false" 
    :close-on-press-escape="false"
    title="请阅读并同意以下协议后使用"
    class="policy-modal"
    width="80%"
    center
  >
    <div class="policy-modal-content">
      <p class="policy-description">
        为了保障您的权益，请您在使用本网站前认真阅读并同意以下协议。
      </p>
      <div class="policy-links">
        <el-button 
          type="text" 
          @click="openPrivacyPolicy"
          class="policy-link-btn"
        >
          《隐私政策》
        </el-button>
        <span class="policy-separator">|</span>
        <el-button 
          type="text" 
          @click="openTermsOfService"
          class="policy-link-btn"
        >
          《网站服务条款》
        </el-button>
      </div>
      <p class="ip-notice">
        特别提示：使用本网站的留言功能时，我们会自动采集您的IP地址，详情请见《隐私政策》。
      </p>
    </div>
    <template #footer>
      <div class="policy-modal-footer">
        <el-button 
          type="primary" 
          size="large"
          @click="handleAgree"
          class="agree-btn"
        >
          全部同意
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const visible = ref(false);

// 监听组件挂载，检查是否已同意协议
onMounted(() => {
  const hasAgreed = localStorage.getItem('hasAgreedToPolicies');
  if (!hasAgreed) {
    visible.value = true;
  }
});

// 打开隐私政策页面
const openPrivacyPolicy = () => {
  router.push('/privacy-policy');
};

// 打开服务条款页面
const openTermsOfService = () => {
  router.push('/terms-of-service');
};

// 处理同意操作
const handleAgree = () => {
  localStorage.setItem('hasAgreedToPolicies', 'true');
  visible.value = false;
};
</script>

<style scoped>
.policy-modal {
  display: flex;
  align-items: center;
  justify-content: center;
}

.policy-modal .el-dialog__wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.policy-modal-content {
  text-align: center;
  padding: 1rem 0;
}

.policy-description {
  font-size: 1.1rem;
  color: #333;
  margin-bottom: 2rem;
}

.policy-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.policy-link-btn {
  font-size: 1.1rem;
  color: #3b82f6 !important;
  font-weight: 500;
  text-decoration: underline;
}

.policy-separator {
  color: #666;
}

.ip-notice {
  font-size: 0.9rem;
  color: #666;
  margin-top: 1rem;
}

.policy-modal-footer {
  display: flex;
  justify-content: center;
}

.agree-btn {
  padding: 0.75rem 3rem;
  font-size: 1.1rem;
  border-radius: 9999px;
  background-color: #3b82f6;
  border: none;
}

.agree-btn:hover {
  background-color: #2563eb;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .policy-modal {
    width: 95% !important;
  }
  
  .policy-links {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .policy-separator {
    display: none;
  }
  
  .agree-btn {
    padding: 0.75rem 2rem;
    width: 100%;
  }
}
</style>