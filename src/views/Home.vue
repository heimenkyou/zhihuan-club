<script setup lang="ts">
// 首页组件逻辑
</script>

<template>
  <div class="home">
    <div class="hero-section">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">
            <span class="hero-icon">🎯</span>
            欢迎来到招新留言板
          </h1>
          <p class="hero-description">
            这是一个基于Vue3 + Vite + TypeScript的现代化留言板应用
          </p>
          <p class="hero-features">
            支持留言展示、点赞互动、响应式设计等特性
          </p>
          <router-link to="/messages" class="cta-button">
            <span class="button-icon">💬</span>
            查看留言列表
          </router-link>
        </div>

        <div class="hero-visual">
          <div class="floating-card">
            <div class="card-icon">💭</div>
            <div class="card-text">分享想法</div>
          </div>
          <div class="floating-card">
            <div class="card-icon">👍</div>
            <div class="card-text">点赞互动</div>
          </div>
          <div class="floating-card">
            <div class="card-icon">🚀</div>
            <div class="card-text">快速响应</div>
          </div>
        </div>
      </div>
    </div>

    <div class="features-section">
      <div class="container">
        <h2 class="features-title">技术特性</h2>
        <div class="features-grid">
          <div class="feature-item">
            <div class="feature-icon">⚡</div>
            <h3 class="feature-name">Vue 3</h3>
            <p class="feature-desc">使用最新的Composition API，性能更优</p>
          </div>
          <div class="feature-item">
            <div class="feature-icon">🔧</div>
            <h3 class="feature-name">Vite</h3>
            <p class="feature-desc">极速的开发服务器和构建工具</p>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📝</div>
            <h3 class="feature-name">TypeScript</h3>
            <p class="feature-desc">类型安全，开发体验更佳</p>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📱</div>
            <h3 class="feature-name">响应式</h3>
            <p class="feature-desc">完美适配PC端和移动端</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 基础样式 */
* {
  box-sizing: border-box;
}

.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 英雄区域 */
.hero-section {
  padding: 4rem 0 6rem;
  position: relative;
  overflow: hidden;
}

.hero-content {
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 800;
  color: white;
  margin: 0 0 1.5rem 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  line-height: 1.2;
}

.hero-icon {
  font-size: 3rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {

  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0);
  }

  40% {
    transform: translateY(-10px);
  }

  60% {
    transform: translateY(-5px);
  }
}

.hero-description {
  font-size: 1.3rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 1rem 0;
  font-weight: 400;
}

.hero-features {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.7);
  margin: 0 0 2.5rem 0;
  font-weight: 300;
}

.cta-button {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  text-decoration: none;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  border: 2px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.cta-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.cta-button:hover::before {
  left: 100%;
}

.cta-button:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.button-icon {
  font-size: 1.2rem;
}

/* 英雄区域视觉元素 */
.hero-visual {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 1;
}

.floating-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 1rem;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: float 6s ease-in-out infinite;
}

.floating-card:nth-child(1) {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.floating-card:nth-child(2) {
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.floating-card:nth-child(3) {
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {

  0%,
  100% {
    transform: translateY(0px) rotate(0deg);
  }

  33% {
    transform: translateY(-20px) rotate(5deg);
  }

  66% {
    transform: translateY(10px) rotate(-5deg);
  }
}

.card-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.card-text {
  color: white;
  font-size: 0.9rem;
  font-weight: 500;
}

/* 特性区域 */
.features-section {
  padding: 4rem 0;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
}

.features-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin: 0 0 3rem 0;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-item {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 2rem;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  display: block;
}

.feature-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: white;
  margin: 0 0 1rem 0;
}

.feature-desc {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-icon {
    font-size: 2rem;
  }

  .hero-description {
    font-size: 1.1rem;
  }

  .hero-features {
    font-size: 1rem;
  }

  .cta-button {
    padding: 0.8rem 1.5rem;
    font-size: 1rem;
  }

  .features-title {
    font-size: 2rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .floating-card {
    display: none;
  }
}

@media (max-width: 480px) {
  .hero-section {
    padding: 3rem 0 4rem;
  }

  .hero-title {
    font-size: 2rem;
    flex-direction: column;
    gap: 0.5rem;
  }

  .hero-icon {
    font-size: 1.5rem;
  }

  .hero-description {
    font-size: 1rem;
  }

  .hero-features {
    font-size: 0.9rem;
  }

  .cta-button {
    padding: 0.7rem 1.2rem;
    font-size: 0.9rem;
  }

  .features-section {
    padding: 3rem 0;
  }

  .features-title {
    font-size: 1.75rem;
  }

  .feature-item {
    padding: 1.5rem;
  }

  .feature-icon {
    font-size: 2.5rem;
  }

  .feature-name {
    font-size: 1.1rem;
  }

  .feature-desc {
    font-size: 0.9rem;
  }
}
</style>