package com.javaworldgeek.buysellstocksbackendnew.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaworldgeek.buysellstocksbackendnew.entity.Product;
import com.javaworldgeek.buysellstocksbackendnew.entity.StockStatus;
import com.javaworldgeek.buysellstocksbackendnew.entity.TransactionType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BuySellStocksDB {
	
	@Autowired
	private final MongoClient mongoClient;
	
	public List<Product> findAllDB(){
		
		List<Product> products = new ArrayList<>();
		Product product;
		
		MongoDatabase mongoDatabase = 
				mongoClient.getDatabase("mongodb");
		
		MongoCollection<Document> collection = 
				mongoDatabase.getCollection("product");
		
		try (MongoCursor<Document> cursor = collection.find().iterator()){
			
			while(cursor.hasNext()) {
				Document prodDoc = cursor.next();
				product = new Product();
				
				product.setId(prodDoc.getObjectId("_id").toString());
				product.setName(prodDoc.getString("name"));
				product.setQuantity(prodDoc.getInteger("quantity"));
				product.setPrice(new BigDecimal(prodDoc.get("price", Number.class).longValue()));
				product.setTransType(TransactionType.valueOf(prodDoc.getString("transType")));
				product.setStockSts(StockStatus.valueOf(prodDoc.getString("stockSts")));
				
				products.add(product);
			}//end of while
			
		}//end of try block
		
		return products;
	}//end of findAllDB
	
	public void createProductDB(Product product) {
		MongoDatabase mongoDatabase = 
				mongoClient.getDatabase("mongodb");
		
		MongoCollection<Document> collection = 
				mongoDatabase.getCollection("product");
		
		Document prodDocument = new Document();
		
		prodDocument.append("name", product.getName());
		prodDocument.append("quantity", product.getQuantity());
		prodDocument.append("price", product.getPrice());
		prodDocument.append("transType", product.getTransType());
		prodDocument.append("stockSts", product.getStockSts());
		
		collection.insertOne(prodDocument);

	}//end of createProductDB
	
	public void updateProductDB(Product prod) {
		
		MongoDatabase mongoDatabase = 
				mongoClient.getDatabase("mongodb");
		
		MongoCollection<Document> collection = 
				mongoDatabase.getCollection("product");
		
		//filtering condition
		Document filter = new Document("_id",new ObjectId(prod.getId()));

		Document prodDocument = new Document();
		prodDocument.append("name", prod.getName());
		prodDocument.append("quantity", prod.getQuantity());
		prodDocument.append("price", prod.getPrice());
		prodDocument.append("transType", prod.getTransType());
		prodDocument.append("stockSts", prod.getStockSts());
		
		collection.updateOne(filter, new Document("$set", prodDocument));
		
	}//end of updateProductDB
	
	public void deleteAllDB() {
		
		MongoDatabase mongoDatabase = 
				mongoClient.getDatabase("mongodb");
		
		MongoCollection<Document> collection = 
				mongoDatabase.getCollection("product");
		
		collection.deleteMany(new Document());
		
	}//end of deleteAllDB
	
}
