import request from './request'

/**
 * 获取商品列表分页数据
 * @param {number} pageNum 第几页
 * @param {number} pageSize 每页多少条
 */
export function getProductList(pageNum = 1, pageSize = 10) {
  return request.get('/product/list', {
    params: { pageNum, pageSize }
  })
}