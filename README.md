# spring-uploadfile

#### 项目介绍
通过fastdfs-java-client的api按块下载文件,

下载成功后写入到输出流并将进度按用户通过websocket推送到客户端

注:该demo只是单纯实现了有进度条的下载,用户发送下载请求后会另起一个线程
进行文件下载