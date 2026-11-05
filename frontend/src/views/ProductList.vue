<template>
  <div class="product-page">
    <h2>商品列表</h2>

    <!-- 商品卡片区域：加上 v-loading 后，请求时会自动转圈 -->
    <div class="product-list" v-loading="loading">
      <el-card
        v-for="item in productList"
        :key="item.spuId"
        class="product-card"
        :body-style="{ padding: '0px' }"
      >
        <img :src="item.mainImage" class="image" />
        <div class="info">
          <h3 class="name">{{ item.name }}</h3>
          <p class="price">¥ {{ item.minPrice }} 起</p>
          <el-button
            type="danger"
            size="small"
            class="add-cart-btn"
            :disabled="!item.skuList || item.skuList.length === 0"
            @click="openSkuDialog(item)"
          >
            加入购物车
          </el-button>
        </div>
      </el-card>
    </div>

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

    <!-- 选规格弹窗 -->
    <el-dialog v-model="dialogVisible" title="选择规格" width="440px">
      <div v-if="currentSpu">
        <div class="dialog-product-name">{{ currentSpu.name }}</div>
        <el-radio-group v-model="selectedSkuId" class="sku-group">
          <el-radio
            v-for="sku in currentSpu.skuList"
            :key="sku.id"
            :value="sku.id"
            border
            class="sku-radio"
          >
            {{ sku.name }} —— ¥{{ sku.price }}
          </el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="adding" @click="handleAddToCart">确定加入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProductList } from '@/api/product'
import { addToCart } from '@/api/cart'

// 列表状态
const productList = ref([])
const loading = ref(false)
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(6)

// 选规格弹窗状态
const dialogVisible = ref(false)
const currentSpu = ref(null)
const selectedSkuId = ref(null)
const adding = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const res = await getProductList(pageNum.value, pageSize.value)
    productList.value = res.list
    total.value = res.total
  } catch (e) {
    // 拦截器已处理
  } finally {
    loading.value = false
  }
}

function handlePageChange(newPage) {
  pageNum.value = newPage
  fetchData()
}

// 打开选规格弹窗
function openSkuDialog(item) {
  currentSpu.value = item
  // 如果只有一个规格，直接默认选中，省一次点击
  selectedSkuId.value = item.skuList.length === 1 ? item.skuList[0].id : null
  dialogVisible.value = true
}

// 确认加入购物车
async function handleAddToCart() {
  if (!selectedSkuId.value) {
    ElMessage.warning('请先选择规格')
    return
  }
  adding.value = true
  try {
    await addToCart({ skuId: selectedSkuId.value, quantity: 1 })
    ElMessage.success('加入购物车成功')
    dialogVisible.value = false
  } catch (e) {
    // 未登录会走 401 → 拦截器跳登录；其他错误拦截器已弹
  } finally {
    adding.value = false
  }
}

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
  min-height: 300px;
}
.product-card {
  width: 220px;
  transition: transform 0.2s;
}
.product-card:hover {
  transform: translateY(-5px);
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
  text-overflow: ellipsis;
}
.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 12px 0;
}
.add-cart-btn {
  width: 100%;
}
.pagination-box {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

/* 弹窗内样式 */
.dialog-product-name {
  font-weight: 500;
  margin-bottom: 12px;
  color: #333;
}
.sku-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.sku-radio {
  margin-right: 0 !important;
  width: 100%;
  height: auto;
  padding: 10px 14px;
}
</style>