/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:server.FunctionTest
 * @description:TODO
 * @date:2016-4-13 上午9:10:03
 * @version:v1.0.0 
 * @author:WangHao
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016-4-13     WangHao       v1.0.0        create
 *
 *
 */
package server;

import java.math.BigDecimal;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.ValidationError;

public class FunctionTest
{
	// 测试信用卡卡号
	public static final String AMERICAN_EXPRESS = "378282246310005";
	public static final String MASTERCARD = "5555555555554444";
	public static final String VISA = "4111111111111111";

	/**
	 * 仅用于测试 将交易状态改为Transaction.Status.SETTLED
	 */
	public static void setTranSettled(String transactionId)
	{
		TokenGenerator.gateway.testing().settle(transactionId);
	}

	/**
	 * 仅用于测试 将交易状态改为Transaction.Status.CONFIRMED
	 */
	public static void setTranConfirmed(String transactionId)
	{
		TokenGenerator.gateway.testing().settlementConfirm(transactionId);
	}

	/**
	 * 仅用于测试 将交易状态改为Transaction.Status.DECLINED
	 */
	public static void setTranDecline(String transactionId)
	{
		TokenGenerator.gateway.testing().settlementDecline(transactionId);
	}

	/**
	 * 
	 * @Description:查看交易状态
	 * @param transaction
	 * @version:v1.0
	 * @author:WangHao
	 * @date:2016-4-13 上午9:30:43
	 */
	public static void getTranStatus(String transactionId)
	{
		TokenGenerator.gateway.transaction().find(transactionId).getStatus();
	}

	/**
	 * 取消交易或退款
	 */
	public static void cancleTransaction(String transactionId)
	{
		Transaction transaction = TokenGenerator.gateway.transaction().find(transactionId);
		if (transaction.getStatus() == Transaction.Status.SUBMITTED_FOR_SETTLEMENT)
		{
			// can void
			voidTransaction(transactionId);
		}
		else if (transaction.getStatus() == Transaction.Status.SETTLED)
		{
			// will have to refund it
			refundTransaction(transactionId);
		}
		else
		{
			// this example only expected one of the two above statuses
		}
	}

	public static void voidTransaction(String transactionId)
	{
		Result<Transaction> result = TokenGenerator.gateway.transaction().voidTransaction(transactionId);
		if (result.isSuccess())
		{
			// transaction successfully voided
		}
		else
		{
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors())
			{
				System.out.println(error.getMessage());
			}
		}
	}

	public static void refundTransaction(String transactionId)
	{
		Result<Transaction> result = TokenGenerator.gateway.transaction().refund(transactionId);
		if (!result.isSuccess())
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors())
			{
				System.out.println(error.getMessage());
			}
	}
	
	/**
	 * 部分退款
	 */
	public static void refundTransaction(String transactionId, BigDecimal amount)
	{
		Result<Transaction> result = TokenGenerator.gateway.transaction().refund(transactionId, amount);
		if (!result.isSuccess())
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors())
			{
				System.out.println(error.getMessage());
			}
	}

}
