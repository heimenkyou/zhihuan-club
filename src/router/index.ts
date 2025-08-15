import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import MessageList from '../views/MessageList.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/messages',
    name: 'messageList',
    component: MessageList
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router