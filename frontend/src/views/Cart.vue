<template>
  <div class="cart-page">
    <h2>我的购物车</h2>

    <!-- v-loading 请求时转圈 -->
    <div v-loading="loading">
      <!-- 如果有数据，展示表格 -->
      <el-table :data="cartList" style="width: 100%" v-if="cartList.length > 0">
        
        <!-- 商品信息列（图片 + 名字） -->
        <el-table-column label="商品" width="350">
          <template #default="scope">
            <div class="product-info">
              <img :src="scope.row.skuImage" class="product-img" />
              <span class="product-name">{{ scope.row.skuName }}</span>
            </div>
          </template>
        </el-table-column>

        <!-- 单价列 -->
        <el-table-column label="单价" width="150">
          <template #default="scope">
            ¥ {{ scope.row.price }}
          </template>
        </el-table-column>

        <!-- 数量列（加减号） -->
        <el-table-column label="数量" width="200">
          <template #default="scope">
            <el-input-number 
              v-model="scope.row.quantity" 
              :min="1" 
              @change="(currentValue) => handleQuantityChange(scope.row.skuId, currentValue)"
            />
          </template>
        </el-table-column>

        <!-- 小计列 -->
        <el-table-column label="小计" width="150">
          <template #default="scope">
            <span class="total-price">¥ {{ scope.row.totalPrice }}</span>
          </template>
        </el-table-column>

        <!-- 操作列（删除） -->
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="danger" text @click="handleDelete(scope.row.skuId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 购物车为空时的提示 -->
      <el-empty v-if="!loading && cartList.length === 0" description="购物车空空如也，快去逛逛吧">
        <el-button type="primary" @click="$router.push('/products')">去逛商品</el-button>
      </el-empty>

      <!-- 底部结算栏 -->
      <div class="checkout-bar" v-if="cartList.length > 0">
        <div class="total">
          合计：<span class="price">¥ {{ totalAmount }}</span>
        </div>
        <el-button type="danger" size="large">去结算</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getCartList, updateCartQuantity, removeCartItem } from '@/api/cart'

// 状态变量
const cartList = ref([])
const loading = ref(false)

// 自动计算所有商品的小计总和
const totalAmount = computed(() => {
  let sum = 0
  cartList.value.forEach(item => {
    sum += Number(item.totalPrice)
  })
  return sum.toFixed(2) // 保留两位小数
})

// 获取购物车列表
async function fetchData() {
  loading.value = true
  try {
    const res = await getCartList()
    // res 直接就是后端返回的 List
    cartList.value = res || []
  } catch (e) {
    // 错误已在 request.js 拦截器处理
  } finally {
    loading.value = false
  }
}

// 修改数量 (el-input-number 触发)
async function handleQuantityChange(skuId, newQuantity) {
  try {
    await updateCartQuantity(skuId, newQuantity)
    ElMessage.success('数量已更新')
    fetchData() // 重新拉取最新数据，保证总价实时刷新
  } catch (e) {
    // 如果失败，重新拉一次数据，把页面上的错误数字还原
    fetchData()
  }
}

// 删除商品
async function handleDelete(skuId) {
  try {
    await removeCartItem(skuId)
    ElMessage.success('删除成功')
    fetchData() // 重新拉取数据刷新页面
  } catch (e) {
    //
  }
}

// 页面加载时请求数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.cart-page {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}
h2 {
  margin-bottom: 20px;
}
.product-info {
  display: flex;
  align-items: center;
  gap: 15px;
}
.product-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}
.product-name {
  font-weight: 500;
  font-size: 15px;
}
.total-price {
  color: #f56c6c;
  font-weight: bold;
}
.checkout-bar {
  margin-top: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 30px;
}
.total {
  font-size: 16px;
}
.total .price {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
}
</style>