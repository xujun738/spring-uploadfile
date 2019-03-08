package com.tgram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2018</p>
 * <p>Company : tgram </p>
 *
 * @author eric
 * @version 1.0
 * @Date 2019/3/8 上午10:03
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}