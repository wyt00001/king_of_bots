package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String photo;
    private Integer rating;
    private String openid;
}
//POJO Plain Old Java Object。一个类只由 Java 语言最原始、最通用的语法构成，不依赖任何第三方框架的接口或注解，就可以称为 POJO。私有字段 + 公有 getter/setter + 无参构造
