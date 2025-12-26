package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
// 业务逻辑
public class AddServiceImpl implements AddService { //implements 实现接口，extends继承类

    @Autowired
    private BotMapper botMapper;
// | 方法                    | 所属类/接口                  | 作用                                                 |
// | --------------------- | ----------------------- | -------------------------------------------------- |
// | `getPrincipal()`      | `Authentication`        | **从“已认证令牌”里取出“用户对象”**（通常是 `UserDetails`）           |
// | `getContext()`        | `SecurityContextHolder` | **拿到当前线程绑定的 SecurityContext**（容器里存 Authentication） |
// | `getAuthentication()` | `SecurityContext`       | **从 SecurityContext 里取出 Authentication 令牌**        |

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginuser = (UserDetailsImpl) token.getPrincipal();
        User user = loginuser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        Map<String, String> map = new HashMap<>();
        if (title == null || title.length() == 0) {
            map.put("error_message", "标题不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }
        if (description == null || description.length() == 0) {
            description = "这个用户很懒，什么也没留下~";
        }
        if (description.length() > 300) {
            map.put("error_message", "描述长度不能大于300");
            return map;
        }
        if (content == null || content.length() == 0) {
            map.put("error_message", "代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "代码长度不能大于10000");
            return map;
        }
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(queryWrapper) >= 10) {
            map.put("error_message", "每个用户最多只能创建10个Bot!");
            return map;
        }
        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, now, now);
        botMapper.insert(bot);
        map.put("error_message", "success");

        return map;
    }
}
