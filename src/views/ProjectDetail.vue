<template>
  <div class="project-showcase">
    <!-- 1. 顶部标题区（导航栏）：修复高度过高+与下方间距问题 -->
    <div class="top-header">
      <div class="top-header__container">
        <h1 class="main-title">智能校园导航系统</h1>
        <div class="top-meta">
          <div class="dev-time">开发时间：2022年09月 - 2023年03月</div>
          <div class="team-members-text">
            团队成员：张明（项目负责人）、李华（前端开发）、王芳（后端开发）、刘强（算法工程师）
          </div>
        </div>
      </div>
    </div>

    <!-- 2. 核心轮播图区 - 全新设计 -->
    <div class="project-container">
      <!-- 轮播图模块 -->
      <div class="project-section carousel-section">
        <h2 class="section-title">
          <el-icon><Picture /></el-icon>
          项目展示
        </h2>
        <!-- 轮播图全新配置 -->
        <div class="carousel-container">
          <el-carousel
            :interval="3000"
            :duration="800"
            :type="isMobile ? '' : 'card'"
            :height="isMobile ? '240px' : '450px'"
            indicator-position="none"
            indicator-class="custom-indicator"
            arrow="always"
            class="modern-carousel"
            @resize="handleCarouselResize"
          >
            <el-carousel-item
              v-for="item in carouselImages"
              :key="item.id"
              class="carousel-slide"
            >
              <div class="carousel-content-wrapper">
                <div class="carousel-image-container">
                  <img
                    :src="item.url"
                    :alt="item.caption"
                    class="carousel-image"
                    loading="lazy"
                  />
                </div>
                <div class="carousel-caption">
                  <h3 class="caption-title">
                    {{ item.title || item.caption }}
                  </h3>
                  <p class="caption-description">
                    {{ item.description || item.caption }}
                  </p>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </div>

    <!-- 主体内容区和底部CTA区保持不变 -->
    <div class="project-container">
      <!-- 3.1 技术栈模块 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Cpu /></el-icon>
          技术栈
        </h2>
        <div class="tech-tags-container">
          <el-tag
            v-for="(tech, index) in technologies"
            :key="tech.name"
            :type="tagTypes[index % tagTypes.length] as 'primary' | 'success' | 'warning' | 'info' | 'danger'"
            size="large"
            effect="light"
            class="tech-tag"
          >
            {{ tech.name }}
          </el-tag>
        </div>
      </div>

      <!-- 3.2 项目介绍模块 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Document /></el-icon>
          项目介绍
        </h2>
        <div class="project-intro-content">
          <p class="intro-paragraph">
            本项目是针对高校"找楼难、找教室难"痛点开发的智能导航应用，基于Flutter实现跨平台适配，整合高德地图API与ARCore技术，提供室内外一体化导航服务，定位精度达1-3米，支持教室状态实时同步与个性化路线规划。
          </p>
          <p class="intro-paragraph">
            项目历时6个月开发，累计注册用户5200+，导航请求超3万次，先后获校赛一等奖、省级金奖及全国大学生计算机设计大赛一等奖，并与当地科技企业达成合作推进商业化。
          </p>
          <h3 class="intro-subtitle">核心功能</h3>
          <ul class="intro-list">
            <li>精准定位：GPS+WiFi+蓝牙三重定位，室内1-3米/室外≤5米精度</li>
            <li>AR实景导航：摄像头实时叠加指引，复杂路口识别率95%+</li>
            <li>实时同步：对接教务系统，显示教室占用/图书馆座位状态</li>
            <li>个性路线：支持最短/避晒/无障碍等多维度路线选择</li>
          </ul>
        </div>
      </div>

      <!-- 3.3 团队成员模块 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><User /></el-icon>
          团队成员
        </h2>
        <div class="team-members-container">
          <div
            class="team-member-card"
            v-for="member in teamMembers"
            :key="member.id"
          >
            <span class="member-card__badge"></span>
            <h3 class="member-name">{{ member.name }}</h3>
            <p class="member-role">{{ member.role }}</p>
          </div>
        </div>
      </div>

      <!-- 3.4 获得奖项模块 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Trophy /></el-icon>
          获得奖项
        </h2>
        <div class="awards-timeline">
          <el-timeline :reverse="false" class="custom-timeline">
            <el-timeline-item
              v-for="(award, index) in awards"
              :key="index"
              :timestamp="award.date"
              placement="top"
              :type="award.type as 'primary' | 'success' | 'warning' | 'info' | 'danger'"
              class="timeline-item"
            >
              <el-card class="award-card">
                <h4 class="award-title">{{ award.title }}</h4>
                <p class="award-desc">{{ award.description }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>

    <!-- 4. 底部CTA区：修改联系我们部分 -->
    <div class="cta-section">
      <div class="cta-container">
        <h2 class="cta-title">对我们的项目感兴趣？</h2>
        <p class="cta-desc">欢迎联系我们获取更多信息或探讨合作机会</p>

        <!-- 删除按钮，添加联系电话列表 -->
        <div class="contact-phones">
          <div
            v-for="(phone, index) in contactPhones"
            :key="index"
            class="phone-item"
          >
            <el-icon class="phone-icon"><Phone /></el-icon>
            <span class="phone-label">联系电话：</span>
            <span class="phone-number">{{ phone }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue"
import {
  User,
  Cpu,
  Trophy,
  // 删除未使用的 Promotion 导入
  Document,
  Picture,
  // 添加Phone图标
  Phone,
} from "@element-plus/icons-vue"
import {
  ElCarousel,
  ElCarouselItem,
  ElTag,
  ElTimeline,
  ElTimelineItem,
  ElCard,
  // 移除ElButton的导入
  // ElButton,
} from "element-plus"

// 轮播图响应式处理
const isMobile = ref(false)
const handleCarouselResize = () => {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  handleCarouselResize()
  window.addEventListener("resize", handleCarouselResize)
})

onUnmounted(() => {
  window.removeEventListener("resize", handleCarouselResize)
})

// 优化轮播图数据结构，增加更多信息
type CarouselImage = {
  id: number
  url: string
  title: string
  description: string
  caption: string
}
const carouselImages = ref<CarouselImage[]>([
  {
    id: 1,
    url: "https://images.unsplash.com/photo-1551650975-87deedd944c3?ixlib=rb-4.0.3&auto=format&fit=crop&w=1974&q=80",
    title: "应用主界面",
    description: "简洁清晰的导航入口与功能分区设计",
    caption: "应用主界面展示",
  },
  {
    id: 2,
    url: "https://images.unsplash.com/photo-1550745165-9bc0b252726f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80",
    title: "AR导航功能",
    description: "摄像头实时叠加箭头指引，提供直观导航体验",
    caption: "AR导航功能演示",
  },
  {
    id: 3,
    url: "https://images.unsplash.com/photo-1547082299-de196ea013d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80",
    title: "室内导航",
    description: "教学楼内精准定位与路径规划，解决找教室难题",
    caption: "室内导航效果",
  },
  {
    id: 4,
    url: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80",
    title: "个性化设置",
    description: "支持路线偏好与界面风格自定义，满足不同用户需求",
    caption: "用户个性化设置",
  },
  {
    id: 5,
    url: "https://images.unsplash.com/photo-1563986768609-322da1557225?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80",
    title: "数据统计",
    description: "实时显示导航数据与用户行为分析",
    caption: "数据统计与分析界面",
  },
])

// 其他模块数据：保持原结构不变
type TeamMember = {
  id: number
  name: string
  role: string
}
const teamMembers = ref<TeamMember[]>([
  { id: 1, name: "张明", role: "项目负责人" },
  { id: 2, name: "李华", role: "前端开发" },
  { id: 3, name: "王芳", role: "后端开发" },
  { id: 4, name: "刘强", role: "算法工程师" },
])

type Technology = { name: string }
const technologies = ref<Technology[]>([
  { name: "Flutter" },
  { name: "Firebase" },
  { name: "高德地图API" },
  { name: "ARCore" },
  { name: "Dart" },
  { name: "GPS/WiFi定位" },
])

const tagTypes = ref<
  Array<"primary" | "success" | "warning" | "info" | "danger">
>(["primary", "info", "success", "warning", "danger", "primary"])

type Award = {
  date: string
  title: string
  description: string
  type: "primary" | "success" | "warning" | "info" | "danger"
}
const awards = ref<Award[]>([
  {
    date: "2023年3月",
    title: "校赛一等奖",
    description: "获得学校科技创新大赛一等奖，获校级资金支持",
    type: "primary",
  },
  {
    date: "2023年5月",
    title: "省级比赛金奖",
    description: "在省级大学生创新创业大赛中获得金奖，进入全国赛",
    type: "success",
  },
  {
    date: "2023年7月",
    title: "全国大赛一等奖",
    description: "荣获全国大学生计算机设计大赛一等奖，获行业关注",
    type: "warning",
  },
  {
    date: "2023年9月",
    title: "校企合作认证",
    description: "与当地科技企业达成合作协议，推进项目商业化落地",
    type: "danger",
  },
])
// 添加联系电话数组数据
const contactPhones = ref<string[]>(["138-1234-5678", "139-8765-4321"])
</script>

<style scoped lang="scss">
// 1. 基础变量：保持原定义
$color-primary: #165dff;
$color-primary-light: #e8f3ff;
$color-primary-hover: #0e4bdb;
$color-neutral-100: #f5f7fa;
$color-neutral-200: #e5e6eb;
$color-neutral-600: #4e5969;
$color-neutral-900: #1d2129;
$shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.06);
$shadow-md: 0 4px 16px rgba(0, 0, 0, 0.12);
$shadow-lg: 0 8px 24px rgba(0, 0, 0, 0.15);
$radius-sm: 8px;
$radius-md: 12px;
$radius-lg: 16px;
$radius-full: 9999px;
$transition-base: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
$transition-slow: all 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);
$spacing-xs: 8px;
$spacing-sm: 16px;
$spacing-md: 24px;
$spacing-lg: 32px;
$font-size-xs: 0.8rem;
$font-size-sm: 0.9rem;
$font-size-base: 1rem;
$font-size-md: 1.1rem;
$font-size-lg: 1.3rem;
$font-size-xl: 1.8rem;
$font-weight-medium: 500;
$font-weight-bold: 600;

