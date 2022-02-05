package com.wikibook.bigdata.smartcar.redis;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;



import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@SuppressWarnings("unused")
public class RedisClient extends Thread{
	public static FirebaseOptions option;
    public static Firestore db; 
    public final static String PATH = "./kjh-prj-firebase-adminsdk-4ufoa-e7df9f3dda.json";
    public final static String COLLECTION_NAME = "컬렉션";

	private String key;
	private Jedis jedis;


	@SuppressWarnings("resource")
	public RedisClient(String k) {

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisPool jPool = new JedisPool(jedisPoolConfig, "server02.hadoop.com", 6379);
		jedis = jPool.getResource();

		this.key = k;
		

	}

	@Override    
	public void run() {
        
		firebase fdb = new firebase();
		try {
			firebase.init();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fdb.makeDatabaseConn();
        fdb.select();		

		Set<String> overSpeedCarList = null;
		
		int cnt = 1;


		try {
			while(true) {

				overSpeedCarList = jedis.smembers(key);

				System.out.println("################################################");
				System.out.println("#####   Start of The OverSpeed SmartCar    #####");
				System.out.println("################################################");
				
				
				System.out.println("\n[ Try No." + cnt++ + "]");
				if(overSpeedCarList.size() > 0) {
					for (String list : overSpeedCarList) {
						System.out.println(list);
						fdb.insert(list);
						System.out.println("send to firebase");
					}
					System.out.println("");
					
					jedis.del(key);
				}else{
					System.out.println("\nEmpty Car List...\n");
				}

				System.out.println("################################################");
				System.out.println("######   End of The OverSpeed SmartCar    ######");
				System.out.println("################################################");
				System.out.println("\n\n");

				Thread.sleep(10 * 1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( jedis != null ) jedis.close();
		}
	}
}
