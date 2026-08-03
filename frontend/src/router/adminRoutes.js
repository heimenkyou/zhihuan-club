const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/views/admin/layout/AdminLayout.vue'),
    meta: { requiresAuth: true },
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '仪表盘' },
      },
      {
        path: 'applications',
        component: () => import('@/views/admin/applications/ApplicationList.vue'),
        meta: { title: '报名管理' },
      },
      {
        path: 'awards',
        component: () => import('@/views/admin/awards/AwardManagement.vue'),
        meta: { title: '奖项管理' },
      },
      {
        path: 'messages',
        component: () => import('@/views/admin/messages/MessageManagement.vue'),
        meta: { title: '消息管理' },
      },
      {
        path: 'admins',
        component: () => import('@/views/admin/admins/AdminManagement.vue'),
        meta: { title: '管理员管理', requiresSuperAdmin: true },
      },
      // 项目管理路由
      {
        path: 'projects',
        name: 'ProjectManagement',
        component: () => import('@/views/admin/projects/ProjectManagement.vue'),
        meta: { title: '项目管理' },
      },
      // 项目编辑页面路由
      {
        path: 'projects/edit/:id?',
        name: 'ProjectEdit',
        component: () => import('@/views/admin/projects/ProjectEditPage.vue'),
        meta: { title: '编辑项目' },
      },
      // 个人资料页面路由
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('@/views/admin/Profile.vue'),
        meta: { title: '个人资料' },
      },
      {
        path: 'attachments',
        component: () => import('@/views/admin/attachments/AttachmentManagement.vue'),
        meta: { title: '附件库' },
      },
    ],
  },
  {
    path: '/admin/login',
    component: () => import('@/views/admin/Login.vue'),
    meta: { title: '管理员登录', requiresAuth: false },
  },
]

export default adminRoutes
