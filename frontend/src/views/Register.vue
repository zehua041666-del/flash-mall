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
          <el-input v-model="form.email" placeholder="邮箱（可选）" />
        </el-form-item>
        <el-button type="primary" style="width: 100%" @click="handleRegister">注册</el-button>
      </el-form>
      <div class="switch">
        已有账号？<el-link type="primary" @click="$router.push('/login')">去登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const form = reactive({
  username: '',
  password: '',
  email: ''
})

function handleRegister() {
  ElMessage.info('注册功能待 W2 实现')
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