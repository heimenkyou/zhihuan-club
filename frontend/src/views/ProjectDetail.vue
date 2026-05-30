<template>
  <div class="project-showcase">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p class="loading-text">正在加载项目详情...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-container">
      <div class="error-icon">⚠️</div>
      <p class="error-text">{{ error }}</p>
      <button class="retry-btn" @click="fetchProjectDetail">重试</button>
    </div>

    <!-- 项目详情内容 -->
    <template v-else-if="projectDetail">
      <div class="top-header">
        <div class="top-header__container">
          <!-- 修改顶部标题区域的flex布局 -->
          <div
            style="
              display: flex;
              align-items: center;
              justify-content: space-between;
              margin-bottom: 8px;
            "
          >
            <!-- 返回按钮 - 移除margin: auto并保留绝对定位 -->
            <button
              style="
                width: 40px;
                height: 40px;
                border-radius: 50%;
                border: 1px solid #e5e6eb;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #4e5969;
                background: transparent;
                cursor: pointer;
              "
              @click="router.back()"
              aria-label="返回项目列表"
            >
              <el-icon style="font-size: 16px"><ArrowLeft /></el-icon>
            </button>

            <!-- 标题 - 使用flex容器包裹确保完美居中 -->
            <div style="flex: 1; display: flex; justify-content: center">
              <h1
                style="
                  margin: 0;
                  text-align: center;
                  font-size: 1.8rem;
                  font-weight: 600;
                  color: #1d2129;
                "
              >
                {{ projectDetail.title || '项目详情' }}
              </h1>
            </div>

            <!-- 占位元素保持标题居中 -->
            <div style="width: 40px"></div>
          </div>

          <!-- 元数据区域 -->
          <div class="top-meta">
            <div class="dev-time">
              开发时间：{{ projectDetail.timeRange || '-' }}
            </div>
            <div class="category-text">
              项目分类：{{ projectDetail.category || '-' }}
            </div>
            <div class="team-members-text">
              团队成员：{{
                formatTeamMembers(projectDetail.teamDivisions || [])
              }}
            </div>
          </div>
        </div>
      </div>

      <!-- 2. 核心轮播图区 -->
      <div class="project-container">
        <div class="project-section carousel-section">
          <h2 class="section-title">
            <el-icon><Picture /></el-icon>
            项目展示
          </h2>
          <div class="carousel-container">
            <el-carousel
              :interval="3000"
              :duration="800"
              :type="isMobile ? '' : 'card'"
              :height="isMobile ? '240px' : '450px'"
              indicator-position="none"
              indicator-class="custom-indicator"
              arrow="always"
              class="project-carousel"
              @resize="handleCarouselResize"
            >
              <template
                v-if="
                  projectDetail.mediaResources &&
                  projectDetail.mediaResources.length > 0
                "
              >
                <el-carousel-item
                  v-for="item in projectDetail.mediaResources"
                  :key="item.id"
                  class="carousel-item"
                >
                  <div class="carousel-image-wrap bg-gray-100">
                    <el-image
                      :src="item.url"
                      :alt="item.title || '项目图片'"
                      class="carousel-image cursor-zoom-in"
                      :preview-src-list="previewImageList"
                      :initial-index="projectDetail.mediaResources.findIndex(resource => resource.id === item.id)"
                      :preview-teleported="true"
                      fit="cover"
                    />
                    <div class="carousel-caption">
                      <h3 class="caption-title">
                        {{ item.title || '项目图片' }}
                      </h3>
                      <p class="caption-description">
                        {{ item.description || '' }}
                      </p>
                    </div>
                  </div>
                </el-carousel-item>
              </template>
              <template v-else>
                <el-carousel-item class="carousel-item">
                  <div class="empty-image">
                    <span class="empty-text">暂无项目图片</span>
                  </div>
                </el-carousel-item>
              </template>
            </el-carousel>
          </div>
        </div>
      </div>

      <!-- 主体内容区 -->
      <div class="project-container">
        <!-- 3.1 技术栈模块 -->
        <div class="project-section">
          <h2 class="section-title">
            <el-icon><Cpu /></el-icon>
            技术栈
          </h2>
          <div class="tech-tags-container">
            <template
              v-if="
                projectDetail &&
                projectDetail.techStackTags &&
                projectDetail.techStackTags.length > 0
              "
            >
              <el-tag
                v-for="(tech, index) in projectDetail.techStackTags"
                :key="index"
                :type="tagTypes[index % tagTypes.length] as 'primary' | 'success' | 'warning' | 'info' | 'danger'"
                size="large"
                effect="light"
                class="tech-tag"
              >
                {{ tech }}
              </el-tag>
            </template>
            <template v-else>
              <p class="text-gray-500">暂无技术栈信息</p>
            </template>
          </div>
        </div>

        <!-- 3.2 项目介绍模块 -->
        <div class="project-section">
          <h2 class="section-title">
            <el-icon><Document /></el-icon>
            项目介绍
          </h2>
          <div class="project-intro-content">
            <template v-if="projectDetail.descriptionMd">
              <MdPreview :modelValue="projectDetail.descriptionMd"></MdPreview>
            </template>
            <template v-else>
              <p class="text-gray-500">暂无项目介绍</p>
            </template>
          </div>
        </div>

        <!-- 3.3 团队成员模块 -->
        <div class="project-section">
          <h2 class="section-title">
            <el-icon><User /></el-icon>
            团队分工
          </h2>
          <div class="team-members-container">
            <template
              v-if="
                projectDetail.teamDivisions &&
                projectDetail.teamDivisions.length > 0
              "
            >
              <div
                class="team-member-card"
                v-for="(member, index) in projectDetail.teamDivisions"
                :key="`${member.name || 'member'}-${index}`"
              >
                <span class="member-card__badge"></span>
                <h3 class="member-name">{{ member.name || '匿名成员' }}</h3>
                <p class="member-role">{{ member.role || '暂无职责描述' }}</p>
              </div>
            </template>
            <template v-else>
              <p class="text-gray-500">暂无团队成员信息</p>
            </template>
          </div>
        </div>

        <!-- 3.4 获得奖项模块 -->
        <div
          class="project-section"
          v-if="projectDetail.awards && projectDetail.awards.length > 0"
        >
          <h2 class="section-title">
            <el-icon><Trophy /></el-icon>
            获得奖项
          </h2>
          <div class="awards-timeline">
            <el-timeline :reverse="false" class="custom-timeline">
              <el-timeline-item
                v-for="award in projectDetail.awards"
                :key="award.id"
                :timestamp="String(award.awardDate)"
                placement="top"
                :type="getAwardType(award.awardLevel)"
                class="timeline-item"
              >
                <el-card class="award-card">
                  <h4 class="award-title">
                    {{ `${award.competitionName} - ${award.awardLevel}` }}
                  </h4>
                  <p class="award-desc">
                    赛道：{{ award.competitionTrack }}<br />
                    级别：{{ award.competitionLevel }}<br />
                    获奖者：{{ award.winners.join(', ') }} ({{ award.year }}年)
                  </p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </div>

      <!-- 底部提示 -->
      <div class="bottom-end-notice">
        <div class="end-text">
          <el-icon><Finished /></el-icon>
          已经到底了，感谢您的浏览
        </div>
      </div>
    </template>
    <CommonFooter />
  </div>
