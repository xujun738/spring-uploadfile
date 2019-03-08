package com.tgram.observer;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Observable;

/**
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2018</p>
 * <p>Company : tgram </p>
 *
 * @author eric
 * @version 1.0
 * @Date 2019/3/8 下午2:00
 */
public class Progress extends Observable implements Runnable {

    private HttpServletResponse response;

    /**
     * 等更新的进度值
     */
    private Integer progress;

    private FastFileStorageClient storageClient;

    /**
     * 待下载的文件路径  /group1/M00/00/00/文件名
     */
    private String fileName;

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Integer getProgress() {
        return progress;
    }

    /**
     * 如果progress设置为小于0的值,则表示下载出现异常,然后通过websocket发送异常给客户端
     *
     * @param progress
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
        setChanged();
        //通知所有观察者
        notifyObservers();
    }

    /**
     * default constructor
     */
    public Progress() {

    }

    /**
     * @param response httpservletrespone对象
     */
    public Progress(HttpServletResponse response, FastFileStorageClient storageClient, String fileName) {
        this.response = response;
        this.storageClient = storageClient;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            StorePath storePath = StorePath.parseFromUrl(fileName);
            FileInfo fileInfo = storageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
            long fileSize = fileInfo.getFileSize();
            System.out.println("文件总大小:" + fileSize);
            long slice = Math.floorDiv(fileSize, 100);
            long left = fileSize - slice * 99;

            byte[] sliceBytes = null;

            for (int i = 0; i < 100; i++) {

                if (i != 99) {
                    sliceBytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), i * slice, slice, ins -> {
                        byte[] result = IOUtils.toByteArray(ins);
                        response.getOutputStream().write(result);
                        return result;
                    });


                } else {
                    sliceBytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), 99 * slice, left, ins -> {
                        byte[] result = IOUtils.toByteArray(ins);
                        response.getOutputStream().write(result);
                        return result;
                    });
                }
                //通知观赏者进度改变
                setProgress(i + 1);
            }
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
            //通知客户端下载出现异常
            setProgress(-1);
        }
    }
}
