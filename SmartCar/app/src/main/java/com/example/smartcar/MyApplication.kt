package com.example.smartcar

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


//앱 전역에서 이용할 객체
class MyApplication: MultiDexApplication() {
    companion object {
        //파이어베이스 인증 객체
        lateinit var auth: FirebaseAuth
        //인증된 사용자의 이메일 정보
        var email: String? = null
        //인증 상태를 파악하는 함수
        fun checkAuth(): Boolean {
            val currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                if(currentUser.isEmailVerified){
                    true
                } else{
                    false
                }
            } ?: let {
                false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}