</template>

<script setup lang="ts">
  // 添加必要的导入
  import CommonFooter from '@/components/CommonFooter.vue'
  import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import {
    User,
    Cpu,
    Trophy,
    Document,
    Picture,
    Finished,
    ArrowLeft,
  } from '@element-plus/icons-vue'
  import {
    ElCarousel,
    ElCarouselItem,
    ElTag,
    ElTimeline,
    ElTimelineItem,
    ElCard,
    ElImage,
  } from 'element-plus'
  import { getProjectDetail } from '@/services/projectService'
  import type { ProjectDetail as ProjectDetailType } from '@/services/projectService'

  // 路由相关
  const route = useRoute()
  const router = useRouter()
  const projectId = computed(() => {
    const id = route.query.id
    return id ? String(id) : ''
  })

  // 响应式数据
  const isMobile = ref(false)
  const loading = ref(false)
  const error = ref('')
  const projectDetail = ref<ProjectDetailType | null>(null)

  // 技术标签类型数组
  const tagTypes = ref<
    Array<'primary' | 'success' | 'warning' | 'info' | 'danger'>
  >(['primary', 'info', 'success', 'warning', 'danger', 'primary'])

  // 获取预览图片列表
  const previewImageList = computed(() => {
    return projectDetail.value?.mediaResources?.map(resource => resource.url) || []
  })

  // 轮播图响应式处理
  const handleCarouselResize = () => {
    isMobile.value = window.innerWidth < 768
  }

  // 格式化团队成员显示
  const formatTeamMembers = (
    members?: Array<{ name?: string; role?: string }>
  ) => {
    if (!members || members.length === 0) {
      return '暂无团队成员信息'
    }
    return members
      .map(
        member => `${member.name || '匿名成员'}（${member.role || '暂无职责'}）`
      )
      .join('、')
  }

  // 获取奖项类型对应的Element Plus样式
  const getAwardType = (
    type?: string
  ): 'primary' | 'success' | 'warning' | 'info' | 'danger' => {
    const typeMap: Record<
      string,
      'primary' | 'success' | 'warning' | 'info' | 'danger'
    > = {
      primary: 'primary',
      success: 'success',
      warning: 'warning',
      info: 'info',
      danger: 'danger',
      // 映射奖项等级到颜色
      一等奖: 'success',
      金奖: 'warning',
      二等奖: 'primary',
      银奖: 'primary',
      三等奖: 'info',
      铜奖: 'info',
      优秀奖: 'danger',
    }
    return typeMap[type || 'primary'] || 'primary'
  }

  // 获取项目详情数据
  const fetchProjectDetail = async () => {
    if (!projectId.value) {
      error.value = '项目ID不存在'
      return
    }

    loading.value = true
    error.value = ''

    try {
      // 使用修改后的API请求
      const data = await getProjectDetail(projectId.value)
      projectDetail.value = data
    } catch (err) {
      error.value =
        err instanceof Error ? err.message : '获取项目详情失败，请稍后重试'
      console.error('获取项目详情失败:', err)
      // 在控制台打印更详细的错误信息，帮助调试
      console.log('完整错误对象:', JSON.stringify(err, null, 2))
    } finally {
      loading.value = false
    }
  }

  // 生命周期钩子
  onMounted(() => {
    handleCarouselResize()
    window.addEventListener('resize', handleCarouselResize)
    fetchProjectDetail()
  })

  onUnmounted(() => {
    window.removeEventListener('resize', handleCarouselResize)
  })

  // 监听路由参数变化，重新获取数据
  watch(
    () => route.query.id,
    newId => {
      if (newId) {
        fetchProjectDetail()
      }
    }
  )