// 2. 全局样式：保持原定义
.project-showcase {
  font-family: "Inter", "Microsoft YaHei", system-ui, sans-serif;
  color: $color-neutral-600;
  background-color: $color-neutral-100;
  min-height: 100vh;
  line-height: 1.6;
  padding-bottom: $spacing-lg;
}

// 3. 顶部标题区（导航栏）：核心修复——降低高度+增加底部间距
.top-header {
  background-color: #fff;
  box-shadow: $shadow-sm;
  // 修复高度过高：PC端减小上下内边距，手机端进一步减小
  padding: $spacing-md 0; // PC端：24px上下
  margin-bottom: $spacing-md; // 修复与下方间距：增加底部margin 24px

  // 手机端适配：进一步减小内边距和字体，避免占1/3屏幕
  @media (max-width: 768px) {
    padding: $spacing-sm 0; // 手机端：16px上下
  }

  &__container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 $spacing-sm;
    text-align: center;
  }

  .main-title {
    font-size: $font-size-xl;
    font-weight: $font-weight-bold;
    color: $color-neutral-900;
    margin-bottom: $spacing-xs;
    letter-spacing: -0.02em;

    // 手机端：减小字体，避免撑开高度
    @media (max-width: 480px) {
      font-size: 1.5rem; // 从1.3rem调整为1.5rem，平衡可读性和高度
    }
  }

  .top-meta {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    gap: $spacing-sm;
    font-size: $font-size-sm;
    color: $color-neutral-600;

    // 手机端：减小字体+垂直排列，避免横向溢出
    @media (max-width: 768px) {
      flex-direction: column;
      gap: $spacing-xs;
      padding: 0 $spacing-xs;
      font-size: 0.85rem; // 从0.9rem减小到0.85rem
    }
  }

  .dev-time {
    padding: $spacing-xs $spacing-sm;
    background-color: $color-primary-light;
    color: $color-primary;
    border-radius: $radius-full;
    font-weight: $font-weight-medium;

    // 手机端：减小内边距，避免按钮过宽
    @media (max-width: 480px) {
      padding: 6px 12px;
      font-size: $font-size-xs;
    }
  }

  .team-members-text {
    line-height: 1.5;

    // 手机端：强制换行，避免文字过长撑开高度
    @media (max-width: 480px) {
      white-space: pre-line;
      font-size: $font-size-xs; // 从0.85rem减小到0.8rem
    }
  }
}

