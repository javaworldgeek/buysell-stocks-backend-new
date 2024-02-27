package com.javaworldgeek.buysellstocksbackendnew.controller;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaworldgeek.buysellstocksbackendnew.thread.ProductBulkExecutorTask;
import com.javaworldgeek.buysellstocksbackendnew.entity.Product;
import com.javaworldgeek.buysellstocksbackendnew.service.BuySellStocksService;
import com.javaworldgeek.buysellstocksbackendnew.util.BuySellStocksUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/buysellnew")
public class BuySellStocksController {

	private static final Logger LOGGER = 
			LoggerFactory.getLogger(BuySellStocksController.class);
	
	private final BuySellStocksService buySellStockServ;

	private final int NUM_THREADS = 10;
	
	@CrossOrigin
	@GetMapping
	public List<Product> findAll(){
		LOGGER.info("Inside findAll() method - new");
		return buySellStockServ.findAllServ();
	}//end of findAll

	@CrossOrigin
	@PostMapping
	public void addProduct(@RequestBody Product product) {
		LOGGER.info("Product add: {}", product);
		 buySellStockServ.createProductServ(product);
	}//end of addProduct
	
	@CrossOrigin
	@PostMapping("/execute")
	public void doBulkExecution() {
		LOGGER.info("Inside bulk execution");
		
		ExecutorService executor = 
				Executors.newFixedThreadPool(NUM_THREADS);
		
		int price = BuySellStocksUtil.genRandomProdPrice();

		for(int exe=0; exe<NUM_THREADS; exe++) {
			executor.execute(
					new ProductBulkExecutorTask(buySellStockServ, price));
		}//end of for
		
		executor.shutdown();
	}//end of doBulkExecution
	
	@CrossOrigin
	@DeleteMapping("/deleteAll")
	public void deleteAll() {
		LOGGER.info("Product delete all using new buy sell end point");
		 buySellStockServ.deleteAllServ();
	}//end of deleteAll
	
	@CrossOrigin
	@PutMapping("/doBuySellStocks")
	public void doBuySellStocks() {
		LOGGER.info("Inside doBuySellStocks new method");
		buySellStockServ.doBuySellServ();
	}//end of doBuySellStocks
	
}//end of BuySellStocksController
