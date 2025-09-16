<template>
  <div class="terms-container">
    <!-- <CommonNavbar /> -->
    <el-main class="terms-content">
      <div class="terms-header">
        <div
          class="flex justify-between items-center w-full h-16 border-b px-4"
        >
          <button
            @click="navigateToHome"
            class="text-dark py-2"
            aria-label="返回首页"
          >
            <font-awesome-icon :icon="['fas', 'arrow-left']" class="fa-arrow-left" />
          </button>
          <h1 class="text-xl font-bold">平台服务条款</h1>
          <div class="w-8"></div>
          <!-- 占位元素，确保标题居中 -->
        </div>

        <p class="update-date">更新日期：2025年9月3日</p>
      </div>

      <div class="terms-body">
        <section>
          <h2>一、总则</h2>
          <p>
            欢迎使用我们的学习交流平台（以下简称"本平台"）。使用本平台前，请您仔细阅读本服务条款。一旦您访问或使用本平台，即表示您已同意遵守本条款的全部内容。
          </p>
        </section>

        <section>
          <h2>二、服务范围</h2>
          <p>
            本平台为用户提供公开信息浏览及留言功能，无需注册即可使用。我们致力于为用户提供优质的学习资源和交流空间。
          </p>
        </section>

        <section>
          <h2>三、用户行为规范</h2>
          <p>
            用户在使用本平台时，必须遵守中华人民共和国法律法规及相关国际公约，不得从事以下行为：
          </p>
          <ul>
            <li>
              发布、传播任何违法、违规、低俗、暴力、恐怖、赌博、邪教等有害信息
            </li>
            <li>
              发布、传播侵犯他人知识产权、商业秘密、隐私权等合法权益的内容
            </li>
            <li>恶意刷屏、发布重复内容或干扰平台正常运营的行为</li>
            <li>伪造身份、冒充他人或进行其他欺诈行为</li>
            <li>其他违反法律法规、社会公德或损害本平台利益的行为</li>
          </ul>
        </section>

        <section>
          <h2>四、平台管理</h2>
          <p>为维护平台秩序和用户利益，我们有权采取以下措施：</p>
          <ul>
            <li>删除或屏蔽违规留言内容，无需提前通知</li>
            <li>限制或禁止违规用户使用平台功能</li>
            <li>对严重违规行为保留追究法律责任的权利</li>
          </ul>
        </section>

        <section>
          <h2>五、免责条款</h2>
          <p>
            本平台不对用户留言内容的真实性、准确性、完整性等承担责任。用户应自行对其发布的内容负责，因用户违规行为导致的任何后果由用户自行承担。
          </p>
          <p>
            对于因不可抗力、网络故障、系统故障等非本平台可控因素导致的服务中断或数据丢失，本平台不承担责任。
          </p>
        </section>

        <section>
          <h2>六、条款修改</h2>
          <p>
            我们有权随时修改本服务条款，修改后的条款将在平台上公布。继续使用本平台即表示您接受修改后的条款。
          </p>
        </section>

        <section>
          <h2>七、法律适用与争议解决</h2>
          <p>
            本条款的订立、执行、解释及争议的解决均适用中华人民共和国法律。如就本条款发生任何争议，双方应友好协商解决；协商不成的，任何一方均有权向有管辖权的人民法院提起诉讼。
          </p>
        </section>
      </div>
    </el-main>
  </div>
</template>

<script setup lang="ts">
// 导入必要的导航功能
import { useRouter } from "vue-router"

const router = useRouter()

// 添加导航到首页的方法
const navigateToHome = () => {
  router.push("/")
}
</script>

<style scoped>
.terms-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.terms-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.terms-header {
  text-align: center;
  margin-bottom: 3rem;
  padding-top: 0;
}

.terms-header h1 {
  font-size: 1.5rem;
  color: #1e293b;
  margin: 0;
}

.update-date {
  color: #64748b;
  font-size: 1rem;
  margin-top: 1rem;
}

.terms-body {
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
  .terms-content {
    padding: 1rem;
  }

  .terms-header h1 {
    font-size: 2rem;
  }

  .terms-body {
    padding: 1.5rem;
  }

  section h2 {
    font-size: 1.35rem;
  }
}
</style>