// 4. 主体容器：保持原定义
.project-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 $spacing-sm;
}

// 5. 通用模块样式：给轮播图模块单独加顶部间距（双重保障）
.project-section {
  margin-bottom: $spacing-lg;
  animation: fadeIn 0.8s ease forwards;
  opacity: 0;

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(15px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  // 轮播图模块：额外增加顶部间距，确保与导航栏不拥挤
  &.carousel-section {
    margin-top: $spacing-sm;
  }

  .section-title {
    font-size: $font-size-lg;
    font-weight: $font-weight-bold;
    color: $color-neutral-900;
    margin-bottom: $spacing-md;
    display: flex;
    align-items: center;
    gap: $spacing-xs;
    padding-bottom: $spacing-xs;
    border-bottom: 1px solid $color-neutral-200;

    .el-icon {
      font-size: $font-size-md;
      color: $color-primary;
    }

    @media (max-width: 480px) {
      font-size: $font-size-md;
    }
  }
}

// 6. 轮播图核心样式 - 优化移动端显示
.project-carousel {
  margin-top: $spacing-sm;
  // 确保轮播图容器自身居中
  display: flex;
  justify-content: center;

  // 卡片式轮播：PC端样式保持不变
  &.el-carousel--card {
    .el-carousel__container {
      // 修复内边距：保持一致的左右内边距
      padding: 0 $spacing-sm !important;

      // 大屏PC端：适当调整内边距
      @media (min-width: 1200px) {
        padding: 0 $spacing-md !important;
      }

      // 手机端：取消内边距，全屏显示
      @media (max-width: 768px) {
        padding: 0 !important;
      }
    }

    // 轮播项宽度：优化为60%，确保更好的对称性
    .carousel-item {
      width: 60% !important; // 从58%调整为60%，提高对称性
      opacity: 0.7;
      transition: $transition-slow;
      border-radius: $radius-sm !important;
      overflow: hidden !important;
      box-shadow: $shadow-sm;
      // 确保左右卡片对称
      margin: 0 auto;

      // 激活态：优化缩放效果，确保居中对称
      &.is-active {
        opacity: 1;
        transform: scale(1.02); // 从1.05调整为1.02，减小缩放比例
        box-shadow: $shadow-md;
        z-index: 10;
        // 确保激活卡片绝对居中
        position: relative;
        left: 0 !important;
      }

      // 手机端：恢复100%宽度，全屏显示
      @media (max-width: 768px) {
        width: 100% !important;
        opacity: 1 !important;
        transform: none !important;
      }
    }
  }

  // 移动端轮播图特定样式（非卡片式）
  &.el-carousel:not(.el-carousel--card) {
    .el-carousel__container {
      padding: 0 !important;
    }

    .carousel-item {
      width: 100% !important;
      opacity: 1 !important;
      border-radius: $radius-md !important;
      overflow: hidden !important;
      box-shadow: $shadow-sm;
    }

    // 确保非激活状态的轮播项完全隐藏
    .el-carousel__item--in-active {
      display: none !important;
    }
  }

  // 轮播箭头：优化移动端箭头大小和位置
  .el-carousel__arrow {
    width: 40px;
    height: 40px;
    border-radius: $radius-full;
    background-color: rgba(255, 255, 255, 0.9);
    color: $color-primary;
    box-shadow: $shadow-sm;
    // 调整箭头位置，确保与卡片垂直居中对齐
    top: 50% !important;
    transform: translateY(-50%) !important;

    &:hover {
      background-color: #fff;
      transform: translateY(-50%) scale(1.1) !important;
    }

    @media (max-width: 768px) {
      width: 28px;
      height: 28px;
      font-size: 12px;
    }
  }
}

// 图片容器：优化移动端图片显示
.carousel-image-wrap {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  // 关键：为移动端设置固定的宽高比
  @media (max-width: 768px) {
    height: 100%;
    width: 100%;
  }

  .carousel-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.8s ease;
    // 确保图片大小固定，无论原始尺寸如何
    @media (max-width: 768px) {
      width: 100%;
      height: 100%;
      min-height: 200px;
      // 使用object-position确保图片重点内容显示
      object-position: center center;
    }

    @media (min-width: 768px) {
      &:hover {
        transform: scale(1.02);
      }
    }
  }
}

