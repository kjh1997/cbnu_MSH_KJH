package com.wikibook.bigdata.smartcar.redis;

import java.util.Set;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisClient extends Thread{

	private String key;
	private Jedis jedis;
	private FirebaseOptions option;
    private Firestore db; 
    private final static String PATH = "./kjh-prj-firebase-adminsdk-4ufoa-11ff2301b1.json";
    private final static String COLLECTION_NAME = "컬렉션";

	public RedisClient(String k) {

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisPool jPool = new JedisPool(jedisPoolConfig, "server02.hadoop.com", 6379);
		jedis = jPool.getResource();

		this.key = k;
		

	}
	

	@Override    
	public void run() {
		init();
		makeDatabaseConn();
		
			
		Set<String> overSpeedCarList = null;
		
		int cnt = 1;


		try {
			while(true) {

				overSpeedCarList = jedis.smembers(key);

				System.out.println("################################################");
				System.out.println("#####   Start of The OverSpeed Car    #####");
				System.out.println("################################################");
				
				
				System.out.println("\n[ Try No." + cnt++ + "]");

				if(overSpeedCarList.size() > 0) {
					for (String list : overSpeedCarList) {
						System.out.println(list);
						insert(list);
					}
					System.out.println("");
					
					jedis.del(key);
				}else{
					System.out.println("\nEmpty Car List...\n");
				}

				System.out.println("################################################");
				System.out.println("######   End of The OverSpeed Car    ######");
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
	static void init() throws Exception{
        FileInputStream refreshToken = new FileInputStream(PATH);
        option = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl("https://kjh-prj-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(option);
    }
    
    static void makeDatabaseConn(){  //Firestore 인스턴스 생성
        db = FirestoreClient.getFirestore();
        System.out.println("성공");
    }      
   
    static void insert(String data){  //등록
        Map<Object, Object> item = new HashMap<Object, Object>();
        item.put("name", data);
        db.collection(COLLECTION_NAME).add(item);
    }  
}
