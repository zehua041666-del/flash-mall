<template>
  <div class="product-page">
    <h2>商品列表</h2>

    <!-- 商品卡片区域：加上 v-loading 后，请求时会自动转圈 -->
    <div class="product-list" v-loading="loading">
      <!-- v-for 循环遍历 productList，渲染一张张卡片 -->
      <el-card 
        v-for="item in productList" 
        :key="item.spuId" 
        class="product-card"
        :body-style="{ padding: '0px' }"
      >
        <!-- 商品图片 -->
        <img :src="item.mainImage" class="image" />
        
        <!-- 商品文字信息 -->
        <div class="info">
          <h3 class="name">{{ item.name }}</h3>
          <p class="price">¥ {{ item.minPrice }} 起</p>
        </div>
      </el-card>
    </div>

    <!-- 如果没数据，显示空状态 -->
    <el-empty v-if="!loading && productList.length === 0" description="暂无商品" />

    <!-- 分页器 -->
    <div class="pagination-box">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNum"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductList } from '@/api/product'

// 状态变量
const productList = ref([]) // 存放后端返回的商品数组
const loading = ref(false)  // 控制加载转圈
const total = ref(0)        // 总商品数（分页用）
const pageNum = ref(1)      // 当前第几页
const pageSize = ref(6)     // 每页显示几个（我们总共 3 个商品，设小一点方便看效果）

// 核心方法：去后端拿数据
async function fetchData() {
  loading.value = true
  try {
    // 调用 api，拦截器会自动解包 Result
    const res = await getProductList(pageNum.value, pageSize.value)
    productList.value = res.list // 把商品数组塞进去
    total.value = res.total      // 把总数塞进去
  } catch (e) {
    // 错误拦截器已经处理了，这里留空
  } finally {
    loading.value = false
  }
}

// 翻页时触发
function handlePageChange(newPage) {
  pageNum.value = newPage
  fetchData() // 重新请求新一页的数据
}

// 页面刚加载时，自动去请求第 1 页
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.product-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
  min-height: 300px; /* 防止加载时页面高度塌陷 */
}
.product-card {
  width: 220px;
  cursor: pointer;
  transition: transform 0.2s;
}
.product-card:hover {
  transform: translateY(-5px); /* 鼠标悬停时卡片微微上浮 */
}
.image {
  width: 100%;
  height: 220px;
  object-fit: cover;
  display: block;
}
.info {
  padding: 14px;
}
.name {
  font-size: 16px;
  margin: 0 0 10px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* 名字太长变成省略号 */
}
.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 0;
}
.pagination-box {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style>