// 文字描述：优化移动端文字与图片的关系
.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.4));
  color: #fff;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  text-align: center;
  line-height: 1.5;
  border-bottom-left-radius: $radius-sm;
  border-bottom-right-radius: $radius-sm;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;

  // 移动端优化：紧贴图片，减少高度
  @media (max-width: 768px) {
    padding: 6px 10px;
    font-size: $font-size-xs;
    // 减少文字区域高度
    line-height: 1.4;
    // 增加透明度，让文字更融入图片
    background: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.2));
  }
}

// 轮播指示器：优化移动端导航条样式
.custom-indicator {
  bottom: 8px !important; // 稍微上移，避免与文字重叠
  z-index: 20 !important;

  // 移动端指示器细化
  @media (max-width: 768px) {
    bottom: 6px !important;
  }

  .el-carousel__indicator-btn {
    width: 12px;
    height: 3px; // 细化导航条
    border-radius: $radius-sm;
    background-color: rgba(255, 255, 255, 0.6);
    margin: 0 4px; // 减小间距

    // 移动端进一步细化
    @media (max-width: 768px) {
      width: 10px;
      height: 2px; // 更细的导航条
      margin: 0 3px; // 更小的间距
    }

    &.is-active {
      width: 28px;
      background-color: #fff;
      // 移动端激活状态也更细
      @media (max-width: 768px) {
        width: 20px;
        height: 2px;
      }
    }
  }
}

