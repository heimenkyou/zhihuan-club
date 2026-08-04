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
            我们是信息工程系专注技术实践与创新的年轻团队。从2024年成立至今，一直坚持<strong>“以赛促学，以创促研”</strong>，
            在竞赛和项目中动手实践、真实成长。无论你是想深耕技术，还是跨领域探索创新，这里都欢迎你加入。
          </p>
        </div>

        <div
          class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 overflow-hidden mobile:rounded-lg"
        >
          <div class="md:flex flex-col mobile:flex-col">
            <div class="mobile:w-full">
              <img
                src="@/assets/images/compuerCode.avif"
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
                智环学创融合协会成立于2024年，是信息工程系下属的学术科技类社团。我们专注于计算机相关学科竞赛与创新创业竞赛，致力于为热爱技术、渴望实践的同学提供一个交流、学习和成长的平台。
                <strong
                  >简单来说就是两件事: 打计算机类竞赛，搞创新创业项目</strong
                >
              </p>
              <p
                class="text-gray-700 leading-relaxed mb-3 mobile:text-sm mobile:mb-2"
              >
                从算法竞赛到“计算机设计大赛”、“挑战杯”，我们都在认真参与。如果你喜欢折腾技术、想把课上学的知识用起来，或者只是对做项目感兴趣，这里就是你的地方。
                <strong>学，就该动手学；创，就得真去创。</strong>
              </p>
              <p class="text-gray-700 leading-relaxed mobile:text-sm">
                社团目前设有算法部、项目资源部和综合管理部三大部门。我们不靠资历说话，只看行动和热情，只要你热爱技术、勇于尝试，这里都有属于你的舞台。
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

        <!-- 时间线容器 -->
        <div class="space-y-8 md:space-y-16">
          <!-- 2024年：社团成立 -->
          <div class="flex items-start animate-slide-up" style="animation-delay: 0.1s">
            <!-- 移动端时间线图标 -->
            <div class="md:hidden flex flex-col items-center mr-4 mt-2">
              <div class="w-8 h-8 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                <span class="text-sm font-bold">24</span>
              </div>
              <div class="w-0.5 h-48 bg-gray-200 mt-2"></div>
            </div>
            <!-- 桌面端时间线 -->
            <div class="hidden md:flex items-center w-full">
              <div class="w-1/2 pr-10 text-right">
                <h3 class="text-xl font-bold text-dark mb-2">社团成立</h3>
                <p class="text-gray-600">智环学创融合协会正式成立，由一群热爱科技创新的同学共同创建，开启社团发展之路</p>
              </div>
              <div class="flex flex-col items-center">
                <div class="w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                  <span class="font-bold">24</span>
                </div>
                <div class="w-0.5 h-24 bg-gray-200 mt-2"></div>
              </div>
              <div class="w-1/2 pl-10">
                <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4">
                  <h4 class="font-medium text-dark mb-2">2024年8月</h4>
                  <p class="text-gray-600 text-sm">零零散散凑了几个部门，完成社团注册</p>
                </div>
              </div>
            </div>
            <!-- 移动端内容 -->
            <div class="md:hidden flex-1">
              <div class="flex items-center mb-3">
                <el-icon class="text-primary mr-2"><i-ep-circle-check /></el-icon>
                <h3 class="text-lg font-bold text-dark">社团成立</h3>
              </div>
              <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4 mb-3">
                <h4 class="font-medium text-dark mb-2 text-primary">2024年8月</h4>
                <p class="text-gray-600 text-sm">零零散散凑了几个部门，完成社团注册</p>
              </div>
              <p class="text-gray-600 text-sm">智环学创融合协会正式成立，由一群热爱科技创新的同学共同创建，开启社团发展之路</p>
            </div>
          </div>

          <!-- 2024年：首次招新 -->
          <div class="flex items-start animate-slide-up" style="animation-delay: 0.2s">
            <!-- 移动端时间线图标 -->
            <div class="md:hidden flex flex-col items-center mr-4 mt-2">
              <div class="w-8 h-8 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                <el-icon class="text-sm"><i-ep-circle-check /></el-icon>
              </div>
              <div class="w-0.5 h-48 bg-gray-200 mt-2"></div>
            </div>
            <!-- 桌面端时间线 -->
            <div class="hidden md:flex items-center w-full">
              <div class="w-1/2 pr-10 text-right">
                <h3 class="text-xl font-bold text-dark mb-2">首次招新</h3>
                <p class="text-gray-600">社团开始第一次正式招新，期待更多热爱科技的同学加入我们，共同创造社团的未来</p>
              </div>
              <div class="flex flex-col items-center">
                <div class="w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                  <el-icon><i-ep-circle-check /></el-icon>
                </div>
                <div class="w-0.5 h-28 bg-gray-200 mt-2"></div>
              </div>
              <div class="w-1/2 pl-10">
                <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4">
                  <h4 class="font-medium text-dark mb-2">2024年9月</h4>
                  <p class="text-gray-600 text-sm">首次招新活动启动，面向大一新生，不设技术门槛</p>
                </div>
              </div>
            </div>
            <!-- 移动端内容 -->
            <div class="md:hidden flex-1">
              <div class="flex items-center mb-3">
                <el-icon class="text-primary mr-2"><i-ep-circle-check /></el-icon>
                <h3 class="text-lg font-bold text-dark">首次招新</h3>
              </div>
              <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4 mb-3">
                <h4 class="font-medium text-dark mb-2 text-primary">2024年9月</h4>
                <p class="text-gray-600 text-sm">首次招新活动启动，面向大一新生，不设技术门槛</p>
              </div>
              <p class="text-gray-600 text-sm">社团开始第一次正式招新，期待更多热爱科技的同学加入我们，共同创造社团的未来</p>
            </div>
          </div>

          <!-- 架构调整 -->
          <div class="flex items-start animate-slide-up" style="animation-delay: 0.3s">
            <!-- 移动端时间线图标 -->
            <div class="md:hidden flex flex-col items-center mr-4 mt-2">
              <div class="w-8 h-8 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                <i-fa6-solid-gears class="text-sm" />
              </div>
              <div class="w-0.5 h-48 bg-gray-200 mt-2"></div>
            </div>
            <!-- 桌面端时间线 -->
            <div class="hidden md:flex items-center w-full">
              <div class="w-1/2 pr-10 text-right">
                <h3 class="text-xl font-bold text-dark mb-2">变革与成长</h3>
                <p class="text-gray-700">经过半年多的发展，社团经历了一次重要的变革，为了更好的发展方向</p>
              </div>
              <div class="flex flex-col items-center">
                <div class="w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                  <i-fa6-solid-gears />
                </div>
                <div class="w-0.5 h-24 bg-gray-200 mt-2"></div>
              </div>
              <div class="w-1/2 pl-10">
                <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4">
                  <h4 class="font-medium text-dark mb-2 text-primary">2025年5月</h4>
                  <p class="text-gray-600 text-sm">进行了人员缩减和部门架构大调整，形成了现在的社团结构</p>
                </div>
              </div>
            </div>
            <!-- 移动端内容 -->
            <div class="md:hidden flex-1">
              <div class="flex items-center mb-3">
                <i-fa6-solid-gears class="text-primary mr-2" />
                <h3 class="text-lg font-bold text-dark">变革与成长</h3>
              </div>
              <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4 mb-3">
                <h4 class="font-medium text-dark mb-2 text-primary">2025年5月</h4>
                <p class="text-gray-600 text-sm">进行了人员缩减和部门架构大调整，形成了现在的社团结构</p>
              </div>
              <p class="text-gray-700 text-sm">经过半年多的发展，社团经历了一次重要的变革，为了更好的发展方向</p>
            </div>
          </div>

          <!-- 网站上线 -->
          <div class="flex items-start animate-slide-up" style="animation-delay: 0.3s">
            <!-- 移动端时间线图标 -->
            <div class="md:hidden flex flex-col items-center mr-4 mt-2">
              <div class="w-8 h-8 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                <i-fa6-solid-globe class="text-sm" />
              </div>
              <div class="w-0.5 h-48 bg-gray-200 mt-2"></div>
            </div>
            <!-- 桌面端时间线 -->
            <div class="hidden md:flex items-center w-full">
              <div class="w-1/2 pr-10 text-right">
                <h3 class="text-xl font-bold text-dark mb-2">数字化转型</h3>
                <p class="text-gray-600">为了更好地展示社团风采和成果，提升内部管理效率</p>
              </div>
              <div class="flex flex-col items-center">
                <div class="w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center shadow-lg">
                  <i-fa6-solid-globe />
                </div>
                <div class="w-0.5 h-24 bg-gray-200 mt-2"></div>
              </div>
              <div class="w-1/2 pl-10">
                <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4">
                  <h4 class="font-medium text-dark mb-2 text-primary">2025年9月</h4>
                  <p class="text-gray-600 text-sm">社团网站初步建立上线，非官方，仅是社团内成员团队自发学习开发以及内部使用</p>
                </div>
              </div>
            </div>
            <!-- 移动端内容 -->
            <div class="md:hidden flex-1">
              <div class="flex items-center mb-3">
                <i-fa6-solid-globe class="text-primary mr-2" />
                <h3 class="text-lg font-bold text-dark">数字化转型</h3>
              </div>
              <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4 mb-3">
                <h4 class="font-medium text-dark mb-2 text-primary">2025年9月</h4>
                <p class="text-gray-600 text-sm">社团网站初步建立上线，非官方，仅是社团内成员团队自发学习开发以及内部使用</p>
              </div>
              <p class="text-gray-600 text-sm">为了更好地展示社团风采和成果，提升内部管理效率</p>
            </div>
          </div>

          <!-- 未来展望 -->
          <div class="flex items-start animate-slide-up" style="animation-delay: 0.3s">
            <!-- 移动端时间线图标 -->
            <div class="md:hidden flex flex-col items-center mr-4 mt-2">
              <div class="w-8 h-8 rounded-full bg-secondary text-white flex items-center justify-center shadow-lg">
                <i-fa6-solid-star class="text-sm" />
              </div>
              <!-- 最后一个节点不需要连接线 -->
            </div>
            <!-- 桌面端时间线 -->
            <div class="hidden md:flex items-center w-full">
              <div class="w-1/2 pr-10 text-right">
                <h3 class="text-xl font-bold text-dark mb-2">未来可期</h3>
                <p class="text-gray-600">社团正在逐步发展中，我们将通过不定期的活动和项目实践，慢慢积累经验和成果，期待与你一起见证成长</p>
              </div>
              <div class="flex flex-col items-center">
                <div class="w-10 h-10 rounded-full bg-secondary text-white flex items-center justify-center shadow-lg">
                  <i-fa6-solid-star />
                </div>
                <!-- 最后一个节点不需要连接线 -->
              </div>
              <div class="w-1/2 pl-10">
                <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4">
                  <h4 class="font-medium text-dark mb-2 text-secondary">未来展望</h4>
                  <p class="text-gray-600 text-sm">期待在各类竞赛中崭露头角，成为学校有影响力的科技创新社团</p>
                </div>
              </div>
            </div>
            <!-- 移动端内容 -->
            <div class="md:hidden flex-1">
              <div class="flex items-center mb-3">
                <i-fa6-solid-star class="text-secondary mr-2" />
                <h3 class="text-lg font-bold text-dark">未来可期</h3>
              </div>
              <div class="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 p-4 mb-3">
                <h4 class="font-medium text-dark mb-2 text-secondary">未来展望</h4>
                <p class="text-gray-600 text-sm">期待在各类竞赛中崭露头角，成为学校有影响力的科技创新社团</p>
              </div>
              <p class="text-gray-600 text-sm">社团正在逐步发展中，我们将通过不定期的活动和项目实践，慢慢积累经验和成果，期待与你一起见证成长</p>
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
            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-8 text-center animate-scale-in"
            style="animation-delay: 0.1s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-primary/10 flex items-center justify-center"
            >
              <i-fa6-solid-id-card class="fa-id-card" />
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">社长</h3>
            <p class="text-gray-600 text-sm mb-4">
              负责社团整体规划和对外联络，统筹各部门工作
            </p>
            <div class="text-sm text-gray-500">
              <p>社长：王熠博</p>
            </div>
          </div>

          <div
            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-8 text-center animate-scale-in"
            style="animation-delay: 0.2s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 rounded-full bg-secondary/10 flex items-center justify-center"
            >
              <i-fa6-solid-user class="fa-user" />
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">副社长</h3>
            <p class="text-gray-600 text-sm mb-4">
              协助社长工作，负责内部管理和活动组织
            </p>
            <div class="text-sm text-gray-500">
              <p>副社长：张泽天</p>
            </div>
          </div>
        </div>

        <!-- 三个部门 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <!-- 算法部 -->
          <div
            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-6 text-center animate-scale-in"
            style="animation-delay: 0.3s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 flex items-center justify-center overflow-hidden group"
            >
              <el-image
                :src="algorithmImg"
                alt="算法部图标"
                class="w-full h-full object-cover cursor-zoom-in"
                :preview-src-list="[algorithmImg]"
                loading="lazy"
              />
              <div
                class="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"
              >
                <i-fa6-solid-magnifying-glass-plus class="text-white text-2xl" />
              </div>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">算法部</h3>
            <p class="text-gray-600 text-sm mb-4">
              专注于算法类竞赛，如ACM、蓝桥杯等，培养算法思维和编程能力
            </p>
            <div class="text-sm text-gray-500">
              <p>负责人：边韶涵</p>
            </div>
          </div>

          <!-- 项目竞赛部 -->
          <div
            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-6 text-center animate-scale-in"
            style="animation-delay: 0.4s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 flex items-center justify-center overflow-hidden group"
            >
              <el-image
                :src="projectCompetitionImg"
                alt="项目竞赛部图标"
                class="w-full h-full object-cover cursor-zoom-in"
                :preview-src-list="[projectCompetitionImg]"
                loading="lazy"
              />
              <div
                class="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"
              >
                <i-fa6-solid-magnifying-glass-plus class="text-white text-2xl" />
              </div>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">项目竞赛部</h3>
            <p class="text-gray-600 text-sm mb-4">
              负责项目类竞赛，如挑战杯、计算机设计大赛、物联网设计大赛等
            </p>
            <div class="text-sm text-gray-500">
              <p>负责人：赵楚一洋</p>
            </div>
          </div>

          <!-- 综合管理部 -->
          <div
            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-6 text-center animate-scale-in"
            style="animation-delay: 0.5s"
          >
            <div
              class="w-24 h-24 mx-auto mb-4 flex items-center justify-center overflow-hidden group"
            >
              <el-image
                :src="generalAdminImg"
                alt="综合管理部图标"
                class="w-full h-full object-cover cursor-zoom-in"
                :preview-src-list="[generalAdminImg]"
                loading="lazy"
              />
              <div
                class="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"
              >
                <i-fa6-solid-magnifying-glass-plus class="text-white text-2xl" />
              </div>
            </div>
            <h3 class="text-xl font-bold text-dark mb-3">综合管理部</h3>
            <p class="text-gray-600 text-sm mb-4">
              负责社团日常管理、活动组织、宣传推广和对外联络工作
            </p>
            <div class="text-sm text-gray-500">
              <p>负责人：杨铠硕</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 6. 联系我们 -->
      <section class="animate-slide-up">
        <div class="text-center mb-10">
          <h2 class="text-2xl font-bold text-dark mb-4">联系我们</h2>
          <div class="w-20 h-1 bg-primary mx-auto"></div>
        </div>

        <div
          class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-8 text-center"
        >
          <p class="text-gray-600">点击底部页脚的图标就行辣！😎👇</p>
        </div>
      </section>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup>
  import { onMounted } from 'vue'
  import CommonNavbar from '@/components/CommonNavbar.vue'
  import CommonFooter from '@/components/CommonFooter.vue'

  import algorithmImg from '@/assets/images/department-algorithm.webp'
  import projectCompetitionImg from '@/assets/images/department-project-competition.webp'
  import generalAdminImg from '@/assets/images/department-general-admin.webp'
  // 滚动监听动画：元素进入视口时添加slide-up动画（保留原HTML交互）
  onMounted(() => {
    const observerOptions = {
      root: null,
      rootMargin: '0px',
      threshold: 0.1,
    }

    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('animate-slide-up')
          observer.unobserve(entry.target)
        }
      })
    }, observerOptions)

    // 监听所有section元素（确保滚动时触发动画）
    document.querySelectorAll('section').forEach(section => {
      observer.observe(section)
    })
  })
</script>

<style scoped>
  /* 基础样式：确保页面最小高度和背景色 */
  .about-page {
    background-color: #f8fafc;
  }

  /* 自定义颜色类（适配CDN版Tailwind，替代tailwind.config） */
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

  /* 动画样式：移除@layer，CDN不支持（保留原HTML动画效果） */
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

  /* 响应式适配：修复移动端时间线布局 */
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

  @media (max-width: 768px) {
    .about-page main {
      padding-top: 1rem !important;
    }

    /* 调整时间线样式 */
    .relative > div:nth-child(1) {
      left: 20px !important;
    }

    .relative > div:nth-child(n + 2) {
      padding-left: 40px !important;
    }

    /* 调整卡片布局 */
    .grid-cols-1.md\:grid-cols-2,
    .grid-cols-1.md\:grid-cols-3 {
      grid-template-columns: 1fr !important;
    }
  }
</style>
