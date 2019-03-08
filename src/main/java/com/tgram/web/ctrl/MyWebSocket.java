package com.tgram.web.ctrl;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2018</p>
 * <p>Company : tgram </p>
 *
 * @author eric
 * @version 1.0
 * @Date 2019/3/8 上午9:57
 */
@ServerEndpoint("/websocket/{userId}")
@Component
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String userId = null;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @OnError public void onError(Session session, Throwable error) {
     * System.out.println("发生错误");
     * error.printStackTrace();
     * }
     * <p>
     * <p>
     * public void sendMessage(String message) throws IOException {
     * this.session.getBasicRemote().sendText(message);
     * //this.session.getAsyncRemote().sendText(message);
     * }
     * <p>
     * <p>
     * /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) {
        for (MyWebSocket item : webSocketSet) {
            try {
                //这里可以设定只推送给这个userId的，为null则全部推送
                if (userId == null) {
                    item.sendMessage(message);
                } else if (item.userId.equals(userId)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 判断是否与该用户端创建socket连接
     *
     * @param userId
     * @return
     */
    public static boolean isUserConnected(String userId) {
        for (MyWebSocket item : webSocketSet) {
            if (item.userId.equals(userId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
