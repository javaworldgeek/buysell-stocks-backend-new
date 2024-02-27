package com.javaworldgeek.buysellstocksbackendnew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

	@Bean
	public MongoClient mongoClient() {
		
		ConnectionString connectionString = 
				new ConnectionString("mongodb://localhost:27017");
		
		MongoClientSettings clientSettings = 
				MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.build();
		
		return MongoClients.create(clientSettings);
		
	}//end of mongoClient
	
}
