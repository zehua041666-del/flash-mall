package com.flashmall.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flashmall.user.entity.User;

@Mapper 
public interface UserMapper extends BaseMapper<User>{
    
}
