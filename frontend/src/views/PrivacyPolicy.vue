<template>
  <div class="policy-container">
    <el-main class="policy-content">
      <div class="policy-header">
        <div
          class="flex justify-between items-center w-full h-12 border-b px-4"
        >
          <button
            @click="navigateToHome"
            class="text-dark py-2"
            aria-label="返回首页"
          >
            <font-awesome-icon :icon="['fas', 'arrow-left']" class="fa-arrow-left" />
          </button>
          <h1 class="text-xl font-bold">隐私政策</h1>
          <div class="w-8"></div>
          <!-- 占位元素，确保标题居中 -->
        </div>

        <p class="update-date">更新日期：2025年9月3日</p>
      </div>

      <div class="policy-body">
        <section>
          <h2>一、引言</h2>
          <p>
            欢迎使用我们的学习交流平台（以下简称"本平台"）。我们非常重视您的隐私保护，并会尽力保护您的个人信息安全。本隐私政策旨在向您说明我们如何收集、使用、存储和保护您的个人信息，以及您享有的相关权利。
          </p>
        </section>

        <section>
          <h2>二、信息收集</h2>
          <p>
            在您使用本平台的留言功能时，我们会自动采集您的IP地址。我们仅在您提交留言时采集该信息，不会在您浏览平台时收集任何个人信息。
          </p>
        </section>

        <section>
          <h2>三、IP地址的用途</h2>
          <p>我们采集IP地址的目的是：</p>
          <ul>
            <li>用于区分不同的留言用户，确保留言功能正常运行</li>
            <li>识别和防范恶意留言行为，维护平台秩序</li>
            <li>保障留言内容的真实性和有效性</li>
          </ul>
        </section>

        <section>
          <h2>四、信息存储</h2>
          <p>
            我们仅保留IP地址至留言被删除或平台关闭时为止，不会将其用于其他用途。我们采取严格的安全措施保护您的信息不被未经授权的访问、使用或泄露。
          </p>
        </section>

        <section>
          <h2>五、信息共享</h2>
          <p>
            我们承诺不会将您的IP地址泄露给任何第三方，除非遵守法律法规要求、保护本平台的合法权益或应政府部门的合法要求。
          </p>
        </section>

        <section>
          <h2>六、用户权利</h2>
          <p>
            您有权了解我们收集的关于您的信息，有权要求我们更正或删除不准确的信息。如您有任何疑问或需求，请联系我们。
          </p>
        </section>

        <section>
          <h2>七、政策更新</h2>
          <p>
            我们可能会不时更新本隐私政策。更新后的政策将在平台上公布，建议您定期查阅。
          </p>
        </section>

        <section>
          <h2>八、联系我们</h2>
          <p>如您对本隐私政策有任何疑问，请通过以下方式联系我们：</p>
          <p>邮箱：Wenbin.Lo@outlook.com</p>
          <!-- <a
              href="https://qm.qq.com/q/KiJWZAQ1C6"
              target="_blank"
              rel="noopener noreferrer"
              class="join-link-btn bg-primary hover:bg-primary-dark text-white font-medium px-6 py-2.5 rounded-lg transition-colors flex items-center gap-2"
            >
              <font-awesome-icon :icon="['fas', 'external-link']" class="fa-external-link" /> 直接加群
            </a> -->
          <p>
            <a
              href="https://qm.qq.com/q/KiJWZAQ1C6"
              target="_blank"
              rel="noopener noreferrer"
            >
              <font-awesome-icon :icon="['fas', 'external-link']" class="fa-external-link" /> QQ群：967370226
            </a>
          </p>
        </section>
      </div>
    </el-main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

/**
 * 返回首页，避免条款页形成无出口的独立浏览路径。
 */
const navigateToHome = () => {
  router.push('/')
}
</script>

<style scoped>
  .policy-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }

  .policy-content {
    flex: 1;
    padding: 2rem;
    max-width: 1200px;
    margin: 0 auto;
    width: 100%;
  }

  .policy-header {
    text-align: center;
    margin-bottom: 3rem;
    padding-top: 0;
  }

  .policy-header h1 {
    font-size: 1.5rem;
    color: #1e293b;
    margin: 0;
  }

  .update-date {
    color: #64748b;
    font-size: 1rem;
    margin-top: 1rem;
  }

  .policy-body {
    background: white;
    border-radius: 0.75rem;
    padding: 2rem;
    box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
  }

  section {
    margin-bottom: 2.5rem;
  }

  section h2 {
    font-size: 1.5rem;
    color: #1e293b;
    margin-bottom: 1rem;
    padding-bottom: 0.5rem;
    border-bottom: 2px solid #e2e8f0;
  }

  section p {
    margin-bottom: 1rem;
    line-height: 1.8;
    color: #475569;
  }

  section ul {
    list-style-type: disc;
    padding-left: 2rem;
    margin-bottom: 1rem;
  }

  section li {
    margin-bottom: 0.5rem;
    line-height: 1.8;
    color: #475569;
  }

  /* 移动端适配 */
  @media (max-width: 768px) {
    .policy-content {
      padding: 1rem;
    }

    .policy-header h1 {
      font-size: 2rem;
    }

    .policy-body {
      padding: 1.5rem;
    }

    section h2 {
      font-size: 1.35rem;
    }
  }
</style>
