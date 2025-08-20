<template>
  <div class="project-showcase">
    <!-- 项目头部区域 -->
    <div class="project-header">
      <div class="header-overlay">
        <h1 class="project-title">智能校园导航系统</h1>
        <p class="project-subtitle">基于Flutter开发的校园智能导航解决方案</p>
        <div class="project-tags">
          <el-tag type="primary" effect="dark">移动应用</el-tag>
          <el-tag type="success" effect="dark">Flutter</el-tag>
          <el-tag type="warning" effect="dark">高德地图API</el-tag>
          <el-tag type="danger" effect="dark">Firebase</el-tag>
        </div>
      </div>
    </div>

    <!-- 项目主体内容 -->
    <div class="project-container">
      <!-- 项目概述 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><InfoFilled /></el-icon>
          项目概述
        </h2>
        <div class="section-content">
          <p>本项目是一款基于Flutter框架开发的校园智能导航应用，旨在解决新生和访客在校园内迷路的问题。系统整合了高德地图API，提供室内外一体化导航服务，并加入了AR实景导航功能，极大提升了用户体验。</p>
          <p>项目历时6个月开发，由CodePulse社团10名成员共同完成，在2023年全国大学生计算机设计大赛中获得一等奖。</p>
        </div>
      </div>

      <!-- 项目亮点 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Star /></el-icon>
          项目亮点
        </h2>
        <div class="highlights-grid">
          <div class="highlight-card" v-for="(highlight, index) in highlights" :key="index">
            <div class="highlight-icon">
              <el-icon><component :is="highlight.icon" /></el-icon>
            </div>
            <h3>{{ highlight.title }}</h3>
            <p>{{ highlight.description }}</p>
          </div>
        </div>
      </div>

      <!-- 图片展示 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Picture /></el-icon>
          项目展示
        </h2>
        <el-carousel :interval="4000" type="card" height="400px" indicator-position="none">
          <el-carousel-item v-for="item in carouselImages" :key="item.id">
            <img :src="item.url" :alt="item.caption" class="carousel-image">
            <div class="carousel-caption">{{ item.caption }}</div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 团队成员 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><User /></el-icon>
          团队成员
        </h2>
        <div class="team-members">
          <div class="member-card" v-for="member in teamMembers" :key="member.id">
            <div class="member-avatar">
              <img :src="member.avatar" :alt="member.name">
            </div>
            <div class="member-info">
              <h3>{{ member.name }}</h3>
              <p class="member-role">{{ member.role }}</p>
              <p class="member-major">{{ member.major }}</p>
              <div class="member-skills">
                <el-tag size="small" v-for="skill in member.skills" :key="skill" type="info">{{ skill }}</el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 技术栈 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Cpu /></el-icon>
          技术栈
        </h2>
        <div class="tech-stack">
          <div class="tech-item" v-for="tech in technologies" :key="tech.name">
            <div class="tech-logo">
              <img :src="tech.logo" :alt="tech.name">
            </div>
            <div class="tech-info">
              <h3>{{ tech.name }}</h3>
              <p>{{ tech.description }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 项目成果 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><Trophy /></el-icon>
          项目成果
        </h2>
        <el-timeline>
          <el-timeline-item
            v-for="(achievement, index) in achievements"
            :key="index"
            :timestamp="achievement.date"
            placement="top"
            :type="achievement.type"
          >
            <el-card>
              <h4>{{ achievement.title }}</h4>
              <p>{{ achievement.description }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>

      <!-- 项目演示视频 -->
      <div class="project-section">
        <h2 class="section-title">
          <el-icon><VideoCamera /></el-icon>
          项目演示
        </h2>
        <div class="video-container">
          <div class="video-wrapper">
            <iframe 
              src="https://www.youtube.com/embed/dQw4w9WgXcQ" 
              frameborder="0" 
              allowfullscreen
            ></iframe>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部CTA -->
    <div class="cta-section">
      <h2>对我们的项目感兴趣？</h2>
      <p>欢迎联系我们获取更多信息或探讨合作机会</p>
      <el-button type="primary" size="large" round>
        联系我们 <el-icon><Promotion /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  InfoFilled, Star, Location, Camera, Clock, User,
  Picture, Cpu, Trophy, VideoCamera, Promotion,
} from '@element-plus/icons-vue'

const highlights = ref([
  {
    icon: 'Location',
    title: '精准定位',
    description: '采用混合定位技术，室内外定位精度达到1-3米'
  },
  {
    icon: 'Camera',
    title: 'AR导航',
    description: '创新的AR实景导航功能，直观指引方向'
  },
  {
    icon: 'Refresh',
    title: '实时更新',
    description: '教室状态实时更新，避免空跑教室'
  },
  {
    icon: 'Recommend',
    title: '个性化推荐',
    description: '基于用户习惯的智能路线推荐'
  }
])

const carouselImages = ref([
  {
    id: 1,
    url: 'https://images.unsplash.com/photo-1551650975-87deedd944c3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80',
    caption: '应用主界面展示'
  },
  {
    id: 2,
    url: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80',
    caption: 'AR导航功能演示'
  },
  {
    id: 3,
    url: 'https://images.unsplash.com/photo-1547082299-de196ea013d6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80',
    caption: '室内导航效果'
  },
  {
    id: 4,
    url: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80',
    caption: '用户个性化设置'
  }
])

const teamMembers = ref([
  {
    id: 1,
    name: '张明',
    role: '项目负责人',
    major: '计算机科学与技术',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=987&q=80',
    skills: ['Flutter', 'Dart', '项目管理']
  },
  {
    id: 2,
    name: '李华',
    role: '前端开发',
    major: '软件工程',
    avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1976&q=80',
    skills: ['UI设计', '前端开发', '用户体验']
  },
  {
    id: 3,
    name: '王芳',
    role: '后端开发',
    major: '数据科学',
    avatar: 'https://images.unsplash.com/photo-1573496358961-3c82838ef709?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80',
    skills: ['Firebase', 'API开发', '数据库']
  },
  {
    id: 4,
    name: '刘强',
    role: '算法工程师',
    major: '人工智能',
    avatar: 'https://images.unsplash.com/photo-1560250097-0b93528c311a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80',
    skills: ['路径规划', '机器学习', '算法优化']
  }
])

const technologies = ref([
  {
    name: 'Flutter',
    description: '跨平台移动应用开发框架',
    logo: 'https://cdn.worldvectorlogo.com/logos/flutter.svg'
  },
  {
    name: 'Firebase',
    description: '后端即服务(BaaS)平台',
    logo: 'https://cdn.worldvectorlogo.com/logos/firebase-1.svg'
  },
  {
    name: '高德地图API',
    description: '提供地图和定位服务',
    logo: 'https://cdn.icon-icons.com/icons2/2699/PNG/512/amap_logo_icon_168587.png'
  },
  {
    name: 'ARCore',
    description: '增强现实开发平台',
    logo: 'https://cdn.worldvectorlogo.com/logos/arcore.svg'
  }
])

const achievements = ref([
  {
    date: '2023年3月',
    title: '校赛一等奖',
    description: '获得学校科技创新大赛一等奖',
    type: 'primary'
  },
  {
    date: '2023年5月',
    title: '省级比赛金奖',
    description: '在省级大学生创新创业大赛中获得金奖',
    type: 'success'
  },
  {
    date: '2023年7月',
    title: '全国大赛一等奖',
    description: '荣获全国大学生计算机设计大赛一等奖',
    type: 'warning'
  },
  {
    date: '2023年9月',
    title: '校企合作',
    description: '与当地科技企业达成合作协议',
    type: 'danger'
  }
])
</script>

<style scoped lang="scss">
.project-showcase {
  font-family: 'Inter', system-ui, sans-serif;
  color: #333;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf5 100%);
  min-height: 100vh;
}

.project-header {
  height: 500px;
  background-image: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), 
                    url('https://images.unsplash.com/photo-1550751827-4bd374c3f58b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  color: white;
  text-align: center;
  margin-bottom: 60px;

  .header-overlay {
    max-width: 800px;
    padding: 0 20px;
    z-index: 2;
  }

  .project-title {
    font-size: 3rem;
    font-weight: bold;
    margin-bottom: 16px;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    animation: fadeInDown 1s ease;
  }

  .project-subtitle {
    font-size: 1.5rem;
    margin-bottom: 24px;
    opacity: 0.9;
    animation: fadeInUp 1s ease;
  }

  .project-tags {
    display: flex;
    justify-content: center;
    gap: 12px;
    flex-wrap: wrap;
    animation: fadeIn 1.5s ease;

    .el-tag {
      font-size: 1rem;
      padding: 8px 16px;
      border: none;
    }
  }
}

.project-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.project-section {
  margin-bottom: 60px;
  animation: fadeIn 0.8s ease;

  .section-title {
    font-size: 1.8rem;
    font-weight: bold;
    margin-bottom: 30px;
    display: flex;
    align-items: center;
    gap: 12px;
    color: #2c3e50;

    .el-icon {
      font-size: 1.5rem;
    }
  }

  .section-content {
    line-height: 1.8;
    font-size: 1.1rem;
    color: #555;
    background: white;
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  }
}

.highlights-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;

  .highlight-card {
    background: white;
    border-radius: 12px;
    padding: 30px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    text-align: center;

    &:hover {
      transform: translateY(-8px);
      box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
    }

    .highlight-icon {
      width: 70px;
      height: 70px;
      background: linear-gradient(135deg, #165DFF, #4080FF);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 20px;

      .el-icon {
        color: white;
        font-size: 32px;
      }
    }

    h3 {
      font-size: 1.3rem;
      margin-bottom: 12px;
      color: #2c3e50;
    }

    p {
      color: #666;
      line-height: 1.6;
    }
  }
}

.el-carousel {
  margin-top: 30px;

  .carousel-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 8px;
  }

  .carousel-caption {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 16px;
    background: rgba(0, 0, 0, 0.7);
    color: white;
    text-align: center;
    font-size: 1.1rem;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
  }
}

