import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearToken } from '@/utils/token'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截：统一注入 token，业务代码不用每次手动加 header
request.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截：与后端 Result{code, message, data} 约定对齐
// code=0 成功直接吐 data，业务代码拿到的就是纯数据
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 0) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        clearToken()
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res.data
  },
  (error) => {
    // HTTP 层异常（网络断、超时、500 未走到 Result 包装）
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request