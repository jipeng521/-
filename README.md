# 微信扫码支付整合版

## 项目介绍

项目中需要用到微信支付，然而微信官方模块的sdk与demo下载，下载后发现仅仅只有sdk，并没有demo提供调用。很坑。

![image-20200623105423820](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623105423820.png)

坑人的官网地址：

https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=11_1



没办法，公司要用啊。只好自己动手写一个，

整体使用了springBoot+maven+jsp。

下载后直接就可以运行的，

修改配置也很简单。一起看一看吧。

## 运行效果

在这里输入金额和付款人信息，当然，可以通过其他方式来传参，这里仅仅只是演示一下。

![image-20200623110222063](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623110222063.png)



扫码支付页面

![image-20200623110448100](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623110448100.png)

支付成功页面

![image-20200623113306083](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623113306083.png)

### 回显信息

跳转到扫码支付页面，回显了订单金额，付款人信息等。

订单编号是根据当前日期+随机字母/数字组成的，做其他项目时，可以参考一下。

### 倒计时js实现

可以看到有个倒计时显示，我也是写了好久，具体代码在前台页面的js里。

![image-20200623121034463](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623121034463.png)

超时后，原本倒计时的位置，提示订单超时；二维码进行销毁；计时器清空等操作。

![image-20200623110941486](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623110941486.png)

### 二维码的生成

有很多工具，这里我用的是二维码生成插件jquery.qrcode.js，

他是基于jquery的二维码生成的js，用这个之前，还得先引入jquery。

具体用法可以百度一下，或者在我代码里就可以看到

![image-20200623111632371](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623111632371.png)

## 使用方法

### 启动

springBoot项目，启动这个WxPayApplication即可。

![image-20200623121313589](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623121313589.png)

### 改配置

在配置文件中修改自己微信支付的配置即可

路径

\wxpay-example\src\main\java\com\mian\wxPay\config\WeChatConfig.java

![image-20200623111957705](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623111957705.png)

### 改二维码内容

目前是固定的，扫出来是“大继鹏”，我也是为了这个项目下载后可以直接运行才这么做的。

正常来说是把“大继鹏”这行注释掉；上面那行代码的注释打开，

如果上面的配置文件都没问题，就会根据微信的预支付订单号来生成对应的二维码。

正常来说，微信预支付的链接是这样：“weixin：//wxpay/bizpayurl?sr=XXXXX。”

根据这个链接生成二维码，微信扫码直接就可以进行支付啦。

![image-20200623112223806](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623112223806.png)

![image-20200623112648897](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623112648897.png)



### 改二维码中间的LOGO

本来人家官方的js插件不支持生成带LOGO的二维码，是我自己修改过的js，就是这个“qr.js”

LOGO的图片路径如下所示：

![image-20200623112850189](C:\Users\YAY\AppData\Roaming\Typora\typora-user-images\image-20200623112850189.png)



## 结束语

> 基本上就这些吧，我对 Java了解还不够，深感自己学识浅薄，
>
> 这个整合版还有很大优化余地，
>
> 随缘更新吧~
>
> 继鹏~QQ:571928856