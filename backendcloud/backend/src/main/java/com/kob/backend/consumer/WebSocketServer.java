package com.kob.backend.consumer;

import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
//习惯上会把“负责从队列里拉消息并进行业务处理”的代码单独放到一个模块（或 package）里，起名就叫 consumer
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
//WebSocket 只做了一件事——让“浏览器”和“服务器”之间，第一次握手成功后，变成一条“永不断线”的双向通道，从此双方可以随时随地、低成本地互相发数据。
//建立在 TCP 之上，但使用 自己轻量的帧协议（文本帧、二进制帧、ping/pong 心跳帧）。
// 双方随时发，不需要再带 Cookie、User-Agent 等冗余头。
// 浏览器 API 只有 4 个事件：Open、Message、Error、Close。服务器同理

@Component
@ServerEndpoint("/websocket/{token}")
// 注意不要以'/'结尾 @ServerEndpoint 把该类注册成 WebSocket 端点，路径模板中 {token} 可在后面用 @PathParam 取出。
public class WebSocketServer {//每个新的连接都会由容器（Tomcat/Jetty/Undertow）new 一个新的 WebSocketServer 实例，因此实例变量是线程隔离的，而 static 变量是全局共享的。

    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); //static 属于类而不属于任何实例
    private User user;
    private Session session = null;
    public Game game = null;
    private final static String AddPlayerUrl = "https://app7634.acapp.acwing.com.cn/api/player/add/";
    private final static String RemovePlayerUrl = "https://app7634.acapp.acwing.com.cn/api/player/remove/";
    public static UserMapper userMapper; //UserMapper是一个接口，但userMapper并不是“接口的实例”，而是一个“引用变量”，它指向的是某个实现了这个接口的对象。
    public static RecordMapper recordMapper;
    private static BotMapper botMapper;
    public static RestTemplate restTemplate;
    //CopyOnWriteArraySet 线程安全的集合，可用于并发场景，适合于写操作多读操作少的场景。

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("WebSocket opened: " + session.getId());
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
        System.out.println("User found: " + this.user.getUsername());
    }

    private void startMatching(Integer botId) {
        System.out.println("Start matching: " + this.user.getUsername());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        restTemplate.postForObject(AddPlayerUrl, data, String.class);
    }

    private void stopMatching() {
        System.out.println("Stop matching: " + this.user.getUsername());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(RemovePlayerUrl, data, String.class);
    }

    @OnClose
    public void onClose() {
        System.out.println("WebSocket closed: " + session.getId());
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User user1 = userMapper.selectById(aId);
        User user2 = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);
        Game game = new Game(
                13,
                14,
                20,
                user1.getId(),
                botA,
                user2.getId(),
                botB
        );
        game.createMap();
        if (users.get(user1.getId()) != null) {
            users.get(user1.getId()).game = game;
        }
        if (users.get(user2.getId()) != null) {
            users.get(user2.getId()).game = game;
        }
        game.start();
        System.out.println("Game started: " + user1.getUsername() + " vs " + user2.getUsername());
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());
        JSONObject resp1 = new JSONObject();
        resp1.put("event", "start-matching");
        resp1.put("side", "left");
        resp1.put("opponent_username", user2.getUsername());
        resp1.put("opponent_photo", user2.getPhoto());
        resp1.put("game", respGame);

        JSONObject resp2 = new JSONObject();
        resp2.put("event", "start-matching");
        resp2.put("side", "right");
        resp2.put("opponent_username", user1.getUsername());
        resp2.put("opponent_photo", user1.getPhoto());
        resp2.put("game", respGame);
        if (users.get(user1.getId()) != null) {
            users.get(user1.getId()).sendMessage(resp1.toJSONString());
        }
        if (users.get(user2.getId()) != null) {
            users.get(user2.getId()).sendMessage(resp2.toJSONString());
        }
    }

    private void move(int direction) {
        System.out.println("玩家 " + this.user.getUsername() + " 尝试移动方向: " + direction);
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1))  // 亲自出马
                game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1))  // 亲自出马
                game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
//可见性 一个线程对共享变量的修改，能不能立刻被其他线程看到。每个线程有自己的工作内存（CPU 缓存），如果不主动刷回主存或重新读取，别的线程就永远看不到最新值。
//原子性 一个或一组操作“不可再拆分”，要么全部执行完，要么完全不执行，不会被线程调度器中途打断。
//线程安全 一段代码/对象在被多个线程同时使用时，仍然能保持业务语义正确，不会出现数据污染、崩溃或异常结果。
