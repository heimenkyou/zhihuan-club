// 将第一行修改为
import type { RouteRecordRaw } from 'vue-router';
import AdminLayout from '../views/admin/layout/AdminLayout.vue';
import Dashboard from '../views/admin/Dashboard.vue';
import ApplicationList from '../views/admin/applications/ApplicationList.vue';
import MessageManagement from '../views/admin/messages/MessageManagement.vue';
import AwardManagement from '../views/admin/awards/AwardManagement.vue';
import Login from '../views/admin/Login.vue';

// 公共路由
const publicRoutes: RouteRecordRaw[] = [
  {
    path: '/admin/login',
    name: 'adminLogin',
    component: Login,
    meta: {
      requiresAuth: false,
    },
  },
];

// 受保护的路由
const protectedRoutes: RouteRecordRaw[] = [
  {
    path: '/admin',
    name: 'admin',
    component: AdminLayout,
    meta: {
      requiresAuth: true,
    },
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'adminDashboard',
        component: Dashboard,
        meta: {
          title: '后台首页',
        },
      },
      {
        path: 'applications',
        name: 'adminApplications',
        component: ApplicationList,
        meta: {
          title: '报名信息管理',
        },
      },
      {
        path: 'messages',
        name: 'adminMessages',
        component: MessageManagement,
        meta: {
          title: '留言板管理',
        },
      },
      {
        path: 'awards',
        name: 'adminAwards',
        component: AwardManagement,
        meta: {
          title: '奖项管理',
        },
      },
    ],
  },
];

export { publicRoutes, protectedRoutes };