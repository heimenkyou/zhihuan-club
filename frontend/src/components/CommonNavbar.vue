<template>
  <!-- 1. 桌面端布局：仅顶部导航（无路由内容区） -->
  <div v-if="!isMobile" class="desktop-layout">
    <!-- 桌面端顶部导航 -->
    <nav class="web-nav">
      <div class="web-nav-container">
        <div
          class="web-nav-brand flex items-center"
          @click="navigateToHome"
          aria-label="返回首页"
        >
          <i-fa6-solid-code class="fa-code nav-brand-icon" />智环学创融合协会
        </div>
        <div class="web-nav-links">
          <button
            v-for="item in navItems"
            :key="item.id"
            @click="handleNavClick(item)"
            :class="[
              'web-nav-link',
              { active: currentRoutePath === item.path },
            ]"
          >
            {{ item.text }}
          </button>
        </div>
        <div class="menu-toggle">
          <i-fa6-solid-bars class="fa-bars" />
        </div>
      </div>
    </nav>
  </div>

  <!-- 2. 移动端布局：仅导航相关（顶部导航+下拉菜单，无内容区） -->
  <div v-else class="mobile-layout fixed top-0 left-0 right-0 z-50">
    <!-- 移动端顶部导航：固定在顶部 -->
    <div class="mobile-top-nav bg-white px-4 py-3 shadow-sm sticky z-20 flex items-center justify-between">
      <!-- 非首页显示返回箭头 -->
      <div
        v-if="currentRoutePath !== '/'"
        class="flex items-center space-x-4 w-full"
      >
        <button @click="router.back()" class="text-dark" aria-label="返回">
          <i-fa6-solid-arrow-left class="fa-arrow-left" />
        </button>
        <div class="font-medium text-lg text-center flex-1">
          {{ currentPageTitle }}
        </div>
      </div>
      <!-- 首页保持原有样式 -->
      <div v-else class="font-medium text-lg">智环学创融合协会</div>
      <button @click="openSidebar" class="text-dark" aria-label="打开菜单">
        <i-fa6-solid-bars class="fa-bars" />
      </button>
    </div>

    <!-- 移动端导航下拉菜单 -->
    <div
      class="sidebar-backdrop"
      :class="{ visible: isSidebarOpen }"
      @click="closeSidebar"
    ></div>
    <div class="mobile-sidebar" :class="{ open: isSidebarOpen }">
      <div class="p-4 border-b flex justify-between items-center">
        <h2 class="text-lg font-bold text-dark">导航菜单</h2>
        <button @click="closeSidebar" class="p-2" aria-label="关闭菜单">
          <i-fa6-solid-xmark class="fa-times" />
        </button>
      </div>
      <nav class="py-1">
        <div
          v-for="item in navItems"
          :key="item.id"
          class="mobile-nav-item"
          :class="{ active: currentRoutePath === item.path }"
          @click="handleNavClick(item)"
        >
          {{ item.text }}
        </div>
      </nav>
      <div class="p-4 border-t">
        <button
          type="button"
          @click="handleJoinClick()"
          class="w-full py-3 text-white font-bold rounded-lg shadow-md hover:bg-primary/90 transition-colors"
          style="background-color: #3b82f6"
        >
          立即报名
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

// 1. 路由相关
const router = useRouter();
const route = useRoute();
const currentRoutePath = ref(route.path); // 当前路由路径

// 监听路由变化，更新当前路由
watch(route, (newRoute) => {
	currentRoutePath.value = newRoute.path;
});

// 2. 导航数据（带路由路径）
const navItems = [
	{ id: 1, text: "首页", path: "/" },
	{ id: 2, text: "项目库", path: "/projects" },
	{ id: 3, text: "奖项库", path: "/awards" },
	{ id: 4, text: "关于我们", path: "/about" },
	{ id: 5, text: "新手导航", path: "/resources" },
	{ id: 6, text: "加入我们", path: "/join" },
	{ id: 7, text: "留言板", path: "/messages" },
];

// 3. 响应式状态
const isMobile = ref(false); // 是否移动端
const isSidebarOpen = ref(false); // 导航菜单是否打开
let previousBodyOverflow = null;

// 4. 核心交互
/** 导航项点击：路由跳转+关闭侧边栏 */
const handleNavClick = (item) => {
	router.push(item.path);
	closeSidebar();
};

/** "立即报名"按钮点击 */
const handleJoinClick = () => {
	router.push("/join");
	closeSidebar();
};

/** 打开导航菜单 */
const openSidebar = () => {
	if (isSidebarOpen.value) return;

	previousBodyOverflow = document.body.style.overflow;
	isSidebarOpen.value = true;
	document.body.style.overflow = "hidden"; // 禁止页面滚动
};

