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
            <span class="code-text">把你的脑洞，变成真实的代码。</span>
          </h1>
        </div>

        <p class="description">
          这是一个由兴趣驱动的小型开发者社团。不用担心自己技术菜，哪怕你连 HTML 都没听过，只要愿意带上电脑，用 AI 试着跑通第一行代码，这里都有人陪你一起折腾。
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
          <el-link href="#about" underline="never" class="text-center">
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
                <i-ep-arrow-down />
              </el-icon>
            </div>
          </el-link>
        </div>
      </div>
    </el-main>

    <!-- 社团简介 -->
    <el-main class="about-section" id="about">
      <div class="about-container">
        <div class="text-center mb-4">
          <h1 class="section-title">加入前，你需要知道这些</h1>
          <div class="section-divider"></div>
          <p class="about-lead">
            这里提供同伴交流、方向建议和实践机会，长期成长仍然需要自己主动完成。
          </p>
        </div>
        <div class="about-content">
          <div class="about-info">
            <div class="about-faq-grid">
              <article class="about-faq-card">
                <h3>我纯零基础，什么都不会能加吗？</h3>
                <p>能。社团里没几个真大佬，大家都是边查边学。现在有了 AI，做个好看的网站或者小程序门槛很低。只要你想学，大把的入门项目可以练手。</p>
              </article>
              <article class="about-faq-card">
                <h3>加入后会有学长开课教我们吗？</h3>
                <p>坦白说，没有那种按部就班的讲座培训，听着也无聊。我们的方式是发任务和自学，但遇到 Bug 或者卡壳了，在群里问一声，老手们随时帮你排雷。</p>
              </article>
              <article class="about-faq-card">
                <h3>必须写代码吗？能干点别的吗？</h3>
                <p>当然可以。如果你对写代码没兴趣，但能用 AI 搞定好看的 UI 设计、排版，或者能搞定大创比赛的 PPT 和策划案，团队同样非常需要你。</p>
              </article>
              <article class="about-faq-card">
                <h3>社团平时都有什么活动？</h3>
                <p>没什么走形式的开会。更多时候是大家找个空教室，带着电脑坐一起各干各的（也就是自习）。偶尔办个展示会，看看大家最近又搞出了什么好玩的东西。</p>
              </article>
            </div>
            <p class="about-highlight">
              执行力比你的基础与天赋更重要
            </p>
          </div>
          <div class="about-image">
            <div class="image-decoration top-left"></div>
            <div class="image-decoration bottom-right"></div>
            <img class="about-img" src="/you.webp" alt="一起动手完成作品" />
            <div class="image-badge about-image-badge">
              <h4 class="badge-title">期待你的加入</h4>
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
            我们最近搞出的一些动静
          </h2>
          <div class="w-20 h-1 bg-primary mx-auto rounded-full"></div>
          <p class="text-gray-600 mt-4 max-w-2xl mx-auto">
            没那么多高大上的理论，就是遇到问题解决问题。这里记录了我们拿过的省级奖项，和平时敲出来的真实项目。
          </p>
        </div>
        <!-- 轮播图区域 -->
        <div class="relative">
          <el-carousel
            ref="carouselRef"
            :interval="4000"
            :type="isSmallScreen ? undefined : 'card'"
            :height="isSmallScreen ? '260px' : '400px'"
            autoplay
            indicator-position="none"
          >
            <!-- 渲染高光时刻卡片 -->
            <el-carousel-item
              v-for="moment in highlightMoments"
              :key="moment.id"
            >
              <div
                class="w-full h-full bg-white rounded-xl shadow-lg overflow-hidden"
              >
                <el-image
                  :src="moment.image"
                  :alt="moment.alt"
                  class="w-full h-48 object-contain cursor-zoom-in bg-gray-100"
                  :preview-src-list="[moment.image]"
                  :initial-index="0"
                  :preview-teleported="true"
                  fit="cover"
                />
                <div class="p-6">
                  <div class="flex justify-between items-start mb-4">
                    <h3 class="text-xl font-bold text-dark">
                      {{ moment.title }}
                    </h3>
                  </div>
                  <p class="text-gray-600 mb-6 line-clamp-6">
                    {{ moment.description }}
                  </p>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>

          <!-- 左右导航按钮 -->
          <button
            class="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 w-10 h-10 rounded-full bg-white/80 shadow-md flex items-center justify-center hover:bg-white transition-all duration-300 border border-gray-200"
            @click="prev"
          >
            <el-icon><i-ep-arrow-left /></el-icon>
          </button>
          <button
            class="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 w-10 h-10 rounded-full bg-white/80 shadow-md flex items-center justify-center hover:bg-white transition-all duration-300 border border-gray-200"
            @click="next"
          >
            <el-icon><i-ep-arrow-right /></el-icon>
          </button>
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
    <!-- 探索方向 -->
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
            你可以从哪里开始
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
          <p class="direction-lead">
            方向可以交叉，也可以随时调整。先选择一个小目标，做出第一个能够展示的成果。
          </p>
        </div>

        <!-- 竞赛列表 -->
        <div class="competitions-grid">
          <!-- 方向一：AI 编程与应用 -->
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
              <i-fa6-solid-code style="font-size: 2.5rem; color: #3b82f6" />
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              AI 编程与应用
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              不用死记硬背枯燥的语法，学习怎么用提示词（Prompt）让 AI 帮你写代码。零基础也能快速搞出能跑的网页。
            </p>
            <span class="direction-tag">从一个小工具开始</span>
          </div>

          <!-- 方向二：算法与竞赛 -->
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
              <i-fa6-solid-trophy style="font-size: 2.5rem; color: #6366f1" />
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              算法与竞赛
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              针对蓝桥杯等算法竞赛的路线。大家一起刷题交流，适合想要拿奖、攒简历和赚学分的同学。
            </p>
            <span class="direction-tag">从一次练习开始</span>
          </div>

          <!-- 方向三：软件与硬件创作 -->
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
              <i-fa6-solid-desktop style="font-size: 2.5rem; color: #22c55e" />
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              软件与硬件创作
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              如果你有一定的基础，想做完整的项目（比如微信小程序或单片机硬件），可以来这里一起对接，把东西落地。
            </p>
            <span class="direction-tag">从一个真实需求开始</span>
          </div>

          <!-- 方向四：项目表达与设计 -->
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
              <i-fa6-solid-lightbulb style="font-size: 2.5rem; color: #ef4444" />
            </div>
            <h3
              style="
                font-size: 1.25rem;
                font-weight: bold;
                color: #1e293b;
                margin-bottom: 0.75rem;
              "
            >
              项目表达与设计
            </h3>
            <p style="color: #6b7280; margin-bottom: 1rem">
              负责团队的“面子工程”。用 AI 绘画和排版工具，搞定项目 UI、比赛演示文稿和视频剪辑。
            </p>
            <span class="direction-tag">从表达一个想法开始</span>
          </div>
        </div>

        <!-- 加入流程入口 -->
        <div style="text-align: center; margin-top: 3rem">
          <button
            @click="tojoin"
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
            查看加入方式
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
          class="cta-actions"
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
          从一个小目标开始
        </h2>
        <p style="color: white; font-size: 1.125rem; margin-bottom: 2.5rem">
          不需要提前准备完整技能。选择一个感兴趣的方向，完成一次真实尝试，再决定是否长期参与。
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
            了解加入流程
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
            给我们留言
          </button>
        </div>
      </div>
    </el-main>

    <CommonFooter />
  </div>
