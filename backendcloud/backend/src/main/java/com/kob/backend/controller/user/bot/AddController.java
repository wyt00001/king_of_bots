package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
//Controller专门“接请求、送响应”——把前端发来的 HTTP 请求路由到后端业务，再把处理结果打包成 JSON/HTML 等形式还给前端
// | 注解                | 所属层 | 作用                                           |
// | ----------------- | --- | -------------------------------------------- |
// | `@Component`      | 通用  | 最基础，任何类都能用                                   |
// | `@Repository`     | 持久层 | 注册 Bean + 封装持久化异常（`@ControllerAdvice` 可统一转译） |
// | `@Configuration`  | 配置层 | 注册 Bean + 声明类里可写 `@Bean` 工厂方法                |
// | `@Controller`     | 表现层 | 注册 Bean + 视图解析（模板引擎）                         |
// | `@RestController` | 表现层 | 注册 Bean + 强制 `@ResponseBody`（JSON）           |
// 	@Service 注册成 Spring Bean, 声明“这是业务逻辑层”，便于 AOP/事务等针对服务层做切面
@RestController
public class AddController {

    @Autowired
    private AddService addService;

    @PostMapping("/api/user/bot/add/")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return addService.add(data);
    }
}
