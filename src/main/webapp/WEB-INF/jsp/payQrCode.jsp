<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信支付</title>
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
	<style>
		.container{width: 100%;}
		.mtm{margin-top: 10px;width: 600px;margin:0 auto;}
		#QRcode .pay-top{padding: 15px 0px;background: #FAFAFA;border: 2px #009900 dashed;margin: 20px 0px;font-family: 微软雅黑;}
		*{margin:0;padding:0;-webkit-font-smoothing:antialiased}#messages{list-style-type:none;margin:0;overflow:auto;overflow-x:hidden}#messages,li p{word-wrap:break-word}li p{margin-left:56px}.header{text-align:center;color:#eee;font-weight:200;font-size:24px;margin:60px}.test{box-shadow:0 2px 3px rgba(0,0,0,.1);-webkit-box-shadow:2px 2px 5px #333;box-shadow:2px 2px 5px #333}.main{background-color:#fff}.main,.main_header{border-radius:4px 4px 0 0}.main_header{background-color:#eee;margin:0;color:#666;padding:2px 0;font-size:12px}.main_header,.qrcode{text-align:center}.qrcode img{width:160px;border-radius:10px;height:160px}#messages{padding:8px;background-color:#000;color:#dc3545;text-align:left;font-size:12px;height:100%}.gray{color:#666}.green{color:green}.blue{color:#007bff}.yellow{color:#ffc107}.center{text-align:center}.gray a{text-decoration:none}.test{width:600px;margin: 0 auto;}
	</style>
</head>
<body>
<div class="container">
	<div class="alert alert-info mtm" role="alert">
		<p>请在规定的时间内完成微信扫码支付</p>
	</div>
	<div id="QRcode" class="mtm">
		<div class="text-center pay-top">
            <div class="nlpz_sjdjs">剩余：<span>00</span>:<span>00</span>:<span>00</span></div>
        </div>
		<div class="panel panel-default">

			<div class="panel-heading clearfix">
				<span class="pull-left">订单号：${out_trade_no }</span>
			</div>

			<div class="panel-heading clearfix">
				<span class="pull-left">总金额：${total_fee }（单位：分）</span>
			</div>
			<div class="panel-heading clearfix">
				<span class="pull-left">付款人：${body }</span>
			</div>

			<div class="panel-body text-center">
				<div id="code"></div>
			</div>
			<div class="panel-footer text-success text-center">请使用微信扫描 二维码以完成支付</div>
		</div>
		<div class="progress" style="display: none;">
			<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="10" aria-valuemin="10" aria-valuemax="100" style="width: 1%"></div>
		</div>
	</div>





	<input type="text" id="qrCodeUrl" value="${qrCodeUrl }"  style="display:none"/>






	<script src="<%=request.getContextPath() %>/js/jquery.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath() %>/js/qr.js" type="text/javascript"></script>

	<script type="text/javascript">

		$('#code').qrcode({
			render: "canvas",                    //设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
			text: $("#qrCodeUrl").val(), //扫描二维码后显示的内容,可以直接填一个网址，扫描二维码后自动跳向该链接
			width: "500",                      //二维码的宽度
			height: "500",                    //二维码的高度
			background: "#ffffff",           //二维码的后景色
			foreground: "#000000",           //二维码的前景色
			src: '../images/logo.png'     //二维码中间的图片
		});




        //当前时间加2分钟
        //1. js获取当前du时间
        var date=new Date();
        //2. 获取当前分zhi钟dao
        var min=date.getMinutes();
        //3. 设置当前时间+2分钟：把当前分钟数+2后的值重新设置为date对象的分钟数
        date.setMinutes(min+2);
        var starttime = date;
        function tt(){
            var nowtime = new Date();
            //alert(nowtime);
            var time = starttime - nowtime;
            var minute = parseInt(time / 1000 / 60 % 60);//分钟
            var seconds = parseInt(time / 1000 % 60);//秒

            if(hour<=0 && minute<=0 && seconds<=0){
                clearInterval(sj);//倒计时停止
                clearInterval(check);//停止检查，支付结果的异步调用
                $('.nlpz_sjdjs').html("订单超时");
                $('#code').html("二维码因超时未支付而消失啦");
            }else{
                $('.nlpz_sjdjs').html('剩余支付时间：'+'<span>'+minute+'</span>'+' 分 '+'<span>'+seconds+'</span>'+' 秒 ');
            }
        }
        var sj = setInterval(function () {tt();}, 1000);



		// 查询是否支付成功
		function checkPayResult() {
			$.get("/wxPayIsSuccess", function(data) {

				if (data) {
					window.location.href = "/paySuccess";
				}
			});
		};
        // 每个3秒调用后台方法，查看订单是否已经支付成功
         var check = setInterval(function () {checkPayResult();}, 3000);

	</script>
</body>
</html>