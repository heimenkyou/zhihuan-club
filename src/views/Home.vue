<template>
  <div class="code-pulse-container">
    <!-- 导航栏 -->
    <!-- 使用新的导航栏组件 -->
    <CommonNavbar />

    <!-- 首页Banner -->
    <el-main class="home-banner" id="home">
      <!-- 粒子动画背景和代码流动画容器 -->
      <div id="particle-container" class="particle-bg"></div>
      <div id="code-flow" class="code-flow-bg"></div>

      <div class="banner-content">
        <div class="title-container">
          <h1 class="main-title">
            <span class="code-text">智环学创融合协会</span>
          </h1>
        </div>

        <p class="description">
          告别单机学习！告别单机学习，组队升级打怪！你的技术合伙人都在这里💥
        </p>

        <div class="action-buttons">
          <el-button
            @click="toabout"
            class="learn-more-btn"
            round
            style="
              font-weight: 600;
              font-size: 1.125rem;
              line-height: 1.75rem;
              padding: 1.45rem 1.75rem;
              background-color: white;
              color: rgb(59 130 246 / var(--tw-bg-opacity, 1));
              border-radius: 9999px;
              border: none;
              outline: none;
              overflow: hidden;
            "
          >
            了解更多
          </el-button>
          <el-button
            class="join-us-btn"
            @click="tojoin"
            round
            style="
              font-weight: 600;
              font-size: 1.125rem;
              line-height: 1.75rem;
              padding: 1.45rem 1.75rem;
              background-color: rgb(59 130 246 / var(--tw-bg-opacity, 1));
              color: white;
              border-radius: 9999px;
              border: none;
              outline: none;
              overflow: hidden;
            "
          >
            加入我们
          </el-button>
        </div>

        <!-- 向下滚动指示 -->
        <div class="scroll-down fixed-bottom flex-center">
          <el-link href="#about" :underline="false" class="text-center">
            <div
              style="
                display: flex;
                flex-direction: column;
                justify-content: center;
                font-weight: 500;
              "
              class="text-center"
            >
              <span
                class="text-center-text d-block mb-1"
                style="text-align: center; color: white; font-size: 16px"
                >向下滚动</span
              >
              <el-icon
                class="scroll-icon"
                style="margin: 0 auto; color: white; font-size: 24px"
              >
                <ArrowDown />
              </el-icon>
            </div>
          </el-link>
        </div>
      </div>
    </el-main>

    <!-- 社团简介 -->
    <el-main class="about-section" id="about">
      <div class="about-container">
        <div class="text-center mb-12">
          <h1 class="section-title">我们是谁？</h1>
          <div class="section-divider"></div>
        </div>

        <div class="about-content">
          <div class="about-text">
            <h3 class="about-subtitle">聊天记录:</h3>
            <p class="about-paragraph ">
              😭：我的专业二本毕业找不到好工作怎么呀？<br /><hr/>
              🥺：早就听说计算机寒冬了，我还有机会嘛？<br /><hr/>
              🤓👆：有的兄弟们，有的！为何不建立个工作室一块做实战项目呢？既能丰富个人简历就业，考研还有优势。<br /><hr/>
              🤩：我什么都不会你们会要我嘛？！<br /><hr/>
              😏: 当然啦，计算机专业：前端，后端，测试，运维，嵌入式开发<br />
              &emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;
              非计算机专业同学：PPT，写项目书，UI设计，视频剪辑，宣讲演讲。
            </p>
            <p class="about-paragraph border-t-2 border-black pt-5">
              我们的宗旨是：<span class="highlight-text"
                >自觉，自律,自信”，热爱可以超过一切❤️快来加入项目组</span
              >。
            </p>
          </div>

          <div class="about-image">
            <div class="image-decoration top-left"></div>
            <div class="image-decoration bottom-right"></div>
            <img
              src="https://picsum.photos/id/180/800/600"
              style="width: 100%; height: 100%"
            />

            <div
              class="image-badge"
              style="
                background: rgb(245 158 11 / var(--tw-bg-opacity, 1));
                transform: skew(-20deg);
              "
            >
              <div>
                <h4
                  class="badge-title"
                  style="
                    color: white;
                    font-size: 1.25rem;
                    transform: skew(20deg);
                  "
                >
                  期待你的加入
                </h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-main>
    <!-- 高光时刻 -->
    <el-main id="achievements" class="py-20 px-4 bg-gray-50">
      <div class="max-w-7xl mx-auto">
        <div class="text-center mb-16">
          <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold text-dark mb-4">
            我们的高光时刻
          </h2>
          <div class="w-20 h-1 bg-primary mx-auto rounded-full"></div>
          <p class="text-gray-600 mt-4 max-w-2xl mx-auto">
            2025年首次招新，期待与你一起创造属于我们的第一个高光时刻
          </p>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="flex justify-center items-center py-16">
          <el-loading-spinner size="large" />
          <span class="ml-4">加载项目中...</span>
        </div>

        <!-- 精选成果滚动区域 -->
        <div class="relative" v-else>
          <div class="overflow-x-auto pb-8 scrollbar-hide">
            <div class="flex gap-6 w-max px-2">
              <!-- 动态渲染项目卡片 -->
              <div
                v-for="(project, index) in projects"
                :key="project.id || index"
                class="w-[280px] md:w-[320px] bg-white rounded-xl shadow-lg overflow-hidden transform hover:-translate-y-2 transition-all duration-300"
              >
                <img
                  :src="project.coverImage ? project.coverImage.replace(/[`\s]/g, '') : ''"
                  :alt="project.title"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div class="p-6">
                  <div class="flex justify-between items-start mb-4">
                    <h3 class="text-xl font-bold text-dark">{{ project.title  }}</h3>
                  </div>
                  <p class="text-gray-600 mb-6 line-clamp-3">
                    {{ project.briefIntro  || '暂无项目简介' }}
                  </p>
                  <button
                    @click="goToProjectDetail(project.id)"
                    class="w-full bg-primary/10 text-primary hover:bg-primary hover:text-white transition-all px-4 py-2 rounded-lg font-medium"
                  >
                    了解项目
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 滚动指示器 -->
          <div
            class="absolute bottom-0 left-1/2 transform -translate-x-1/2 flex gap-2"
          >
            <div class="w-12 h-1 bg-primary rounded-full"></div>
            <div class="w-3 h-1 bg-gray-300 rounded-full"></div>
            <div class="w-3 h-1 bg-gray-300 rounded-full"></div>
          </div>
        </div>

        <!-- 查看更多按钮 -->
        <div class="flex flex-col sm:flex-row gap-4 justify-center mt-12">
          <button
            @click="toproject"
            class="bg-white text-primary border border-primary hover:bg-primary hover:text-white transition-all px-6 py-3 rounded-full font-semibold shadow hover:shadow-md"
          >
            查看更多项目
          </button>
          <button
            @click="toawards"
            class="bg-white text-primary border border-primary hover:bg-primary hover:text-white transition-all px-6 py-3 rounded-full font-semibold shadow hover:shadow-md"
          >
            查看更多奖项
          </button>
        </div>
      </div>
    </el-main>
    <!-- 热门赛事 -->
    <el-main class="competitions-section" id="competitions">
      <div style="max-width: 80rem; margin: 0 auto; padding: 5rem 1rem">
        <!-- 标题区 -->
        <div style="text-align: center; margin-bottom: 4rem">
          <h2
            style="
              font-size: clamp(1.5rem, 3vw, 2.5rem);
              font-weight: bold;
              color: #1e293b;
              margin-bottom: 1rem;
            "
          >
            我们征战的热门赛事
          </h2>
          <div
            style="
              width: 5rem;
              height: 0.25rem;
              background-color: #3b82f6;
              margin: 0 auto;
              border-radius: 9999px;
            "
          ></div>
        </div>

        <!-- 竞赛列表（行内Grid，简化响应式） -->
        <div
          style="
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 2rem;
          "
        >
          <!-- 竞赛1：蓝桥杯 -->
          <div
            style="
              background-color: #f9fafb;
              border-radius: 0.75rem;
              padding: 1.5rem;
              text-align: center;
            "
          >
            <div
              style="
                width: 6rem;
                height: 6rem;
                background-color: #eff6ff;
                border-radius: 9999px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 1.5rem;
              "
            >
              <i
                class="fa fa-code"
                style="font-size: 2.5rem; color: #3b82f6"
              ></i>
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              蓝桥杯
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              国内规模最大的算法竞赛之一，面向全国高校学生
            </p>
            <a href="https://www.lanqiao.cn/" style="color: #3b82f6; font-weight: 500"
              >了解更多
              <i class="fa fa-arrow-right" style="margin-left: 0.25rem"></i
            ></a>
          </div>

          <!-- 竞赛2：ACM-ICPC -->
          <div
            style="
              background-color: #f9fafb;
              border-radius: 0.75rem;
              padding: 1.5rem;
              text-align: center;
            "
          >
            <div
              style="
                width: 6rem;
                height: 6rem;
                background-color: #f3f0ff;
                border-radius: 9999px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 1.5rem;
              "
            >
              <i
                class="fa fa-trophy"
                style="font-size: 2.5rem; color: #6366f1"
              ></i>
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              ACM-ICPC
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              国际大学生程序设计竞赛，计算机领域的奥林匹克
            </p>
            <a href="https://icpc.global" style="color: #3b82f6; font-weight: 500"
              >了解更多
              <i class="fa fa-arrow-right" style="margin-left: 0.25rem"></i
            ></a>
          </div>

          <!-- 竞赛3：挑战杯 -->
          <div
            style="
              background-color: #f9fafb;
              border-radius: 0.75rem;
              padding: 1.5rem;
              text-align: center;
            "
          >
            <div
              style="
                width: 6rem;
                height: 6rem;
                background-color: #fee2e2;
                border-radius: 9999px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 1.5rem;
              "
            >
              <i
                class="fa fa-lightbulb-o"
                style="font-size: 2.5rem; color: #ef4444"
              ></i>
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              挑战杯
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              全国大学生课外学术科技作品竞赛，科技创新的舞台
            </p>
            <a href="www.tiaozhanbei.net" style="color: #3b82f6; font-weight: 500"
              >了解更多
              <i class="fa fa-arrow-right" style="margin-left: 0.25rem"></i
            ></a>
          </div>

          <!-- 竞赛4：计算机设计大赛 -->
          <div
            style="
              background-color: #f9fafb;
              border-radius: 0.75rem;
              padding: 1.5rem;
              text-align: center;
            "
          >
            <div
              style="
                width: 6rem;
                height: 6rem;
                background-color: #dcfce7;
                border-radius: 9999px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 1.5rem;
              "
            >
              <i
                class="fa fa-desktop"
                style="font-size: 2.5rem; color: #22c55e"
              ></i>
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              计算机设计大赛
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              培养创新能力，展示计算机应用成果的重要平台
            </p>
            <a href="http://jsjds.blcu.edu.cn" style="color: #3b82f6; font-weight: 500"
              >了解更多
              <i class="fa fa-arrow-right" style="margin-left: 0.25rem"></i
            ></a>
          </div>
        </div>

        <!-- 探索按钮（静态样式） -->
        <div style="text-align: center; margin-top: 3rem">
          <button
            @click="tocompetitions"
            style="
              background-color: #3b82f6;
              color: white;
              padding: 0.75rem 2rem;
              border-radius: 9999px;
              font-weight: 600;
              border: none;
              cursor: pointer;
            "
          >
            探索全部竞赛
          </button>
        </div>
      </div>
    </el-main>
    <!-- CTA 区域 -->
    <el-main
      class="cta-section"
      id="cta"
      style="position: relative; overflow: hidden; padding: 5rem 1rem"
    >
      <!-- 背景层 -->
      <div style="position: absolute; inset: 0; z-index: 0">
        <img
          src="https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?q=80&w=1740&auto=format&fit=crop"
          alt="团队合作"
          style="width: 100%; height: 100%; object-fit: cover; opacity: 0.3"
        />
        <div
          style="
            position: absolute;
            inset: 0;
            background: linear-gradient(to right, #3b82f6, #10b981);
          "
        ></div>
      </div>

      <!-- CTA 内容层 -->
      <div
        style="
          position: relative;
          z-index: 10;
          text-align: center;
          padding: 0 1rem;
          max-width: 48rem;
          margin: 0 auto;
        "
      >
        <h2
          style="
            font-size: clamp(1.5rem, 3vw, 2.5rem);
            font-weight: bold;
            color: white;
            margin-bottom: 1.5rem;
          "
        >
          期待你的代码，在这里闪耀
        </h2>
        <p style="color: white; font-size: 1.125rem; margin-bottom: 2.5rem">
          加入我们，与优秀的伙伴一起成长，挑战自我，创造精彩
        </p>
        <div
          style="
            display: flex;
            flex-direction: column;
            gap: 1.5rem;
            align-items: center;
          "
        >
          <button
            @click="tojoin"
            style="
              background-color: white;
              color: #3b82f6;
              padding: 1rem 2.5rem;
              border-radius: 9999px;
              font-weight: 600;
              font-size: 1.25rem;
              border: none;
              cursor: pointer;
              width: 280px;
            "
          >
            加入我们
          </button>
          <button
            @click="toMessageBoard"
            style="
              background-color: #8b5cf6;
              color: white;
              padding: 1rem 2.5rem;
              border-radius: 9999px;
              font-weight: 600;
              font-size: 1.25rem;
              border: none;
              cursor: pointer;
              width: 280px;
            "
          >
            我要留言
          </button>
        </div>
      </div>
    </el-main>

    <CommonFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CommonNavbar from '../components/CommonNavbar.vue'
import CommonFooter from '../components/CommonFooter.vue'
import { ArrowDown } from '@element-plus/icons-vue'
import { getProjects } from '../services/projectService'
import type { Project } from '../services/projectService'

const router = useRouter()

// 响应式状态
const projects = ref<Project[]>([])
const loading = ref(false)
const error = ref('')

// // 模拟数据，当无法获取真实数据时使用
// const mockProjects = [
//   {
//     id: 1,
//     title: '智能环境监测',
//     briefIntro: '基于物联网技术的智能环境监测项目，计划在新学期启动，欢迎零基础同学参与学习。',
//     coverImage: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?q=80&w=1740&auto=format&fit=crop',
//     status: '即将开始'
//   },
//   {
//     id: 2,
//     title: '竞赛培训计划',
//     briefIntro: '针对蓝桥杯、挑战杯等竞赛的系统性培训计划，从零基础到参赛水平的完整指导。',
//     coverImage: 'https://images.unsplash.com/photo-1581091226033-d5c42263db6a?q=80&w=1740&auto=format&fit=crop',
//     status: '招新后启动'
//   },
//   {
//     id: 3,
//     title: '技术学习小组',
//     briefIntro: '涵盖Web开发、人工智能、移动开发等多个领域的技术学习小组，每周定期组织研讨和实践。',
//     coverImage: 'https://images.unsplash.com/photo-1607799279861-4dd421887fb3?q=80&w=1740&auto=format&fit=crop',
//     status: '持续进行中'
//   },
//   {
//     id: 4,
//     title: '校园技术节',
//     briefIntro: '计划举办的校园技术节活动，包括技术展览、讲座和互动体验区，展示社团成果和技术创新。',
//     coverImage: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=1740&auto=format&fit=crop',
//     status: '下学期活动'
//   }
// ]

// 获取项目列表数据
const fetchProjects = async () => {
  loading.value = true
  error.value = ''

  try {
    // 使用正确的参数格式
    const params = {
      current: 1,
      size: 4 // 只获取4个项目
    }

    // 调用项目服务获取数据
    const response = await getProjects(params)

    // 处理响应数据
    if (response && response.data && response.data.records && response.data.records.length > 0) {
      projects.value = response.data.records.slice(0, 4)
    } 
  } catch (err) {
    console.error('获取项目列表失败:', err)
    // 发生错误时使用模拟数据
    // projects.value = mockProjects
  } finally {
    loading.value = false
  }
}

// 导航函数
const toabout = () => {
  router.push('/about')
}

const tojoin = () => {
  router.push('/join')
}

const toproject = () => {
  router.push('/projects')
}

const toawards = () => {
  router.push('/awards')
}

// 搜索全部竞赛点击
const tocompetitions = () => {
  router.push('/competitions')
}

// 跳转到留言板的方法
const toMessageBoard = () => {
  router.push('/messages')
}

// 跳转到项目详情页
const goToProjectDetail = (projectId: number) => {
  router.push({ path: '/projectdetail', query: { id: projectId } })
}

// 组件挂载时获取数据
onMounted(() => {
  fetchProjects()
})
</script>

<style scoped lang="scss">
.code-pulse-container {
  font-family: "Inter", "system-ui", "sans-serif";
  color: #333;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: auto;
  padding: 0;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  .container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
    padding: 12px 24px;
  }

  .logo-link {
    display: flex;
    align-items: center;
    gap: 12px;
    color: inherit;

    .logo-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;

      span {
        color: white;
        font-weight: bold;
        font-size: 20px;
      }
    }

    .logo-text {
      font-size: 20px;
      font-weight: bold;
      background: linear-gradient(90deg, #165dff, #ff7d00);
      -webkit-background-clip: text;
      background-clip: text;
      color: transparent;
    }
  }

  .desktop-nav {
    display: flex;
    align-items: center;
    gap: 32px;

    .nav-link {
      position: relative;
      color: #333;
      font-size: 16px;
      transition: color 0.3s;

      &:hover {
        color: #165dff;
      }

      &::after {
        content: "";
        position: absolute;
        bottom: -4px;
        left: 0;
        width: 0;
        height: 2px;
        background-color: #165dff;
        transition: width 0.3s;
      }

      &:hover::after {
        width: 100%;
      }
    }

    .join-btn {
      background: linear-gradient(90deg, #165dff, #ff7d00);
      border: none;
      color: white;
      padding: 8px 24px;
      font-weight: 500;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(22, 93, 255, 0.3);
      }
    }
  }

  .mobile-menu-btn {
    display: none;
    background: none;
    border: none;
    color: #333;
    font-size: 24px;
    padding: 8px;
  }

  .mobile-menu {
    display: none;
    background-color: white;
    border-top: 1px solid #eee;
    padding: 16px 24px;
    animation: fadeIn 0.3s ease;

    .mobile-nav-link {
      display: block;
      padding: 12px 0;
      color: #333;
      font-size: 18px;
      transition: color 0.3s;

      &:hover {
        color: #165dff;
      }
    }

    .mobile-join-btn {
      display: block;
      width: 100%;
      margin-top: 12px;
      background: linear-gradient(90deg, #165dff, #ff7d00);
      border: none;
      color: white;
      padding: 12px 0;
    }
  }

  @media (max-width: 992px) {
    .desktop-nav {
      display: none;
    }

    .mobile-menu-btn {
      display: block;
    }

    .mobile-menu {
      display: block;
    }
  }
}

/* 首页Banner样式 */
.home-banner {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(to bottom, #0f172a, #1e293b);
  padding-top: 80px;
  color: white;
  text-align: center;

  .particle-bg,
  .code-flow-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .particle-bg {
    background: transparent;
    z-index: 1;
  }

  .code-flow-bg {
    background: transparent;
    opacity: 0.4;
    z-index: 2;
  }

  .banner-content {
    position: relative;
    z-index: 3;
    max-width: 1200px;
    padding: 0 24px;
    margin: 0 auto;

    .title-container {
      margin-bottom: 40px;

      .main-title {
        font-size: clamp(2rem, 8vw, 4rem);
        font-weight: bold;
        margin-bottom: 24px;
        line-height: 1.2;

        .code-text {
          background: linear-gradient(90deg, #165dff, #ff7d00);
          -webkit-background-clip: text;
          background-clip: text;
          color: transparent;
          text-shadow: 0 0 10px currentColor;
        }

        .pulse-text {
          display: block;
        }
      }

      .subtitle {
        font-size: clamp(1rem, 4vw, 1.3rem);
        color: #cbd5e1;
        max-width: 800px;
        margin: 0 auto;
      }
    }

    .description {
      color: #cbd5e1;
      max-width: 700px;
      margin: 0 auto 40px;
      font-size: 16px;
    }

    .action-buttons {
      display: flex;
      justify-content: center;
      gap: 16px;
      margin-bottom: 40px;

      .learn-more-btn {
        background-color: white;
        color: #0f172a;
        font-weight: 500;
        padding: 12px 24px;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-4px);
          box-shadow: 0 10px 20px rgba(255, 255, 255, 0.2);
        }
      }

      .join-us-btn {
        border-color: white;
        color: rgb(31, 3, 3);
        padding: 12px 24px;
        transition: all 0.3s;

        &:hover {
          background-color: rgba(255, 255, 255, 0.1);
          transform: translateY(-4px);
        }
      }
    }

    .scroll-down {
      position: absolute;
      top: 450px;
      left: 50%;
      transform: translateX(-50%);
      animation: bounce 2s infinite;

      .scroll-icon {
        color: rgba(255, 255, 255, 0.7);
        font-size: 24px;
        transition: color 0.3s;

        &:hover {
          color: white;
        }
      }
    }
  }
}

/* 通用部分样式 */
.section-title {
  font-size: clamp(1.5rem, 5vw, 2.5rem);
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
}

.section-divider {
  width: 80px;
  height: 2px;
  background: linear-gradient(to right, #165dff, #ff7d00);
  margin: 0 auto 24px;
}

.section-subtitle {
  color: #666;
  max-width: 800px;
  margin: 0 auto;
  font-size: 1rem;
}

.text-center {
  text-align: center;
}

/* 社团简介部分样式 */
.about-section {
  padding: 80px 0;
  background-color: white;

  .about-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .about-content {
    display: grid;
    grid-template-columns: 1fr;
    gap: 40px;
    margin-bottom: 60px;

    @media (min-width: 992px) {
      grid-template-columns: 1fr 1fr;
    }
  }

  .about-text {
    .about-subtitle {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
      color: #333;
    }

    .about-paragraph {
      color: #666;
      margin-bottom: 16px;
      line-height: 1.6;
      font-size: 1.3rem;
    }

    .highlight-text {
      font-weight: bold;
      color: #165dff;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 24px;
      margin-top: 40px;
    }

    .stat-card {
      background-color: #f8fafc;
      padding: 24px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
      }
    }

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      background-color: rgba(22, 93, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;

      .el-icon {
        font-size: 24px;
        color: #165dff;
      }
    }

    .stat-number {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 8px;
      color: #333;
    }

    .stat-label {
      color: #666;
      font-size: 0.875rem;
    }
  }

  .about-image {
    position: relative;

    .image-decoration {
      position: absolute;
      background-color: rgba(22, 93, 255, 0.1);
      border-radius: 8px;
      z-index: -1;

      &.top-left {
        width: 96px;
        height: 96px;
        top: -16px;
        left: -16px;
      }

      &.bottom-right {
        width: 128px;
        height: 128px;
        bottom: -16px;
        right: -16px;
      }
    }

    .about-img {
      width: 100%;
      border-radius: 12px;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
    }

    .image-badge {
      position: absolute;
      bottom: -24px;
      left: -24px;
      background-color: white;
      padding: 16px;
      border-radius: 12px;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      gap: 12px;

      .badge-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background-color: rgba(255, 125, 0, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;

        .el-icon {
          color: #ff7d00;
          font-size: 20px;
        }
      }

      .badge-title {
        font-weight: bold;
        margin-bottom: 4px;
        color: #333;
      }

      .badge-subtitle {
        font-size: 0.875rem;
        color: #666;
      }
    }
  }

  .stats-chart {
    margin-top: 80px;

    .chart-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
      text-align: center;
      color: #333;
    }

    .chart-container {
      background-color: #f8fafc;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    }

    .chart-placeholder {
      height: 250px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #666;
      background-color: white;
      border-radius: 8px;
      border: 1px dashed #ddd;

      .el-icon {
        font-size: 48px;
        margin-bottom: 16px;
        color: #165dff;
      }
    }
  }
}
// 高光时刻
.bg-primary\/10 {
  background-color: rgb(59 130 246 / 0.1);
}
.text-primary {
  --tw-text-opacity: 1;
  color: rgb(59 130 246 / var(--tw-text-opacity, 1));
}

.border-primary {
  --tw-border-opacity: 1;
  border-color: rgb(59 130 246 / var(--tw-border-opacity, 1));
}

.text-accent {
  --tw-text-opacity: 1;
  color: rgb(245 158 11 / var(--tw-text-opacity, 1));
}
/* 部门介绍部分样式 */
.departments-section {
  padding: 80px 0;
  background-color: #f8fafc;

  .departments-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  /* 部门卡片网格布局 */
  .departments-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 24px;
    margin-bottom: 60px;

    @media (min-width: 768px) {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  /* 单个部门卡片样式 */
  .department-card {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }

    /* 部门头部区域 */
    .department-header {
      height: 180px;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      overflow: hidden;

      /* 不同部门的颜色主题 */
      &.algorithm {
        background: linear-gradient(135deg, #165dff 0%, #4080ff 100%);
      }

      &.competition {
        background: linear-gradient(135deg, #ff7d00 0%, #ffa940 100%);
      }

      &.management {
        background: linear-gradient(135deg, #4b5563 0%, #6b7280 100%);
      }

      /* 部门图标样式 */
      .department-icon {
        color: white;
        font-size: 64px;
        z-index: 2;
        transition: all 0.3s ease;
      }

      /* 背景装饰元素 */
      &::before {
        content: "";
        position: absolute;
        width: 150px;
        height: 150px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 50%;
        top: -50px;
        right: -50px;
      }

      &::after {
        content: "";
        position: absolute;
        width: 100px;
        height: 100px;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 50%;
        bottom: -30px;
        left: -30px;
      }
    }

    /* 部门内容区域 */
    .department-content {
      padding: 24px;

      .department-title {
        font-size: 1.25rem;
        font-weight: 600;
        margin-bottom: 16px;
        color: #1f2937;
        display: flex;
        align-items: center;

        .department-title-icon {
          margin-right: 10px;
          font-size: 20px;
        }
      }

      .department-description {
        color: #6b7280;
        font-size: 0.875rem;
        line-height: 1.6;
        margin-bottom: 12px;
      }

      /* 查看成员按钮 */
      .department-details-btn {
        width: 100%;
        margin-top: 16px;
        background: transparent;
        border: 1px solid #e5e7eb;
        color: #4b5563;
        font-size: 0.875rem;
        transition: all 0.3s ease;

        &:hover {
          background: #f3f4f6;
          border-color: #d1d5db;
        }

        .el-icon {
          margin-left: 6px;
          transition: transform 0.3s ease;
        }
      }
    }

    /* 成员列表区域 */
    .department-members {
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px solid #f3f4f6;

      .members-title {
        font-size: 0.875rem;
        font-weight: 600;
        color: #4b5563;
        margin-bottom: 12px;
      }

      .members-list {
        display: grid;
        gap: 12px;
      }

      .member-item {
        display: flex;
        align-items: center;
        padding: 12px;
        background: #f9fafb;
        border-radius: 8px;
        transition: all 0.2s ease;

        &:hover {
          background: #f3f4f6;
          transform: translateX(4px);
        }

        .member-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          margin-right: 12px;
          object-fit: cover;
          border: 2px solid white;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .member-info {
          .member-name {
            font-size: 0.875rem;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 2px;
          }

          .member-position {
            font-size: 0.75rem;
            color: #9ca3af;
          }
        }
      }
    }
  }

  /* 组织架构图样式 */
  .organization-chart {
    margin-top: 80px;
    background: white;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

    .chart-title {
      font-size: 1.5rem;
      font-weight: 600;
      color: #1f2937;
      text-align: center;
      margin-bottom: 40px;
    }

    .chart-container {
      position: relative;
      height: 350px;
      min-width: 600px;
      margin: 0 auto;

      /* 组织节点 */
      .org-node {
        position: absolute;
        padding: 16px 24px;
        border-radius: 8px;
        text-align: center;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
        cursor: pointer;
        z-index: 10;

        &:hover {
          transform: scale(1.05);
          box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }

        &.president {
          top: 0;
          left: 50%;
          transform: translateX(-50%);
          background: linear-gradient(135deg, #165dff 0%, #4080ff 100%);
          color: white;
          width: 180px;
        }

        &.director {
          top: 100px;
          left: 50%;
          transform: translateX(-50%);
          background: white;
          border: 2px solid #e5e7eb;
          width: 200px;
        }

        &.department {
          top: 200px;
          width: 180px;

          &.algorithm {
            left: calc(33.333% - 90px);
            background: rgba(22, 93, 255, 0.1);
            border: 1px solid rgba(22, 93, 255, 0.2);
          }

          &.competition {
            left: calc(50% - 90px);
            background: rgba(255, 125, 0, 0.1);
            border: 1px solid rgba(255, 125, 0, 0.2);
          }

          &.management {
            left: calc(66.666% - 90px);
            background: rgba(75, 85, 99, 0.1);
            border: 1px solid rgba(75, 85, 99, 0.2);
          }
        }

        .org-node-title {
          font-weight: 600;
          margin-bottom: 4px;
        }
      }

      /* 连接线 */
      .org-line {
        position: absolute;
        background: #d1d5db;

        &.vertical {
          width: 2px;
          height: 40px;
          left: 50%;
          transform: translateX(-50%);
        }

        &.horizontal {
          width: calc(100% - 200px);
          height: 2px;
          top: 200px;
          left: 100px;
        }
      }
    }

    .chart-note {
      text-align: center;
      color: #9ca3af;
      font-size: 0.75rem;
      margin-top: 20px;
    }
  }

  /* 响应式调整 */
  @media (max-width: 992px) {
    .departments-grid {
      grid-template-columns: 1fr 1fr;
    }

    .organization-chart {
      display: none;
    }
  }

  @media (max-width: 768px) {
    .departments-grid {
      grid-template-columns: 1fr;
    }

    .department-card {
      max-width: 400px;
      margin: 0 auto;
    }
  }
}

/* 活动风采部分样式 */
.activities-section {
  padding: 80px 0;
  background-color: white;

  .activities-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .activities-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 24px;
    margin-bottom: 40px;

    @media (min-width: 992px) {
      grid-template-columns: 1fr 1fr;
    }
  }

  .featured-activity {
    position: relative;
    height: 400px;
    border-radius: 12px;
    overflow: hidden;
    background-color: #0f172a;
    background-size: cover;
    background-position: center;

    .activity-overlay {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 24px;
      background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
      color: white;

      .activity-tag {
        margin-bottom: 12px;
        font-weight: bold;
      }

      .activity-title {
        font-size: 1.5rem;
        font-weight: bold;
        margin-bottom: 12px;
      }

      .activity-description {
        margin-bottom: 16px;
        font-size: 0.875rem;
        opacity: 0.9;
      }

      .activity-meta {
        display: flex;
        gap: 24px;

        .meta-item {
          display: flex;
          align-items: center;
          font-size: 0.875rem;
          opacity: 0.8;

          .el-icon {
            margin-right: 8px;
            font-size: 16px;
          }
        }
      }
    }
  }

  .activity-highlights {
    .highlights-title {
      font-size: 1.25rem;
      font-weight: bold;
      margin-bottom: 16px;
      color: #333;
    }

    .highlights-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
      margin-bottom: 16px;
    }

    .highlight-item {
      position: relative;
      height: 120px;
      border-radius: 8px;
      overflow: hidden;
      background-size: cover;
      background-position: center;
      cursor: pointer;
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.05);

        .highlight-overlay {
          opacity: 1;
        }
      }

      .highlight-overlay {
        position: absolute;
        inset: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-weight: 500;
        transition: opacity 0.3s ease;
      }
    }

    .activity-features {
      background-color: #f8fafc;
      border-radius: 12px;
      padding: 16px;
      margin-top: 24px;

      .feature-header {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .feature-icon {
          color: #165dff;
          font-size: 24px;
          margin-right: 12px;
        }

        .feature-title {
          font-weight: bold;
          color: #333;
        }
      }

      .feature-list {
        list-style: none;
        padding: 0;
        margin: 0;

        .feature-item {
          display: flex;
          align-items: flex-start;
          margin-bottom: 12px;

          .feature-check {
            color: #165dff;
            font-size: 16px;
            margin-right: 8px;
            margin-top: 2px;
          }

          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }

  .activity-videos {
    margin-top: 60px;

    .videos-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
      text-align: center;
      color: #333;
    }

    .videos-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 24px;

      @media (min-width: 768px) {
        grid-template-columns: repeat(3, 1fr);
      }
    }

    .video-item {
      position: relative;
      border-radius: 12px;
      overflow: hidden;
      cursor: pointer;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-4px);

        .video-overlay {
          background-color: rgba(0, 0, 0, 0.6);

          .play-icon {
            transform: scale(1.2);
          }
        }
      }

      .video-overlay {
        position: absolute;
        inset: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(0, 0, 0, 0.4);
        transition: all 0.3s ease;

        .play-icon {
          color: white;
          font-size: 48px;
          transition: transform 0.3s ease;
        }
      }

      .video-caption {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 12px;
        background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
        color: white;
        font-size: 0.875rem;
      }
    }
  }
}

