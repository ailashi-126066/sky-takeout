package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealDishMapper
{
    //根据菜品id查询套餐id
    List<Long> getSetmealIdsByDishIds(@Param("dishIds") List<Long> dishIds);
}
