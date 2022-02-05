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

@SuppressWarnings("unused")
public class firebase {
	public static FirebaseOptions option;
    public static Firestore db; 
    public final static String PATH = "./kjh-prj-firebase-adminsdk-4ufoa-e7df9f3dda.json";
    public final static String COLLECTION_NAME = "컬렉션";
    
    
    
    final static void init() throws Exception{
        FileInputStream refreshToken = new FileInputStream(PATH);
        option = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl("https://kjh-prj-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(option);
    }
    
    
    public void makeDatabaseConn(){  
        db = FirestoreClient.getFirestore();
    }    
    
    public void select(){ //조회
        db.collection(COLLECTION_NAME).addSnapshotListener( (target, exception)->{
        });
    }    
   
    public void insert(String a){  //등록
        Map<Object, Object> item = new HashMap<Object, Object>();
        item.put("과속 차량", a);
        db.collection(COLLECTION_NAME).add(item);
    }
}