.team-members {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;

  .member-card {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    display: flex;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-8px);
      box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
    }

    .member-avatar {
      width: 120px;
      height: 120px;
      flex-shrink: 0;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .member-info {
      padding: 20px;
      flex: 1;

      h3 {
        font-size: 1.3rem;
        margin-bottom: 6px;
        color: #2c3e50;
      }

      .member-role {
        color: #165DFF;
        font-weight: bold;
        margin-bottom: 6px;
      }

      .member-major {
        color: #666;
        margin-bottom: 12px;
        font-size: 0.9rem;
      }

      .member-skills {
        display: flex;
        flex-wrap: wrap;
        gap: 6px;

        .el-tag {
          margin-right: 6px;
          margin-bottom: 6px;
        }
      }
    }
  }
}

.tech-stack {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;

  .tech-item {
    background: white;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    display: flex;
    align-items: center;
    gap: 16px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
    }

    .tech-logo {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      overflow: hidden;
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;

      img {
        width: 100%;
        height: 100%;
        object-fit: contain;
      }
    }

    h3 {
      font-size: 1.2rem;
      margin-bottom: 8px;
      color: #2c3e50;
    }

    p {
      color: #666;
      font-size: 0.9rem;
      line-height: 1.5;
    }
  }
}

.el-timeline {
  padding-left: 20px;

  .el-timeline-item {
    padding-bottom: 30px;
  }

  :deep(.el-timeline-item__timestamp) {
    color: #666;
    font-size: 1rem;
    font-weight: bold;
  }

  .el-card {
    border-radius: 12px;
    border: none;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

    h4 {
      font-size: 1.2rem;
      margin-bottom: 8px;
      color: #2c3e50;
    }

    p {
      color: #666;
      line-height: 1.6;
    }
  }
}

.video-container {
  margin-top: 30px;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;

  .video-wrapper {
    position: relative;
    padding-bottom: 56.25%; /* 16:9 Aspect Ratio */
    height: 0;
    overflow: hidden;

    iframe {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      border-radius: 8px;
    }
  }
}

.cta-section {
  background: linear-gradient(135deg, #165DFF, #4080FF);
  color: white;
  text-align: center;
  padding: 60px 20px;
  margin-top: 60px;

  h2 {
    font-size: 2.2rem;
    margin-bottom: 16px;
  }

  p {
    font-size: 1.2rem;
    margin-bottom: 30px;
    opacity: 0.9;
  }

  .el-button {
    font-size: 1.1rem;
    padding: 12px 36px;
    background: white;
    color: #165DFF;
    font-weight: bold;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    }
  }
}

@media (max-width: 768px) {
  .project-header {
    height: 400px;

    .project-title {
      font-size: 2.2rem;
    }

    .project-subtitle {
      font-size: 1.2rem;
    }
  }

  .project-section {
    margin-bottom: 40px;

    .section-title {
      font-size: 1.5rem;
    }
  }

  .team-members {
    grid-template-columns: 1fr;
  }
}
</style>