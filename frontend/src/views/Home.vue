<template>
  <div class="home">
    <h1>flash-mall</h1>
    <p>W1 前端骨架跑通</p>
    <div class="actions">
      <el-button type="primary" @click="$router.push('/login')">去登录</el-button>
      <el-button @click="$router.push('/register')">去注册</el-button>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
}
.actions {
  display: flex;
  gap: 12px;
}
</style>