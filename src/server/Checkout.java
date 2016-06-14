/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:server.Checkout
 * @description:TODO
 * @date:2016-4-11 下午6:51:17
 * @version:v1.0.0 
 * @author:WangHao
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016-4-11     WangHao       v1.0.0        create
 *
 *
 */
package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrors;

/**
 * @className:server.Checkout
 * @description:
 * @version:v1.0.0
 * @date:2016-4-12 下午12:06:01
 * @author:WangHao
 */
@Controller
@RequestMapping("/checkout")
public class Checkout
{
	@RequestMapping(method = RequestMethod.POST)
	public void start(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException,
			IOException
	{
		// 返回结果
		String nonce = request.getParameter("payment_method_nonce");

		System.out.println(request.getParameter("orderId"));
		System.out.println(request.getParameter("amount"));
		System.out.println(nonce);

		TransactionRequest Trequest = new TransactionRequest().orderId("testOrderId11111")
				.amount(new BigDecimal("10.00")).paymentMethodNonce(nonce).options().submitForSettlement(true).done();

		Result<Transaction> result = TokenGenerator.gateway.transaction().sale(Trequest);

		// 返回信息
		String str;

		// 调用支付api成功
		if (result.isSuccess())
		{
			Transaction transaction = result.getTarget();
			String Id = transaction.getId();

			// 将交易状态改为settled
			System.out.println("查询交易流水号： " + transaction.getId());
			System.out.println("交易类型： " + transaction.getType());
			System.out.println("支付工具： " + transaction.getPaymentInstrumentType());
			System.out.println("信用卡类型： " + transaction.getCreditCard().getCardType());
			System.out.println("信用卡前六位号码： "+transaction.getCreditCard().getBin());
			System.out.println("信用卡后四位号码： "+transaction.getCreditCard().getLast4());
			System.out.println("持卡人姓名： "+transaction.getCreditCard().getCardholderName());
			System.out.println("有效日期： "+transaction.getCreditCard().getExpirationDate());
			System.out.println("信用卡部分卡号： "+transaction.getCreditCard().getMaskedNumber());
			System.out.println("IssuingBank： "+transaction.getCreditCard().getIssuingBank());

			String status = transaction.getStatus().toString();
			str = Id + status;
		} else
		{
			// 调用支付api失败，打印错误
			ValidationErrors errors = result.getErrors();
			for (ValidationError error : errors.getAllDeepValidationErrors())
			{
				System.out.println(error.getAttribute());
				System.out.println(error.getCode());
				System.out.println(error.getMessage());
			}
			str = result.getMessage();
		}

		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().write(str.getBytes("UTF-8"));

	}

	public static void main(String[] args)
	{
		FunctionTest.setTranSettled("29576s");
	}

}