// 7. 技术栈模块：保持原样式
.tech-tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
  padding: $spacing-md $spacing-sm;
  background-color: #fff;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;

  .tech-tag {
    padding: $spacing-xs $spacing-sm;
    border-radius: $radius-full;
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
    transition: $transition-base;

    &:hover {
      transform: translateY(-2px);
      box-shadow: $shadow-sm;
    }

    @media (max-width: 480px) {
      padding: 6px 12px;
      font-size: $font-size-xs;
    }
  }
}

// 8. 项目介绍模块：保持原样式
.project-intro-content {
  background-color: #fff;
  padding: $spacing-md $spacing-sm;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;
  font-size: $font-size-base;

  @media (max-width: 480px) {
    padding: $spacing-sm $spacing-xs;
    font-size: $font-size-sm;
  }

  .intro-paragraph {
    margin-bottom: $spacing-sm;
    line-height: 1.7;

    &:last-of-type {
      margin-bottom: $spacing-md;
    }
  }

  .intro-subtitle {
    font-size: $font-size-md;
    font-weight: $font-weight-bold;
    color: $color-neutral-900;
    margin-bottom: $spacing-xs;
  }

  .intro-list {
    margin-left: $spacing-sm;
    line-height: 1.7;

    li {
      margin-bottom: $spacing-xs;
      list-style-type: disc;
    }
  }
}

// 9. 团队成员模块：保持原样式
.team-members-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: $spacing-sm;

  @media (max-width: 768px) {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }

  @media (max-width: 480px) {
    grid-template-columns: repeat(2, 1fr);
    gap: $spacing-xs;
  }
}

.team-member-card {
  background-color: #fff;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;
  padding: $spacing-md $spacing-sm;
  transition: $transition-base;
  position: relative;
  text-align: center;

  &:hover {
    transform: translateY(-4px);
    box-shadow: $shadow-md;
  }

  .member-card__badge {
    position: absolute;
    top: 0;
    left: 0;
    width: 3px;
    height: 100%;
    background-color: $color-primary;
    border-top-left-radius: $radius-sm;
    border-bottom-left-radius: $radius-sm;
  }

  .member-name {
    font-size: $font-size-md;
    font-weight: $font-weight-bold;
    color: $color-neutral-900;
    margin-bottom: $spacing-xs;
  }

  .member-role {
    color: $color-primary;
    font-weight: $font-weight-medium;
    font-size: $font-size-sm;
  }

  @media (max-width: 480px) {
    padding: $spacing-sm $spacing-xs;
    .member-name {
      font-size: $font-size-base;
    }
    .member-role {
      font-size: $font-size-xs;
    }
  }
}

// 10. 获得奖项模块：保持原样式
.awards-timeline {
  background-color: #fff;
  padding: $spacing-md $spacing-sm;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;
}

.custom-timeline {
  padding-left: $spacing-xs;

  .timeline-item {
    margin-bottom: $spacing-md;

    &:last-child {
      margin-bottom: 0;
    }

    .el-timeline-item__timestamp {
      color: $color-neutral-600;
      font-size: $font-size-sm;
      font-weight: $font-weight-medium;
      margin-bottom: $spacing-xs;
    }

    .el-timeline-item__node {
      width: 12px;
      height: 12px;
      background-color: $color-primary;
      border-color: $color-primary;
    }

    .el-timeline-item__line {
      width: 2px;
      background-color: $color-neutral-200;
    }
  }

  @media (max-width: 480px) {
    padding-left: 8px;
    .el-timeline-item__timestamp {
      font-size: $font-size-xs;
    }
    .el-timeline-item__node {
      width: 10px;
      height: 10px;
    }
  }
}

