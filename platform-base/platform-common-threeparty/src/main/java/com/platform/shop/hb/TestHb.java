package com.platform.shop.hb;

import java.util.List;

import com.huobi.client.SyncRequestClient;
import com.huobi.client.model.Account;
import com.huobi.client.model.Candlestick;
import com.huobi.client.model.Deposit;
import com.huobi.client.model.Withdraw;
import com.huobi.client.model.enums.AccountType;
import com.huobi.client.model.enums.CandlestickInterval;
import com.platform.utils.JsonUtil;
import com.platform.utils.ObjToStringUtil;

public class TestHb {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String apiKey="vf25treb80-3a968a53-0c8b1d9e-57b3d";
//		String secretKey="3ad2020c-a0e069c1-fd553aca-edb68";
		String apiKey="cc4bc54a-dbye2sf5t7-f8d81c5c-da595";
		String secretKey="b86aeb50-e0e20e16-5ccbcd03-8c02c";
		SyncRequestClient syncClient = SyncRequestClient.create(apiKey, secretKey);//.create();

		// Get the timestamp from Huobi server and print on console
		long timestamp = syncClient.getExchangeTimestamp();
		System.out.println(timestamp);

		// Get the latest btcusdt‘s candlestick data and print the highest price on console
		List<Candlestick> candlestickList =
		    syncClient.getLatestCandlestick("btcusdt", CandlestickInterval.DAY1, 20);
		for (Candlestick item : candlestickList) {
		    System.out.println(item.getHigh());
		}
		 Account accounts=syncClient.getAccountBalance(AccountType.SPOT);
		 System.out.println(JsonUtil.getJsonByObj(accounts));
		 System.out.println(JsonUtil.getJsonByObj(accounts.getBalance("usdt")));
		 
		 List<Deposit> deposits=syncClient.getDepositHistory("usdt", 0l, 100);
		 System.out.println(JsonUtil.getJsonByObj(deposits));
		 System.out.println(ObjToStringUtil.objToString(deposits));
		 
		 List<Withdraw> withdraws=syncClient.getWithdrawHistory("usdt", 0l, 100);
		 System.out.println(JsonUtil.getJsonByObj(withdraws));
		 System.out.println(ObjToStringUtil.objToString(withdraws));
		 
		 
		
		
	}

}
