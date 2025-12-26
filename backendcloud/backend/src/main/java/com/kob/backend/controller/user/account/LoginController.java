package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/api/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username, password);

        // GET请求参数拼接在url后，POST请求参数放在请求体中。GET过长会被截断，POST理论上无限制。GET暴露在地址栏，不安全，POST相对安全些，但仍需加密。GET幂等，POST非幂等。
        // GET可被缓存，POST默认不缓存。GET参数在url，可收藏，回退，POST刷新会重新提交数据，浏览器会弹“确认重新提交表单“提示。GET只能URL-Encoding(ASCII)，POST支持多种编码。
        // GET把header和data一起发一个TCP包，POST先发送header,服务器发送100(continue)后再发body，两个TCP包。(小body时浏览器可能合并)
    }
}