/* 项目展示部分样式 */
.projects-section {
  padding: 80px 0;
  background-color: #f8fafc;

  .projects-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .projects-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 24px;

    @media (min-width: 768px) {
      grid-template-columns: repeat(2, 1fr);
    }

    @media (min-width: 992px) {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  .project-card {
    background-color: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
    }
  }

  .project-image-container {
    position: relative;
    height: 200px;
    background-size: cover;
    background-position: center;

    .project-tag {
      position: absolute;
      top: 16px;
      left: 16px;
    }
  }

  .project-content {
    padding: 20px;

    .project-title {
      font-size: 1.25rem;
      font-weight: bold;
      margin-bottom: 12px;
      color: #333;
    }

    .project-description {
      color: #666;
      margin-bottom: 16px;
      font-size: 0.875rem;
      line-height: 1.6;
    }

    .project-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 20px;

      .tech-tag {
        font-size: 0.75rem;
        background-color: #f8fafc;
        color: #666;
        border: none;
      }
    }

    .project-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 12px;
      border-top: 1px solid #eee;

      .project-author {
        display: flex;
        align-items: center;

        .author-avatar {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          margin-right: 8px;
          object-fit: cover;
        }

        .author-name {
          font-size: 0.875rem;
          color: #666;
        }
      }

      .project-link {
        font-size: 0.875rem;
        color: #165dff;
      }
    }
  }

  .more-projects {
    margin-top: 40px;
    text-align: center;

    .more-btn {
      padding: 12px 24px;
      background-color: white;
      color: #165dff;
      border: 1px solid #165dff;

      &:hover {
        background-color: rgba(22, 93, 255, 0.1);
      }
    }
  }
}
// 高光
.achievements-section {
  .scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
  }
  .scrollbar-hide::-webkit-scrollbar {
    display: none;
  }
}

