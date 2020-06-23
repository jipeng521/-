package com.mian.wxPay.service;


import com.mian.wxPay.entity.PayResult;
import com.mian.wxPay.entity.PreOrderResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理微信支付的相关订单业务
 */
public interface WxOrderService {
	
	/**
	 * 调用微信接口进行统一下单
	 */
	public PreOrderResult placeOrder(String body, String out_trade_no, String total_fee) throws Exception;
	
	/**
	 * 取支付结果
	 */
	public PayResult getWxPayResult(HttpServletRequest request) throws Exception;
	
}
