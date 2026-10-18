import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, clearToken } from '@/utils/token'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken() || '')
  const userInfo = ref(null)

  function loginSuccess(t, info) {
    token.value = t
    userInfo.value = info
    setToken(t)
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    clearToken()
  }

  return { token, userInfo, loginSuccess, logout }
})