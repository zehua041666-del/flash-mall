<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>登录</h2>
      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-button type="primary" style="width: 100%" :loading="loading" @click="handleLogin">
          登录
        </el-button>
      </el-form>
      <div class="switch">
        没有账号？<el-link type="primary" @click="$router.push('/register')">去注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  password: ''
})

// loading 防止用户连点导致重复请求
const loading = ref(false)

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    // 调用登录接口，request.js 拦截器已经把 Result 解包成 data
    // 所以 res 直接就是 { token, userId, username, nickname }
    const res = await login({
      username: form.username,
      password: form.password
    })

    // 控制台打印 JWT，验收标准要求
    console.log('JWT:', res.token)

    // 存进 Pinia + localStorage
    userStore.loginSuccess(res.token, {
      userId: res.userId,
      username: res.username,
      nickname: res.nickname
    })

    ElMessage.success('登录成功')
    router.push('/')
  } catch (e) {
    // 错误提示已经在 request.js 拦截器里弹过了，这里不用再弹
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}
.auth-card {
  width: 360px;
  padding: 20px;
}
h2 {
  text-align: center;
  margin-bottom: 20px;
}
.switch {
  margin-top: 16px;
  text-align: center;
  font-size: 14px;
  color: #666;
}
</style>