</template>

<script setup>
  import { ref, onMounted, onUnmounted } from 'vue'
  import { useRouter } from 'vue-router'
  import CommonNavbar from '@/components/CommonNavbar.vue'
  import CommonFooter from '@/components/CommonFooter.vue'

  // 添加 carouselRef 引用
  const carouselRef = ref()
  const isSmallScreen = ref(false)

  /** 根据屏幕宽度切换轮播展示模式。 */
  const updateCarouselLayout = () => {
    isSmallScreen.value = window.innerWidth < 640
  }

  // 添加导航方法
  const prev = () => {
    carouselRef.value?.prev()
  }

  const next = () => {
    carouselRef.value?.next()
  }

  const router = useRouter()

  // 高光时刻数据
  const highlightMoments = ref([
    {
      id: 1,
      image: new URL('@/assets/images/hightligh/校圈主页.jpg', import.meta.url)
        .href,
      alt: '校圈（微信小程序）',
      title: '校圈（微信小程序）',
      description:
        '校圈是一个基于微信小程序的大学生社交平台，为学生提供了一个交流、分享和发现的平台。',
    },
    {
      id: 2,
      image: new URL('@/assets/images/hightligh/活集贸.gif', import.meta.url)
        .href,
      alt: '招新网',
      title: '招新网',
      description: '没错孩子，就是这个网站。',
    },
    {
      id: 3,
      image: new URL(
        '@/assets/images/hightligh/第九届CCPC铜奖颁奖.webp',
        import.meta.url
      ).href,
      alt: '第九届CCPC铜奖颁奖',
      title: '第九届CCPC铜奖颁奖',
      description:
        '我们在第九届中国大学生程序设计竞赛中荣获铜奖，这是对我们团队协作和编程能力的肯定。',
    },
    {
      id: 4,
      image: new URL('@/assets/images/hightligh/智慧药箱.webp', import.meta.url)
        .href,
      alt: '智慧药箱',
      title: '智慧药箱',
      description: '挑战杯校赛一等奖作品',
    },
    {
      id: 5,
      image: new URL('@/assets/images/hightligh/ccpc.jpg', import.meta.url)
        .href,
      alt: '河北省大学生程序设计竞赛',
      title: '河北省大学生程序设计竞赛',
      description: '参加中国大学生程序设计竞赛河北省赛，',
    },
    {
      id: 6,
      image: new URL(
        '@/assets/images/hightligh/挑战杯路演.webp',
        import.meta.url
      ).href,
      alt: '挑战杯路演',
      title: '挑战杯路演',
      description: '参加2025年挑战杯河北省大学生课外学术科技作品校内赛',
    },
    {
      id: 7,
      image: new URL(
        '@/assets/images/hightligh/十六届蓝桥杯国赛.webp',
        import.meta.url
      ).href,
      alt: '十六届蓝桥杯国赛',
      title: '十六届蓝桥杯国赛',
      description:
        '参加十六届蓝桥杯全国软件和信息技术专业人才大赛国赛，与全国优秀选手同台竞技。',
    },
  ])

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

  // 跳转到留言板的方法
  const toMessageBoard = () => {
    router.push('/messages')
  }

  onMounted(() => {
    updateCarouselLayout()
    window.addEventListener('resize', updateCarouselLayout)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', updateCarouselLayout)
  })