</script>

<style scoped lang="scss">
  // 基础变量：保持原定义
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

  // 全局样式：保持原定义
  .project-showcase {
    font-family: 'Inter', 'Microsoft YaHei', system-ui, sans-serif;
    color: $color-neutral-600;
    background-color: $color-neutral-100;
    min-height: 100vh;
    line-height: 1.6;
    padding-bottom: $spacing-lg;
  }

  .loading-container,
  .error-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 80vh;
    padding: $spacing-lg;
    text-align: center;
  }

  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 3px solid $color-neutral-200;
    border-top-color: $color-primary;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: $spacing-md;
  }

  .loading-text,
  .error-text {
    font-size: $font-size-base;
    color: $color-neutral-600;
  }

  .error-icon {
    font-size: 48px;
    margin-bottom: $spacing-md;
  }

  .retry-btn {
    margin-top: $spacing-md;
    padding: $spacing-xs $spacing-md;
    background-color: $color-primary;
    color: white;
    border: none;
    border-radius: $radius-full;
    cursor: pointer;
    font-size: $font-size-sm;
    transition: $transition-base;

    &:hover {
      background-color: $color-primary-hover;
      transform: translateY(-2px);
      box-shadow: $shadow-sm;
    }
  }

  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }

  // 顶部标题区
  .top-header {
    background-color: #fff;
    box-shadow: $shadow-sm;
    padding: $spacing-md 0;
    margin-bottom: $spacing-md;

    .back-to-list-container {
      margin-bottom: $spacing-md;
      text-align: left;
    }

    @media (max-width: 768px) {
      padding: $spacing-sm 0;
    }

    &__container {
      max-width: 1400px;
      margin: 0 auto;
      padding: 0 $spacing-sm;
      text-align: center;
    }

    .top-meta {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      margin-top: 12px;
      font-size: 0.9rem;
      color: #666;

      .dev-time,
      .category-text,
      .team-members-text {
        font-weight: 500;
      }

      .team-members-text {
        text-align: center;
        max-width: 80%;
        line-height: 1.4;
      }

      @media (max-width: 768px) {
        gap: 6px;
        font-size: 0.8rem;

        .team-members-text {
          max-width: 90%;
        }
      }
    }

    .dev-time {
      padding: $spacing-xs $spacing-sm;
      background-color: $color-primary-light;
      color: $color-primary;
      border-radius: $radius-full;
      font-weight: $font-weight-medium;

      @media (max-width: 480px) {
        padding: 6px 12px;
        font-size: $font-size-xs;
      }
    }

    .team-members-text {
      line-height: 1.5;

      @media (max-width: 480px) {
        white-space: pre-line;
        font-size: $font-size-xs;
      }
    }
  }

  // 4. 主体容器：保持原定义
  .project-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 $spacing-sm;
  }

  // 通用模块样式
  .project-section {
    margin-bottom: $spacing-lg;
    animation: fadeIn 0.8s ease forwards;
    opacity: 0;

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

  // 轮播图核心样式
  .project-carousel {
    margin-top: $spacing-sm;

    &.el-carousel--card {
      .el-carousel__container {
        padding: 0 $spacing-sm !important;

        @media (min-width: 1200px) {
          padding: 0 $spacing-md !important;
        }

        @media (max-width: 768px) {
          padding: 0 !important;
        }
      }

      .carousel-item {
        width: 50%;
        opacity: 0.7;
        transition: $transition-slow;
        border-radius: $radius-sm;
        overflow: hidden;
        box-shadow: $shadow-sm;
        margin: 0 auto;

        &.is-active {
          opacity: 1;
          transform: scale(1.02);
          box-shadow: $shadow-md;
          z-index: 10;
          position: relative;
          left: 0;
        }

        @media (max-width: 768px) {
          width: 100%;
          opacity: 1;
          transform: none;
        }
      }
    }

    &.el-carousel:not(.el-carousel--card) {
      .el-carousel__container {
        padding: 0;
      }

      .carousel-item {
        width: 100%;
        opacity: 1;
        border-radius: $radius-md;
        overflow: hidden;
        box-shadow: $shadow-sm;
      }

      .el-carousel__item--in-active {
        display: none;
      }
    }

    .el-carousel__arrow {
      width: 40px;
      height: 40px;
      border-radius: $radius-full;
      background-color: rgba(255, 255, 255, 0.9);
      color: $color-primary;
      box-shadow: $shadow-sm;
      top: 50%;
      transform: translateY(-50%);

      &:hover {
        background-color: #fff;
        transform: translateY(-50%) scale(1.1);
      }

      @media (max-width: 768px) {
        width: 28px;
        height: 28px;
        font-size: 12px;
      }
    }
  }

  .carousel-image-wrap {
    width: 100%;
    height: 100%;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;

    @media (max-width: 768px) {
      height: 100%;
      width: 100%;
    }

    .carousel-image {
      width: 100%;
      height: 100%;
      object-fit: contain;
      transition: transform 0.8s ease;
      background-color: #f3f4f6;

      @media (max-width: 768px) {
        width: 100%;
        height: 100%;
        min-height: 200px;
        object-position: center center;
      }

      @media (min-width: 768px) {
        &:hover {
          transform: scale(1.02);
        }
      }
    }

    .carousel-caption {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 12px 16px;
      background: linear-gradient(
        to top,
        rgba(0, 0, 0, 0.8),
        rgba(0, 0, 0, 0.4)
      );
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

      @media (max-width: 768px) {
        padding: 6px 10px;
        font-size: $font-size-xs;
        line-height: 1.4;
        background: linear-gradient(
          to top,
          rgba(0, 0, 0, 0.7),
          rgba(0, 0, 0, 0.2)
        );
      }
    }
  }

  .custom-indicator {
    bottom: 8px;
    z-index: 20;

    @media (max-width: 768px) {
      bottom: 6px;
    }

    .el-carousel__indicator-btn {
      width: 12px;
      height: 3px;
      border-radius: $radius-sm;
      background-color: rgba(255, 255, 255, 0.6);
      margin: 0 4px;

      @media (max-width: 768px) {
        width: 10px;
        height: 2px;
        margin: 0 3px;
      }

      &.is-active {
        width: 28px;
        background-color: #fff;

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

  // 联系电话样式
  .contact-phones {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: $spacing-sm;
    margin-top: $spacing-md;
    margin-bottom: $spacing-md;

    @media (max-width: 480px) {
      margin-top: $spacing-sm;
      margin-bottom: $spacing-sm;
    }
  }

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

  .phone-icon {
    font-size: $font-size-md;
    color: $color-neutral-100;
  }

  .phone-label {
    font-size: $font-size-sm;
    opacity: 0.9;
  }

  .phone-number {
    font-size: $font-size-base;
    font-weight: $font-weight-bold;
    color: #fff;
  }

  .empty-image {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: $color-neutral-200;
    border-radius: $radius-sm;
  }

  .empty-text {
    color: $color-neutral-600;
    font-size: $font-size-base;
    font-weight: $font-weight-medium;
  }

  // 底部提示样式
  .bottom-end-notice {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: $spacing-lg;
    margin-bottom: $spacing-lg;
    padding: $spacing-md 0;
    position: relative;
    animation: fadeInUp 0.8s ease-out 0.5s both;

    // 顶部装饰线
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 80px;
      height: 3px;
      background: linear-gradient(
        90deg,
        transparent,
        $color-primary,
        transparent
      );
      border-radius: 3px;
    }

    .end-text {
      display: flex;
      align-items: center;
      gap: $spacing-xs;
      padding: $spacing-sm $spacing-md;
      background: #fff;
      border-radius: $radius-full;
      box-shadow: $shadow-sm;
      font-size: $font-size-sm;
      color: $color-neutral-600;
      font-weight: $font-weight-medium;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: $shadow-md;
      }

      .el-icon {
        font-size: $font-size-base;
        color: $color-primary;
        animation: pulse 2s infinite;
      }
    }

    @media (max-width: 480px) {
      margin-top: $spacing-lg;
      margin-bottom: $spacing-md;

      &::before {
        width: 50px;
        height: 2px;
      }

      .end-text {
        font-size: $font-size-xs;
        padding: 8px 20px;

        .el-icon {
          font-size: $font-size-sm;
        }
      }
    }
  }

  @keyframes pulse {
    0%,
    100% {
      opacity: 1;
      transform: scale(1);
    }
    50% {
      opacity: 0.7;
      transform: scale(1.1);
    }
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }



  // Element Plus 图片预览样式优化
  .el-image-viewer__wrapper {
    .el-image-viewer__canvas {
      cursor: grab;
      
      &:active {
        cursor: grabbing;
      }
    }
  }
</style>
