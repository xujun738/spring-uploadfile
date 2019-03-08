<!DOCTYPE html>
<html>
<head>
<style type="text/css">
progress {
    background-color: #56BE64;
} 
progress::-webkit-progress-bar {
    background: #ccc;
} 
progress::-webkit-progress-value {
    background: #56BE64;
}
percentage{
	position: fixed;
	left: 160px;
}
</style>
<script src="./js/jquery.min.js"></script>
</head>
<body>
	<h1>Spring Boot file upload example</h1>
	<form id="FileuploadForm" method="POST" action="/upload" enctype="multipart/form-data">
		<input type="file" name="uploadFile" id="uploadFile"/><br />
		<br /> <input type="button" id="btnUpload" value="上传文件" onclick="upload()" />
		<div id="msg"></div>
	</form>
	<!--进度条部分(默认隐藏)--> 
	<div style="display: none;" class="progress-body"> 
		<div>
			<span style="width: 100px; display: inline-block; text-align: right">上传进度：</span>
			<progress></progress><percentage>0%</percentage>
		</div> 
		<div>
			<span style="width: 100px; display: inline-block; text-align: right">上传速度：</span>
			<span style="width: 300px;" class="progress-speed">0 M/S, 0/0M</span>
		</div>
		<div>
			<span style="width: 100px; display: inline-block; text-align: right">上传状态：</span>
			<span style="width: 300px;" class="progress-info">请选择文件并点击上传文件按钮</span>
		</div>
	</div>
	<script>
    // 上传文件
    function upload(){
       $("#msg").text("");
       var checkFile = $("#uploadFile").val();
 	   var msgInfo = "";
       if(null==checkFile || ""==checkFile){
    	   $("#msg").text("文件为空,请选择文件!");
       }else{
			var formData = new FormData(document.getElementById("FileuploadForm"));
			$.ajax({
			    	   type: "POST",
			    	   enctype:'multipart/form-data',
			    	   url: '/upload',
			    	   data: formData,
			    	   cache:false,
			    	   processData:false,
			    	   contentType:false,
			    	   error:function(result){
			    		   console.log("error");
				    	   console.log(result);
			    	   		flag = false;
			    	   		$("#msg").text("访问服务器错误，请重试！");
			    	   },
			    	   success: function(result){
			    		    console.log("success");
			    	   },
			    	   xhr: function () { 
				    	   var xhr = $.ajaxSettings.xhr(); 
				    	   if (xhr.upload) { 
				    		   $("#btnUpload").attr("disabled",true);
				    		   $("#uploadFile").attr("disabled",true);
					    	   //处理进度条的事件
					    	    xhr.upload.addEventListener("progress", progressHandle, false); 
					    	    //加载完成的事件 
					    	    xhr.addEventListener("load", completeHandle, false); 
					    	    //加载出错的事件 
					    	    xhr.addEventListener("error", failedHandle, false); 
					    	    //加载取消的事件 
					    	    xhr.addEventListener("abort", canceledHandle, false);
					    	    //开始显示进度条 
					    	    showProgress(); 
					    	    return xhr; 
					    	 }
				    	 }
		   			}, 'json');
		}
    }
    var start = 0;
	//显示进度条的函数 
	function showProgress() {
		start = new Date().getTime(); 
		$(".progress-body").css("display", "block"); 
	} 
	//隐藏进度条的函数 
	function hideProgress() {
		$("#uploadFile").val('');
		$('.progress-body .progress-speed').html("0 M/S, 0/0M"); 
		$('.progress-body percentage').html("0%");
		$('.progress-body .progress-info').html("请选择文件并点击上传文件按钮"); 
		$("#btnUpload").attr("disabled",false);
		$("#uploadFile").attr("disabled",false);
		//$(".progress-body").css("display", "none"); 
	} 
	//进度条更新 
	function progressHandle(e) { 
		$('.progress-body progress').attr({value: e.loaded, max: e.total}); 
		var percent = e.loaded / e.total * 100; 
		var time = ((new Date().getTime() - start) / 1000).toFixed(3);
		if(time == 0){
			time = 1;
		}
		$('.progress-body .progress-speed').html(((e.loaded / 1024) / 1024 / time).toFixed(2) + "M/S, " + ((e.loaded / 1024) / 1024).toFixed(2) + "/" + ((e.total / 1024) / 1024).toFixed(2) + " MB. ");
		$('.progress-body percentage').html(percent.toFixed(2) + "%");
		if (percent == 100) { 
			$('.progress-body .progress-info').html("上传完成,后台正在处理..."); 
		} else { 
			$('.progress-body .progress-info').html("文件上传中..."); 
		} 
	}; 
	//上传完成处理函数 
	function completeHandle(e) { 
		$('.progress-body .progress-info').html("上传文件完成。"); 
		setTimeout(hideProgress, 2000); 
	}; 
	//上传出错处理函数 
	function failedHandle(e) { 
		$('.progress-body .progress-info').html("上传文件出错, 服务不可用或文件过大。"); 
		setTimeout(hideProgress, 2000); 
	}; 
	//上传取消处理函数 
	function canceledHandle(e) { 
		$('.progress-body .progress-info').html("上传文件取消。"); 
		setTimeout(hideProgress, 2000); 
	};
	</script>
</body>
</html>