/* 加入我们部分样式 */
.join-section {
  padding: 80px 0;
  background: linear-gradient(to bottom right, #0f172a, #1e293b);
  color: white;

  .join-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .section-title,
  .section-subtitle {
    color: white;
  }

  .section-divider {
    background: linear-gradient(to right, #165dff, #ff7d00);
  }

  .join-content {
    display: grid;
    grid-template-columns: 1fr;
    gap: 40px;
    margin-top: 40px;

    @media (min-width: 992px) {
      grid-template-columns: 1fr 1fr;
    }
  }

  .join-reasons {
    .reasons-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
    }

    .reasons-list {
      display: grid;
      gap: 24px;
    }

    .reason-item {
      display: flex;
      gap: 16px;

      .reason-icon {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        background-color: rgba(22, 93, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;

        .el-icon {
          color: #165dff;
          font-size: 24px;
        }
      }

      .reason-content {
        .reason-title {
          font-size: 1.125rem;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .reason-description {
          color: #cbd5e1;
          font-size: 0.875rem;
          line-height: 1.6;
        }
      }
    }

    .join-notice {
      margin-top: 40px;
      padding: 20px;
      background-color: rgba(255, 255, 255, 0.05);
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 12px;

      .notice-header {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .notice-icon {
          color: #ff7d00;
          font-size: 24px;
          margin-right: 12px;
        }

        .notice-title {
          font-weight: bold;
          font-size: 1.125rem;
        }
      }

      .notice-list {
        list-style: none;
        padding: 0;
        margin: 0;

        .notice-item {
          display: flex;
          align-items: flex-start;
          margin-bottom: 12px;
          color: #cbd5e1;
          font-size: 0.875rem;

          .notice-check {
            color: #ff7d00;
            font-size: 16px;
            margin-right: 8px;
            margin-top: 2px;
          }

          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }

  .join-form-container {
    .join-form {
      background-color: white;
      padding: 32px;
      border-radius: 12px;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);

      .form-title {
        font-size: 1.5rem;
        font-weight: bold;
        margin-bottom: 24px;
        color: #333;
        text-align: center;
      }

      .el-form-item {
        margin-bottom: 20px;
      }

      .el-input,
      .el-select,
      .el-textarea {
        width: 100%;
      }

      .submit-btn {
        width: 100%;
        padding: 16px;
        font-size: 1rem;
        background: linear-gradient(to right, #165dff, #ff7d00);
        border: none;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 5px 15px rgba(22, 93, 255, 0.3);
        }
      }
    }
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0) translateX(-50%);
  }

  40% {
    transform: translateY(-20px) translateX(-50%);
  }

  60% {
    transform: translateY(-10px) translateX(-50%);
  }
}

.fixed-bottom {
  position: fixed;
  bottom: 20px;
  width: 100%;
  z-index: 100;
}

.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.scroll-text {
  color: #333;
  font-size: 14px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .home-banner .banner-content .action-buttons {
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    width: 100%;
  }

  /* 调整移动端按钮宽度 */
  .home-banner .banner-content .action-buttons .el-button {
    width: 100%;
    max-width: 300px;
  }

  /* 移除按钮之间的左边距 */
  .home-banner .banner-content .action-buttons .el-button + .el-button {
    margin-left: 0 !important;
  }

  .navbar .container {
    padding: 12px 16px;
  }

  .departments-grid {
    grid-template-columns: 1fr !important;
  }

  .organization-chart {
    display: none;
  }
}
</style>
