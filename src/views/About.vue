<template>
  <div class="about-page min-h-screen font-sans text-dark bg-gray-50">
    <!-- 通用导航栏（替换原移动端导航，避免冗余） -->
    <CommonNavbar />

    <!-- 主内容区：优化移动端间距 -->
    <main
      class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 mt-14 mobile:py-4 mobile:mt-12"
    >
      <!-- 1. 社团简介 -->
      <section class="mb-12 mobile:mb-8 animate-fade-in">
        <div class="text-center mb-8 mobile:mb-6">
          <h1
            class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold text-dark mb-3 mobile:text-[clamp(1.2rem,5vw,2rem)]"
          >
            关于智环学创融合协会
          </h1>
          <p class="text-gray-600 max-w-2xl mx-auto mobile:text-sm">
            我们是一群怀揣着对科技的热忱与憧憬的青年集结而成的社团。成立以来，始终以
            “融合创新、实践成长”
            为核心，致力于为所有热爱科技创新的同学，打造一个自由交流、共同学习的平台。
          </p>
        </div>

        <div
          class="bg-white rounded-xl shadow-lg overflow-hidden mobile:rounded-lg"
        >
          <div class="md:flex flex-col mobile:flex-col">
            <div class="mobile:w-full">
              <img
                src="https://images.unsplash.com/photo-1484417894907-623942c8ee29?q=80&w=1740&auto=format&fit=crop"
                alt="社团活动照片"
                class="w-full h-64 md:h-full object-cover mobile:h-48"
                loading="lazy"
              />
            </div>
            <div
              class="p-8 md:p-10 flex flex-col justify-center mobile:w-full mobile:p-5"
            >
              <h2 class="text-2xl font-bold text-dark mb-4 mobile:text-xl">
                社团简介
              </h2>
              <p
                class="text-gray-700 leading-relaxed mb-3 mobile:text-sm mobile:mb-2"
              >
                智环学创融合协会成立于2024年，是一个充满活力的科技创新社团。社团以"融合创新、实践成长"为核心理念，致力于为热爱科技创新的同学提供一个自由交流、共同学习的平台。
              </p>
              <p
                class="text-gray-700 leading-relaxed mb-3 mobile:text-sm mobile:mb-2"
              >
                作为新成立的社团，虽暂未积累下深厚的历史沉淀，但我们充满热情与创造力。在这里，不设技术门槛，无论是计算机专业，渴望在前端、后端、测试等技术赛道从
                0 到 1 做项目的同学；还是非计算机专业，希望在 PPT
                制作、项目书撰写、UI
                设计等领域发挥创意与特长的同学，都能找到属于自己的位置。
              </p>
              <p class="text-gray-700 leading-relaxed mobile:text-sm">
                社团下设算法部、项目资源部和综合管理部三个部门，主要面向大一新生，强调学习意愿和自主学习能力。我们注重在实际项目中学习技术，通过参与各类竞赛来检验和提升能力，助力同学们在就业、考研等方面增强竞争力，在科技探索的道路上携手前行，共同成长。
              </p>
            </div>
          </div>
        </div>
      </section>

      <!-- 2. 社团发展历程 -->
      <section class="mb-16">
        <div class="text-center mb-10">
          <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
            社团发展历程
          </h2>
          <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
        </div>

        <div class="relative">
          <!-- 时间线轴线 -->
          <div
            class="absolute left-0 md:left-1/2 transform md:-translate-x-1/2 h-full w-1 bg-gray-200"
          ></div>

          <!-- 2024年：社团成立 -->
          <div
            class="relative mb-10 md:mb-16 animate-slide-up"
            style="animation-delay: 0.1s"
          >
            <div class="md:flex items-center">
              <div class="md:w-1/2 md:pr-10 md:text-right mb-6 md:mb-0">
                <h3 class="text-xl font-bold text-dark mb-2">社团成立</h3>
                <p class="text-gray-600">
                  智环学创融合协会正式成立，由一群热爱科技创新的同学共同创建，开启社团发展之路
                </p>
              </div>
              <div
                class="absolute left-0 md:left-1/2 transform -translate-x-1/2 w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center shadow-lg z-10"
              >
                <span class="font-bold">24</span>
              </div>
              <div class="md:w-1/2 md:pl-10 md:ml-auto">
                <div class="bg-white rounded-xl shadow-md p-4">
                  <h4 class="font-medium text-dark mb-2">2024年9月</h4>
                  <p class="text-gray-600 text-sm">
                    完成社团注册，确定三个部门架构：算法部、项目竞赛部、综合管理部
                  </p>
                </div>
              </div>
            </div>
          </div>

          <!-- 2025年：首次招新 -->
          <div
            class="relative mb-10 md:mb-16 animate-slide-up"
            style="animation-delay: 0.2s"
          >
            <div class="md:flex items-center">
              <div class="md:w-1/2 md:pr-10 md:text-right mb-6 md:mb-0">
                <h3 class="text-xl font-bold text-dark mb-2">新的开始</h3>
                <p class="text-gray-600">
                  社团开始第一次正式招新，期待更多热爱科技的同学加入我们，共同创造社团的未来
                </p>
              </div>
              <div
                class="absolute left-0 md:left-1/2 transform -translate-x-1/2 w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center shadow-lg z-10"
              >
                <span class="font-bold">25</span>
              </div>
              <div class="md:w-1/2 md:pl-10 md:text-left md:ml-auto">
                <div class="bg-white rounded-xl shadow-md p-4">
                  <h4 class="font-medium text-dark mb-2">2025年3月</h4>
                  <p class="text-gray-600 text-sm">
                    首次招新活动启动，面向大一新生，不设技术门槛
                  </p>
                </div>
              </div>
            </div>
          </div>

          <!-- 未来展望 -->
          <div class="relative animate-slide-up" style="animation-delay: 0.3s">
            <div class="md:flex items-center">
              <div class="md:w-1/2 md:pr-10 md:text-right mb-6 md:mb-0">
                <h3 class="text-xl font-bold text-dark mb-2">未来可期</h3>
                <p class="text-gray-600">
                  社团正在逐步发展中，我们将通过不定期的活动和项目实践，慢慢积累经验和成果，期待与你一起见证成长
                </p>
              </div>
              <div
                class="absolute left-0 md:left-1/2 transform -translate-x-1/2 w-10 h-10 rounded-full bg-secondary text-white flex items-center justify-center shadow-lg z-10"
              >
                <i class="fa fa-star"></i>
              </div>
              <div class="md:w-1/2 md:pl-10 md:ml-auto">
                <div class="bg-white rounded-xl shadow-md p-4">
                  <h4 class="font-medium text-dark mb-2">未来展望</h4>
                  <p class="text-gray-600 text-sm">
                    期待在各类竞赛中崭露头角，成为学校有影响力的科技创新社团
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 3. 社团架构 -->
      <section class="mb-16">
        <div class="text-center mb-10">
          <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
            社团架构
          </h2>
          <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
        </div>

        <!-- 社长和副社长 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-12">
          <div
            class="bg-white rounded-xl shadow-lg p-8 text-center animate-scale-in"
            style="animation-delay: 0.1s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-primary/10 flex items-center justify-center"
            >
              <i class="fa fa-user-tie text-4xl text-primary"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">社长</h3>
            <p class="text-gray-600 text-sm mb-4">
              负责社团整体规划和对外联络，统筹各部门工作
            </p>
            <div class="text-sm text-gray-500">
              <p>社长：待确定</p>
            </div>
          </div>

          <div
            class="bg-white rounded-xl shadow-lg p-8 text-center animate-scale-in"
            style="animation-delay: 0.2s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-secondary/10 flex items-center justify-center"
            >
              <i class="fa fa-users text-4xl text-secondary"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">副社长</h3>
            <p class="text-gray-600 text-sm mb-4">
              协助社长工作，负责内部管理和活动组织
            </p>
            <div class="text-sm text-gray-500">
              <p>副社长：待确定</p>
            </div>
          </div>
        </div>

        <!-- 三个部门 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <!-- 算法部 -->
          <div
            class="bg-white rounded-xl shadow-lg p-6 text-center animate-scale-in"
            style="animation-delay: 0.3s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-purple-100 flex items-center justify-center"
            >
              <i class="fa fa-brain text-4xl text-purple-500"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">算法部</h3>
            <p class="text-gray-600 text-sm mb-4">
              专注于算法类竞赛，如ACM、蓝桥杯等，培养算法思维和编程能力
            </p>
            <div class="text-sm text-gray-500">
              <p>部长：待确定</p>
              <p>副部长：待确定</p>
            </div>
          </div>

          <!-- 项目竞赛部 -->
          <div
            class="bg-white rounded-xl shadow-lg p-6 text-center animate-scale-in"
            style="animation-delay: 0.4s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-blue-100 flex items-center justify-center"
            >
              <i class="fa fa-project-diagram text-4xl text-blue-500"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">项目竞赛部</h3>
            <p class="text-gray-600 text-sm mb-4">
              负责项目类竞赛，如挑战杯、计算机设计大赛、物联网设计大赛等
            </p>
            <div class="text-sm text-gray-500">
              <p>部长：待确定</p>
              <p>副部长：待确定</p>
            </div>
          </div>

          <!-- 综合管理部 -->
          <div
            class="bg-white rounded-xl shadow-lg p-6 text-center animate-scale-in"
            style="animation-delay: 0.5s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-green-100 flex items-center justify-center"
            >
              <i class="fa fa-cogs text-4xl text-green-500"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">综合管理部</h3>
            <p class="text-gray-600 text-sm mb-4">
              负责社团日常管理、活动组织、宣传推广和对外联络工作
            </p>
            <div class="text-sm text-gray-500">
              <p>部长：待确定</p>
              <p>副部长：待确定</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 4. 指导老师 -->
      <section class="mb-16">
        <div class="text-center mb-10">
          <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
            指导老师
          </h2>
          <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
          <!-- 指导老师卡片 -->
          <div
            class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up"
            style="animation-delay: 0.1s"
          >
            <div class="relative">
              <img
                src="https://images.unsplash.com/photo-1568602471122-7832951cc4c5?q=80&w=1740&auto=format&fit=crop"
                alt="指导老师"
                class="w-full h-64 object-cover"
                loading="lazy"
              />
              <div
                class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-6"
              >
                <h3 class="text-xl font-bold text-white">指导老师</h3>
                <p class="text-white/80">待确定</p>
              </div>
            </div>
            <div class="p-6">
              <p class="text-gray-700 mb-4">
                我们正在联系相关专业的老师担任社团指导老师，为社团发展提供专业指导和支持。
              </p>
              <div class="flex justify-center gap-4">
                <a
                  href="#"
                  class="text-gray-500 hover:text-primary transition-colors"
                >
                  <i class="fa fa-envelope text-xl"></i>
                </a>
                <a
                  href="#"
                  class="text-gray-500 hover:text-primary transition-colors"
                >
                  <i class="fa fa-weixin text-xl"></i>
                </a>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 5. 社团文化 -->
      <section class="mb-16">
        <div class="text-center mb-10">
          <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
            社团文化
          </h2>
          <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <!-- 核心理念 -->
          <div
            class="bg-white rounded-xl shadow-lg p-8 animate-slide-up"
            style="animation-delay: 0.1s"
          >
            <div
              class="w-16 h-16 mb-6 bg-primary/10 rounded-full flex items-center justify-center mx-auto"
            >
              <i class="fa fa-lightbulb-o text-2xl text-primary"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-4 text-center">
              核心理念
            </h3>
            <ul class="space-y-3">
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >融合：将不同领域的知识融合创新</span
                >
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700">实践：通过项目实践学习技术</span>
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >包容：不设技术门槛，欢迎所有同学</span
                >
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700">成长：在实践中不断学习和进步</span>
              </li>
            </ul>
          </div>

          <!-- 社团活动 -->
          <div
            class="bg-white rounded-xl shadow-lg p-8 animate-slide-up"
            style="animation-delay: 0.2s"
          >
            <div
              class="w-16 h-16 mb-6 bg-primary/10 rounded-full flex items-center justify-center mx-auto"
            >
              <i class="fa fa-calendar text-2xl text-primary"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-4 text-center">
              社团活动
            </h3>
            <ul class="space-y-3">
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >不定期技术交流：根据需求安排分享会</span
                >
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700">项目实践：参与实际项目开发</span>
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700">竞赛准备：为各类比赛做准备</span>
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700">团建活动：不定期组织交流活动</span>
              </li>
            </ul>
          </div>

          <!-- 加入我们 -->
          <div
            class="bg-white rounded-xl shadow-lg p-8 animate-slide-up"
            style="animation-delay: 0.3s"
          >
            <div
              class="w-16 h-16 mb-6 bg-primary/10 rounded-full flex items-center justify-center mx-auto"
            >
              <i class="fa fa-user-plus text-2xl text-primary"></i>
            </div>
            <h3 class="text-xl font-bold text-dark mb-4 text-center">
              加入我们
            </h3>
            <ul class="space-y-3">
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >热爱科技创新，对技术有浓厚兴趣</span
                >
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700">无需任何技术基础，零门槛加入</span>
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >欢迎大一新生，特别是零基础同学</span
                >
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >善于学习，乐于分享，团队协作能力强</span
                >
              </li>
              <li class="flex items-start gap-3">
                <i class="fa fa-check-circle text-secondary mt-1"></i>
                <span class="text-gray-700"
                  >有责任心，能够积极参与社团活动</span
                >
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- 6. 联系我们 -->
      <section class="animate-slide-up">
        <div class="text-center mb-10">
          <h2 class="text-2xl font-bold text-dark mb-4">联系我们</h2>
          <div class="w-20 h-1 bg-primary mx-auto"></div>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-8">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <h3 class="text-xl font-bold text-dark mb-6">联系方式</h3>
              <ul class="space-y-4">
                <li class="flex items-start gap-4">
                  <div
                    class="w-10 h-10 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                  >
                    <i class="fa fa-phone text-primary"></i>
                  </div>
                  <div>
                    <h4 class="font-medium text-dark mb-1">社长电话</h4>
                    <p class="text-gray-600">138-XXXX-XXXX（社长）</p>
                  </div>
                </li>
                <li class="flex items-start gap-4">
                  <div
                    class="w-10 h-10 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                  >
                    <i class="fa fa-envelope text-primary"></i>
                  </div>
                  <div>
                    <h4 class="font-medium text-dark mb-1">社长邮箱</h4>
                    <p class="text-gray-600">社长邮箱待公布</p>
                  </div>
                </li>
                <li class="flex items-start gap-4">
                  <div
                    class="w-10 h-10 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                  >
                    <i class="fa fa-weixin text-primary"></i>
                  </div>
                  <div>
                    <h4 class="font-medium text-dark mb-1">社长微信</h4>
                    <p class="text-gray-600">社长微信待公布</p>
                  </div>
                </li>
              </ul>
            </div>
            <div>
              <h3 class="text-xl font-bold text-dark mb-6">关注我们</h3>
              <div class="bg-gray-50 p-6 rounded-lg">
                <div class="flex justify-center mb-6">
                  <div class="w-32 h-32 bg-white p-2 shadow-md">
                    <img
                      src="https://images.unsplash.com/photo-1550684376-efcbd6e3f031?q=80&w=1740&auto=format&fit=crop"
                      alt="社长微信二维码"
                      class="w-full h-full object-cover"
                      loading="lazy"
                    />
                  </div>
                </div>
                <p class="text-center text-gray-600 text-sm">
                  扫描二维码关注我们的微信公众号，了解更多社团活动和招新信息
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue"
import CommonNavbar from "../components/CommonNavbar.vue"
import CommonFooter from "../components/CommonFooter.vue"

