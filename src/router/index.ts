import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import MessageList from '../views/MessageList.vue';
import ProjecjDetail from '../views/ProjectDetail.vue';
import Projects from '../views/Projects.vue';
import Competitions from '../views/Competitions.vue';
import Awards from '../views/Awards.vue';
import About from '../views/About.vue';
import Resources from '../views/Resources.vue';
import Join from '../views/Join.vue';
import ProjectDetailText from '../views/ProjectDetailTest.vue';
import { publicRoutes, protectedRoutes } from './adminRoutes';
import { useAdminStore } from '../stores/adminStore';

const routes = [
  { path: '/', name: 'home', component: Home },
  { path: '/projects', name: 'projects', component: Projects },
  { path: '/projectdetailtest', name: 'projectdetail', component: ProjectDetailText },
  { path: '/competitions', name: 'competitions', component: Competitions },
  { path: '/awards', name: 'awards', component: Awards },
  { path: '/about', name: 'about', component: About },
  { path: '/resources', name: 'resources', component: Resources },
  { path: '/join', name: 'join', component: Join },
  { path: '/messages', name: 'messageList', component: MessageList },
  { path: '/projectdetail', name: 'projectDetail', component: ProjecjDetail },
  // 添加管理员公共路由
  ...publicRoutes,
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// 删除 routesAdded 标志

// 路由守卫
router.beforeEach((to, _, next) => {
  const adminStore = useAdminStore();
  adminStore.checkLoginStatus();

  // 动态添加受保护路由（每次登录状态变化时都检查）
  if (adminStore.isLoggedIn) {
    // 检查受保护路由是否已经添加
    const hasAdminRoute = router.getRoutes().some(route => route.path === '/admin');
    if (!hasAdminRoute) {
      protectedRoutes.forEach((route) => {
        router.addRoute(route);
      });
      // 添加路由后重新导航到当前路径
      return next({ ...to, replace: true });
    }
  }

  // 如果是受保护的路由，但未登录，则重定向到登录页
  if (to.meta.requiresAuth && !adminStore.isLoggedIn) {
    next('/admin/login');
    return;
  }

  // 如果已登录，访问登录页则重定向到后台首页
  if (adminStore.isLoggedIn && to.path === '/admin/login') {
    next('/admin/dashboard');
    return;
  }

  next();
});

export default router;