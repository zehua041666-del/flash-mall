<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>注册</h2>
      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            show-password
            @keyup.enter="handleRegister"
          />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.email" placeholder="邮箱（可选）" />
        </el-form-item>
        <el-button type="primary" style="width: 100%" :loading="loading" @click="handleRegister">
          注册
        </el-button>
      </el-form>
      <div class="switch">
        已有账号？<el-link type="primary" @click="$router.push('/login')">去登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/user'

const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: ''
})

const loading = ref(false)

async function handleRegister() {
  if (loading.value) return

  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  if (form.password.length < 6) {
    ElMessage.warning('密码长度不能少于 6 位')
    return
  }

  loading.value = true
  try {
    // confirmPassword 是纯前端字段，不传给后端
    await register({
      username: form.username,
      password: form.password,
      email: form.email
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {
    // 错误提示已在 request.js 拦截器统一处理，这里留空
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