// 滚动监听动画：元素进入视口时添加slide-up动画（保留原HTML交互）
onMounted(() => {
  const observerOptions = {
    root: null,
    rootMargin: "0px",
    threshold: 0.1,
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("animate-slide-up")
        observer.unobserve(entry.target)
      }
    })
  }, observerOptions)

  // 监听所有section元素（确保滚动时触发动画）
  document.querySelectorAll("section").forEach((section) => {
    observer.observe(section)
  })
})
</script>

<style scoped>
/* 1. 基础样式：确保页面最小高度和背景色 */
.about-page {
  background-color: #f8fafc;
}

/* 2. 自定义颜色类（适配CDN版Tailwind，替代tailwind.config） */
.bg-primary {
  background-color: #3b82f6;
}
.text-primary {
  color: #3b82f6;
}
.bg-secondary {
  background-color: #10b981;
}
.text-secondary {
  color: #10b981;
}
.bg-accent {
  background-color: #f59e0b;
}
.text-accent {
  color: #f59e0b;
}
.text-dark {
  color: #1e293b;
}

/* 3. 动画样式：移除@layer，CDN不支持（保留原HTML动画效果） */
.animate-fade-in {
  animation: fadeIn 0.8s ease-in-out;
}
.animate-slide-up {
  animation: slideUp 0.6s ease-out;
}
.animate-scale-in {
  animation: scaleIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
@keyframes scaleIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

/* 4. 响应式适配：修复移动端时间线布局 */
@media (max-width: 768px) {
  /* 移动端时间线轴线居中 */
  .relative > .absolute {
    left: 50% !important;
  }
  /* 修复移动端卡片间距 */
  .grid gap-8 > div {
    margin-bottom: 1rem;
  }
}

/* 5. Font Awesome图标样式：确保图标正常显示 */
.fa {
  display: inline-block;
  font-style: normal;
  font-variant: normal;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
}
</style>

<style scoped>
@media (max-width: 768px) {
  .about-page main {
    padding-top: 1rem !important;
  }

  /* 调整时间线样式 */
  .relative > div:nth-child(1) {
    left: 20px !important;
  }

  .relative > div:nth-child(2),
  .relative > div:nth-child(3),
  .relative > div:nth-child(4) {
    padding-left: 40px !important;
  }

  /* 调整卡片布局 */
  .grid-cols-1.md\:grid-cols-2,
  .grid-cols-1.md\:grid-cols-3 {
    grid-template-columns: 1fr !important;
  }
}
</style>
