import request from './request'

// 加入购物车
export function addToCart(data) {
  return request.post('/cart/add', data)
}

// 获取购物车列表
export function getCartList() {
  return request.get('/cart/list')
}

// 修改数量
export function updateCartQuantity(skuId, quantity) {
  return request.put('/cart/quantity', null, {
    params: { skuId, quantity }
  })
}

// 删除商品
export function removeCartItem(skuId) {
  return request.delete(`/cart/${skuId}`)
}