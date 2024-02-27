package com.javaworldgeek.buysellstocksbackendnew.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaworldgeek.buysellstocksbackendnew.entity.Product;
import com.javaworldgeek.buysellstocksbackendnew.entity.StockStatus;
import com.javaworldgeek.buysellstocksbackendnew.entity.TransactionType;
import com.javaworldgeek.buysellstocksbackendnew.repository.BuySellStocksDB;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuySellStocksService {
	
	private final BuySellStocksDB buySellStocksDB;

	public List<Product> findAllServ(){
		return buySellStocksDB.findAllDB()
			   .stream()
			   .sorted(Comparator
               		.comparing((Product p) -> 
               		p.getStockSts().equals(StockStatus.OPEN) ? 0 : 
               		p.getStockSts().equals(StockStatus.PARTIAL) ? 1 : 2)
               		.thenComparing(Product::getQuantity, Comparator.reverseOrder()))
               .collect(Collectors.toList());
	}//end of findAllServ
	
	public void createProductServ(Product prod) {
		buySellStocksDB.createProductDB(prod);
	}//end of createProductServ
	
	public void deleteAllServ() {
		buySellStocksDB.deleteAllDB();
	}//end of deleteAllServ
	
	public void doBuySellServ() {
		
		List<Product> products = findAllServ();
		
		List<Product> buyProducts = 
				products.stream()
				.filter(p -> p.getTransType().equals(TransactionType.BUY))
				.filter(p -> !p.getStockSts().equals(StockStatus.CLOSED))
				.collect(Collectors.toList());
		
		List<Product> sellProducts = 
				products.stream()
				.filter(p -> p.getTransType().equals(TransactionType.SELL))
				.filter(p -> !p.getStockSts().equals(StockStatus.CLOSED))
				.collect(Collectors.toList());
		
		for(Product buy : buyProducts) {
			int buyQuan = buy.getQuantity();
			
			for(Product sell : sellProducts) {
				
				if(buy.getPrice().equals(sell.getPrice()) && 
				   buy.getName().equals(sell.getName())) {
					
					if(!buy.getStockSts().equals(StockStatus.CLOSED) && 
					   !sell.getStockSts().equals(StockStatus.CLOSED)){
						
						int sellQuan = sell.getQuantity();
						
						if(buyQuan < sellQuan) {
							
							sellQuan -= buyQuan;
							buy.setStockSts(StockStatus.CLOSED);
							sell.setQuantity(sellQuan);
							sell.setStockSts(StockStatus.PARTIAL);
							break;
							
						}else if(buyQuan > sellQuan) {
							
							buyQuan -= sellQuan;
							buy.setStockSts(StockStatus.PARTIAL);
							sell.setQuantity(0);
							sell.setStockSts(StockStatus.CLOSED);
							
						}else if(buyQuan == sellQuan) {
							
							buy.setStockSts(StockStatus.CLOSED);
							sell.setQuantity(0);
							sell.setStockSts(StockStatus.CLOSED);
							break;
						}//end of if
						
					}//end of if
					
				}//end of if
				
			}//end of for
			
		}//end of for
		
		
		buyProducts.forEach(buySellStocksDB::updateProductDB);
		sellProducts.forEach(buySellStocksDB::updateProductDB);
	}//end of doBuySellServ
	
}//end of BuySellStocksService