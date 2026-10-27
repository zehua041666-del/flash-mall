package com.flashmall.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 通用分页结果。
 * 放 common，后续 order / cart 等模块分页都复用。
 */
@Data
@AllArgsConstructor
public class PageResult<T> {
    private long total;       // 总记录数
    private long pageNum;     // 当前页码（从 1 开始）
    private long pageSize;    // 每页条数
    private List<T> list;     // 当前页数据
}