package com.tgram.observer;

import com.tgram.web.ctrl.MyWebSocket;

import java.util.Observable;
import java.util.Observer;

/**
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2018</p>
 * <p>Company : tgram </p>
 *
 * @author eric
 * @version 1.0
 * @Date 2019/3/8 下午1:55
 */
public class ProgressObserver implements Observer {

    /**
     * userId也可以放入Progress对象中
     */
    private String userId;

    public ProgressObserver(String userId) {
        this.userId = userId;
    }

    @Override
    public void update(Observable o, Object arg) {

        Progress progress = (Progress) o;
        MyWebSocket.sendInfo(progress.getProgress() + "", userId);
    }
}