</script>

<style scoped lang="scss">
  .code-pulse-container {
    font-family: 'Inter', 'system-ui', 'sans-serif';
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
          content: '';
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
        grid-template-columns: 1.15fr 0.85fr;
        align-items: center;
      }
    }

    .about-lead {
      max-width: 720px;
      margin: 0 auto 40px;
      color: #64748b;
      font-size: 1rem;
      line-height: 1.8;
    }

    .about-faq-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 16px;

      @media (min-width: 768px) {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    .about-faq-card {
      padding: 24px;
      border: 1px solid #e2e8f0;
      border-radius: 16px;
      background: #f8fafc;
      transition: transform 0.25s ease, box-shadow 0.25s ease;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
      }

      h3 {
        margin-bottom: 12px;
        color: #1e293b;
        font-size: 1.1rem;
        font-weight: 700;
      }

      p {
        margin: 0;
        color: #64748b;
        line-height: 1.8;
      }
    }

    .about-highlight {
      margin: 32px 0 0;
      padding: 18px 20px;
      border-left: 4px solid #3b82f6;
      border-radius: 0 12px 12px 0;
      background: #eff6ff;
      color: #2563eb;
      font-weight: 600;
      line-height: 1.7;
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

      .about-image-badge {
        bottom: -24px;
        left: -24px;
        padding: 16px 20px;
        background: #f59e0b;
        transform: skew(-20deg);

        .badge-title {
          margin: 0;
          color: white;
          font-size: 1.25rem;
          transform: skew(20deg);
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
          content: '';
          position: absolute;
          width: 150px;
          height: 150px;
          background: rgba(255, 255, 255, 0.1);
          border-radius: 50%;
          top: -50px;
          right: -50px;
        }

        &::after {
          content: '';
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

  .competitions-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .direction-lead {
    max-width: 680px;
    margin: 24px auto 0;
    color: #64748b;
    line-height: 1.8;
  }

  .competitions-grid > div {
    transition: transform 0.25s ease, box-shadow 0.25s ease;
    box-shadow: 0 4px 14px rgba(15, 23, 42, 0.05);
  }

  .competitions-grid > div:hover {
    transform: translateY(-5px);
    box-shadow: 0 14px 28px rgba(15, 23, 42, 0.1);
  }

  .direction-tag {
    display: inline-flex;
    align-items: center;
    min-height: 28px;
    padding: 4px 10px;
    border-radius: 9999px;
    background: #eff6ff;
    color: #2563eb;
    font-size: 0.8rem;
    font-weight: 600;
  }

  .cta-actions {
    flex-direction: row !important;
    justify-content: center;
    flex-wrap: wrap;
  }

  .cta-actions button {
    width: min(280px, 100%) !important;
  }

  @media (min-width: 640px) {
    .competitions-grid {
      grid-template-columns: repeat(2, 1fr);
    }
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

  /* Element Plus 图片预览样式优化 */
  .el-image-viewer__wrapper {
    .el-image-viewer__canvas {
      cursor: grab;
      
      &:active {
        cursor: grabbing;
      }
    }
  }
</style>
