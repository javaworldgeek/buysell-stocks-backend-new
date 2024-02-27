package com.javaworldgeek.buysellstocksbackendnew.thread;

import java.math.BigDecimal;

import com.javaworldgeek.buysellstocksbackendnew.entity.Product;
import com.javaworldgeek.buysellstocksbackendnew.entity.StockStatus;
import com.javaworldgeek.buysellstocksbackendnew.service.BuySellStocksService;
import com.javaworldgeek.buysellstocksbackendnew.util.BuySellStocksUtil;

public class ProductBulkExecutorTask implements Runnable {

	private BuySellStocksService buySellStocksService;
	private int price;
	
	public ProductBulkExecutorTask(
		BuySellStocksService buySellStocksService,
		int price
	) {
		this.buySellStocksService = buySellStocksService;
		this.price = price;
	}
	
	@Override
	public void run() {
		
		Product prod = new Product(
			"INFY", 
			BuySellStocksUtil.genRandomProdQuantity(), 
			new BigDecimal(price), 
			BuySellStocksUtil.getRandomStockStatus(), 
			StockStatus.OPEN);
		
		buySellStocksService.createProductServ(prod);
		
	}//end of run
	
}//end of ProductBulkExecutorTask
