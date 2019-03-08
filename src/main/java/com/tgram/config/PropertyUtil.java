package com.tgram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2018</p>
 * <p>Company : tgram </p>
 *
 * @author eric
 * @version 1.0
 * @Date 2018/10/19 下午3:42
 */
@Configuration
public class PropertyUtil {


    @Value("${fdfs.visit.url}")
    private String fdfsUrl;

    @Value("${fdfs.visit.port}")
    private String fdfsPort;

    public String getFdfsUrl() {
        return fdfsUrl;
    }

    public void setFdfsUrl(String fdfsUrl) {
        this.fdfsUrl = fdfsUrl;
    }

    public String getFdfsPort() {
        return fdfsPort;
    }

    public void setFdfsPort(String fdfsPort) {
        this.fdfsPort = fdfsPort;
    }
}