.award-card {
  border: none;
  box-shadow: $shadow-sm;
  border-radius: $radius-md;
  padding: $spacing-sm $spacing-sm;
  transition: $transition-base;

  &:hover {
    box-shadow: $shadow-md;
  }

  .award-title {
    font-size: $font-size-base;
    font-weight: $font-weight-bold;
    color: $color-neutral-900;
    margin-bottom: $spacing-xs;
  }

  .award-desc {
    font-size: $font-size-sm;
    color: $color-neutral-600;
    line-height: 1.5;
    margin-bottom: 0;
  }

  @media (max-width: 480px) {
    padding: $spacing-xs $spacing-xs;
    .award-title {
      font-size: $font-size-sm;
    }
    .award-desc {
      font-size: $font-size-xs;
    }
  }
}

// 11. 底部CTA区：保持原样式
.cta-section {
  background: linear-gradient(135deg, $color-primary, $color-primary-hover);
  color: #fff;
  text-align: center;
  padding: $spacing-lg $spacing-sm; // 保持原内边距
  margin: $spacing-lg auto $spacing-lg; // 保持原外边距
  border-radius: $radius-lg;
  max-width: 1400px;
  box-shadow: $shadow-md;

  @media (max-width: 480px) {
    padding: $spacing-md $spacing-xs;
    margin: $spacing-md auto $spacing-sm;
    border-radius: $radius-md;
  }
}

.cta-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 $spacing-sm;
}

.cta-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-bold;
  margin-bottom: $spacing-xs;

  @media (max-width: 480px) {
    font-size: $font-size-lg;
  }
}

.cta-desc {
  font-size: $font-size-base;
  opacity: 0.9;
  margin-bottom: $spacing-md;

  @media (max-width: 480px) {
    font-size: $font-size-sm;
    margin-bottom: $spacing-sm;
  }
}

.cta-button {
  padding: $spacing-xs $spacing-lg;
  font-size: $font-size-base;
  font-weight: $font-weight-bold;
  background-color: #fff;
  color: $color-primary;
  border-radius: $radius-full;
  transition: $transition-base;

  &:hover {
    background-color: $color-neutral-100;
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  }

  @media (max-width: 480px) {
    padding: 10px 28px;
    font-size: $font-size-sm;
    width: 80%;
  }
}
// 调整CTA区域样式，保持框架大小不变
.cta-section {
  background: linear-gradient(135deg, $color-primary, $color-primary-hover);
  color: #fff;
  text-align: center;
  padding: $spacing-lg $spacing-sm;
  margin: $spacing-lg auto $spacing-lg;
  border-radius: $radius-lg;
  max-width: 1400px;
  box-shadow: $shadow-md;

  @media (max-width: 480px) {
    padding: $spacing-md $spacing-xs;
    margin: $spacing-md auto $spacing-sm;
    border-radius: $radius-md;
  }
}

// 联系电话列表样式
.contact-phones {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-sm; // 电话之间的间距
  margin-top: $spacing-md; // 保持与上方文本的距离
  margin-bottom: $spacing-md; // 保持底部间距，确保框架大小不变

  @media (max-width: 480px) {
    margin-top: $spacing-sm;
    margin-bottom: $spacing-sm;
  }
}

// 单个电话项样式
.phone-item {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-md;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: $radius-full;
  backdrop-filter: blur(5px);
  transition: all 0.3s ease;
  min-width: 200px;

  &:hover {
    background-color: rgba(255, 255, 255, 0.15);
    transform: translateY(-2px);
  }

  @media (max-width: 480px) {
    padding: 8px 20px;
    min-width: 180px;
  }
}

// 电话图标样式
.phone-icon {
  font-size: $font-size-md;
  color: $color-neutral-100;
}

// 电话标签样式
.phone-label {
  font-size: $font-size-sm;
  opacity: 0.9;
}

// 电话号码样式
.phone-number {
  font-size: $font-size-base;
  font-weight: $font-weight-bold;
  color: #fff;
}

// 删除原按钮样式（如果不需要可以完全删除这个类）
.cta-button {
  display: none; // 隐藏按钮
}
</style>
