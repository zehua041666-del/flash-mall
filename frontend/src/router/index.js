import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('@/views/Home.vue') },
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue') }
]

// 动态权限路由、路由守卫一律不做，等以后要再加
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router