/** 关闭导航菜单 */
const closeSidebar = () => {
	isSidebarOpen.value = false;
	if (previousBodyOverflow !== null) {
		document.body.style.overflow = previousBodyOverflow;
		previousBodyOverflow = null;
	}
};

/** 检测屏幕尺寸：切换移动端/桌面端 */
const checkScreenSize = () => {
	isMobile.value = window.innerWidth < 768;
	if (!isMobile.value && isSidebarOpen.value) {
		closeSidebar();
	}
};

// 生命周期钩子
onMounted(() => {
	checkScreenSize();
	window.addEventListener("resize", checkScreenSize);
	window.addEventListener("orientationchange", checkScreenSize);
});

onUnmounted(() => {
	window.removeEventListener("resize", checkScreenSize);
	window.removeEventListener("orientationchange", checkScreenSize);
	closeSidebar();
});
// 添加返回首页的方法
const navigateToHome = () => {
	router.push("/");
};

// 计算当前页面标题
const currentPageTitle = computed(() => {
	const item = navItems.find((item) => item.path === currentRoutePath.value);
	return item ? item.text : "智环学创融合协会";
});
</script>

<style scoped>
  /* 1. 桌面端导航：固定顶部，不影响下方内容（下方组件需加margin-top: 60px） */
  .desktop-layout .web-nav {
    background-color: #fff;
    border-bottom: 1px solid #e5e7eb;
    padding: 0 24px;
    height: 60px;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 50;
    box-sizing: border-box;
  }

  .web-nav-container {
    max-width: 1200px;
    margin: 0 auto;
    height: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .web-nav-brand {
    font-size: 20px;
    font-weight: 700;
    color: #3b82f6;
    background: transparent;
    border: none;
    cursor: pointer;
    transition: transform 0.2s ease;
  }

  .web-nav-brand:hover {
    transform: scale(1.05);
  }

  /* 导航图标悬停效果 */
  .nav-brand-icon {
    transition: all 0.3s ease;
  }

  .web-nav-brand:hover .nav-brand-icon {
    transform: rotate(10deg);
    color: #2563eb;
  }

  .web-nav-links {
    display: flex;
    gap: 24px;
  }

  .web-nav-link {
    color: #1e293b;
    font-weight: 500;
    transition: color 0.2s ease;
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 4px 0;
  }

  /* 导航项激活/hover样式 */
  .web-nav-link:hover,
  .web-nav-link.active {
    color: #3b82f6;
  }

  /* 2. 移动端导航：固定顶部，层级最高（避免被内容遮挡） */
  .mobile-layout {
    background: transparent;
  }

  /* 移动端顶部导航 */
  .mobile-top-nav {
    height: 50px;
    background-color: #fff;
    box-sizing: border-box;
  }

  /* 移动端导航下拉菜单：从导航条下方展开，高度自适应内容 */
  .mobile-sidebar {
    position: fixed;
    top: 50px;
    right: 12px;
    width: 80%;
    max-width: 320px;
    max-height: calc(100vh - 60px);
    background-color: white;
    border-radius: 0 0 16px 16px;
    box-shadow: 0 12px 32px rgb(0 0 0 / 0.15);
    z-index: 100;
    overflow-y: auto;
    opacity: 0;
    visibility: hidden;
    transform: translateY(-8px);
    transition:
      opacity 0.25s ease,
      transform 0.25s ease,
      visibility 0.25s;
    box-sizing: border-box;
  }

  .mobile-sidebar.open {
    opacity: 1;
    visibility: visible;
    transform: translateY(0);
  }

  /* 导航菜单遮罩层 */
  .sidebar-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 99;
    opacity: 0;
    visibility: hidden;
    transition: opacity 0.3s ease;
  }

  .sidebar-backdrop.visible {
    opacity: 1;
    visibility: visible;
  }

  /* 移动端导航项 */
  .mobile-nav-item {
    padding: 13px 20px;
    border-bottom: 1px solid #f0f0f0;
    color: #1e293b;
    font-weight: 500;
    font-size: 16px;
    cursor: pointer;
    transition: all 0.2s ease;
  }

  /* 移动端导航项激活/hover样式 */
  .mobile-nav-item:hover,
  .mobile-nav-item.active {
    color: #3b82f6;
    background-color: #f0f9ff;
  }

  /* 3. 响应式切换：严格区分移动端/桌面端 */
  @media (max-width: 767px) {
    /* 移动端隐藏桌面端导航链接 */
    .web-nav-links {
      display: none;
    }

    /* 移动端显示菜单按钮（仅备用） */
    .menu-toggle {
      display: block;
      font-size: 24px;
      color: #1e293b;
      background: transparent;
      border: none;
      cursor: pointer;
    }
  }

  @media (min-width: 768px) {
    /* 桌面端隐藏移动端布局 */
    .mobile-layout,
    .menu-toggle {
      display: none;
    }
  }
</style>
