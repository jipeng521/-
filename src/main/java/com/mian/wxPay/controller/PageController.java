package com.mian.wxPay.controller;

import com.mian.wxPay.entity.PayResult;
import com.mian.wxPay.entity.PreOrderResult;
import com.mian.wxPay.idworker.Sid;
import com.mian.wxPay.service.WxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
public class PageController {

    @Autowired
    private WxOrderService wxOrderService;

    @Autowired
    private Sid sid;

    //0.根目录跳转
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }


    // TODO 业务订单的状态
    public static boolean isOrderPaid = false;


    /**
     * @Description: 生成预付单
     */
    @RequestMapping(value = "/createPreOrder")
    public ModelAndView createPreOrder(String amount, String title, HttpServletRequest request, HttpServletResponse
            response) throws Exception {

        // 付款人
        String body = title;
        // 商户订单号
        String out_trade_no = sid.nextShort();
        // 订单总金额，单位为分
        String total_fee = amount;
        // 统一下单
        System.out.println("-----------------开始调用统一下单");
        PreOrderResult preOrderResult = wxOrderService.placeOrder(body, out_trade_no, total_fee);

        ModelAndView mv = new ModelAndView("payQrCode");
//        mv.addObject("qrCodeUrl", preOrderResult.getCode_url());
        mv.addObject("qrCodeUrl", "大继鹏");
        mv.addObject("total_fee",  total_fee);//金额
        mv.addObject("out_trade_no",  out_trade_no);//订单编号
        mv.addObject("body",  body);//付款人
        System.out.println("******************preOrderResult.getCode_url() = " + preOrderResult.getCode_url());
        System.out.println("**************金额total_fee = " + total_fee);
        System.out.println("***************编号out_trade_no = " + out_trade_no);
        System.out.println("***************付款人 body = " + body);

        return mv;
    }

    //查看是否支付成功
    @RequestMapping(value = "/wxPayIsSuccess")
    @ResponseBody
    public boolean wxPayIsSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO 查看订单是否支付成功，成功返回true，失败返回false
        return isOrderPaid;
    }

    //    微信支付异步通知
    @RequestMapping(value = "/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("*******正在调用异步通知的方法，别忘了在配置里填写本地址，用来接收微信异步通知");
        PayResult payResult = wxOrderService.getWxPayResult(request);

        boolean isPaid = payResult.getReturn_code().equals("SUCCESS") ? true : false;

        // 查询该笔订单在微信那边是否成功支付
        // 支付成功，商户处理后同步返回给微信参数
        PrintWriter writer = response.getWriter();
        if (isPaid) {
            System.out.println("================================= 支付成功 =================================");

            // ====================== 操作商户自己的业务，比如修改订单状态，生成支付流水等 start ==========================
            // TODO
            this.isOrderPaid = true;
            // ============================================ 业务结束， end ==================================

            // 通知微信已经收到消息，不要再给我发消息了，否则微信会8连击调用本接口
            String noticeStr = setXML("SUCCESS", "");
            writer.write(noticeStr);
            writer.flush();

        } else {
            System.out.println("================================= 支付失败 =================================");

            // 支付失败
            String noticeStr = setXML("FAIL", "");
            writer.write(noticeStr);
            writer.flush();
        }

    }

    //支付成功
    @RequestMapping(value = "/paySuccess")
    public String paySuccess() throws Exception {

        return "paySuccess";
    }

    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }


}
