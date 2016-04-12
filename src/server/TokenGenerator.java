/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:server.TokenGenerator
 * @description:
 * @date:2016-4-11 下午5:47:54
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

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

public class TokenGenerator
{
	public static BraintreeGateway gateway = new BraintreeGateway(
			Environment.SANDBOX, 
			"5yg2b93hy7rxr569", // Merchant ID
			"6ys4qzvm7q8y69h8", // Public Key
			"a56dff492ff7de6e886fbb623578a297" // Private Key
	);
	public String getToken()
	{
		return gateway.clientToken().generate();
	}
	/**
	 * 测试代码
	 */
	public static void main(String[] args)
	{
		// 打印产生的client token
		System.out.println(gateway.clientToken().generate());
	}

}
