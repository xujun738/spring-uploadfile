package com.tgram.web.ctrl;

import java.io.IOException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONObject;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tgram.observer.Progress;
import com.tgram.observer.ProgressObserver;
import com.tgram.web.bean.InfoMsg;
import com.tgram.config.FastDFSClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/upload")
public class UploadCtrl {

    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;

    @Autowired
    private FastFileStorageClient storageClient;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String fileUploadInit() {
        // InfoMsg infoMsg = new InfoMsg();

        return "upload";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public InfoMsg fileUpload(@RequestParam("uploadFile") MultipartFile file) {
        InfoMsg infoMsg = new InfoMsg();
        if (file.isEmpty()) {
            infoMsg.setCode("error");
            infoMsg.setMsg("Please select a file to upload");
            return infoMsg;
        }

        try {

            String url = fastDFSClientWrapper.uploadFile(file);
            System.out.println("上传的文件URL:   " + url);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("filesize", file.getSize());

//			File tmp = new File(TMP_PATH, file.getOriginalFilename());
//			if(!tmp.getParentFile().exists()){
//				tmp.getParentFile().mkdirs();
//			}
//			file.transferTo(tmp);

            infoMsg.setCode("success");
            infoMsg.setMsg("You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            infoMsg.setCode("error");
            infoMsg.setMsg("Uploaded file failed");
        }

        return infoMsg;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteFile(String fileName) {
        try {
            fastDFSClientWrapper.deleteFile(fileName);
            return "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }
    }

    @RequestMapping(value = "/download")
    @ResponseBody
    public void download(String fileName, HttpServletResponse response, String userId) throws IOException {

//        StorePath storePath = StorePath.parseFromUrl(fileName);
        // 配置文件下载
//        response.setHeader("content-type", "application/octet-stream");
//        response.setContentType("application/octet-stream");
//        // 下载文件能正常显示中文
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//        byte[] r = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadCallback<byte[]>() {
//            @Override
//            public byte[] recv(InputStream ins) throws IOException {
//                byte[] reulst = IOUtils.toByteArray(ins);
//                System.out.println(reulst.length);
//                return reulst;
//            }
//        });
//        response.getOutputStream().write(r);

        if (MyWebSocket.isUserConnected(userId)) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            Progress progress = new Progress(response, storageClient, fileName);
            progress.addObserver(new ProgressObserver(userId));
            progress.run();
        }

//        FileInfo fileInfo = storageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
//        long fileSize = fileInfo.getFileSize();
//        System.out.println("文件总大小:" + fileSize);
//        long slice = Math.floorDiv(fileSize, 100);
//        long left = fileSize - slice * 99;
//
//        byte[] sliceBytes = null;
//        int downloadBytes = 0;
//
//        for (int i = 0; i < 100; i++) {
//
//            if (i != 99) {
//                sliceBytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), i * slice, slice, ins -> {
//                    byte[] result = IOUtils.toByteArray(ins);
//                    response.getOutputStream().write(result);
//                    return result;
//                });
//
//
//            } else {
//                sliceBytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), 99 * slice, left, ins -> {
//                    byte[] result = IOUtils.toByteArray(ins);
//                    response.getOutputStream().write(result);
//                    return result;
//                });
//            }
//            downloadBytes = downloadBytes + sliceBytes.length;
//            MyWebSocket.sendInfo((i + 1) + "", userId);
//        }
//        response.getOutputStream().flush();
//
////        新起一个线程,然后按段下载文件,每段下载成功后将进度值推送到对应的用户
//        System.out.println("共下载:" + downloadBytes);

    }

}
