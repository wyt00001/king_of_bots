package com.kob.backend.mapper;

import com.kob.backend.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface UserMapper extends BaseMapper<User> {//extends BaseMapper<User> 继承了MyBatis提供的接口，从而在调用insert时会写入Mysql数据库

}
