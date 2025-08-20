import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import MessageList from '../views/MessageList.vue'
import ProjecjDetail from '../views/ProjectDetail.vue'

const routes = [
  {
    path: "/",
    name: "home",
    component: Home,
  },
  {
    path: "/messages",
    name: "messageList",
    component: MessageList,
  },
  {
    path: "/project/detail",
    name: "projectDetail",
    component: ProjecjDetail,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router