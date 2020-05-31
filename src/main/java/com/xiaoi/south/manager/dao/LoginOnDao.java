package com.xiaoi.south.manager.dao;

import org.apache.ibatis.annotations.Param;

public interface LoginOnDao {
    int getCount(@Param("username")String username,@Param("password")String password);
}
