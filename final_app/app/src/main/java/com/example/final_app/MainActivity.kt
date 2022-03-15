package com.example.final_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.final_app.databinding.ActivityMainBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityBinding : ActivityMainBinding
    lateinit var currentFragment: Fragment // fragment valiable

     // 사용자 정보
    var userId = ""
    var userPw = ""
    var userNickName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(1000)
        setTheme(R.style.Theme_Final_app)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        val pref = getSharedPreferences("login_data", Context.MODE_PRIVATE)//자동로그인
        val login_user_idx = pref.getInt("login_user_idx",-1)
        val login_auto_login = pref.getInt("login_auto_login",-1) // preferences에 저장된 값을 보고
        if (login_auto_login == 1){
            thread {
                val client = OkHttpClient()
                val builder1 = FormBody.Builder()
                builder1.add("login_user_idx","$login_user_idx")
                val formBody = builder1.build()
                val site = "http://${ServerInfo.SERVER_IP}:8080/android_server/check_auto_login.jsp"

                val request = Request.Builder().url(site).post(formBody).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful ==true){ //통신 성공하면
                    val result_text = response.body?.string()!!.trim()
                    val chk = Integer.parseInt(result_text)
                    if (chk == 1){
                        val boardMainIntent = Intent(this, BoardMainActivity::class.java)
                        startActivity(boardMainIntent)
                        finish()
                    } else{ // 통신 실패
                        fragmentController("login", false, false)
                    }
                }
            }
        } else{
            fragmentController("login", false, false)
        }

        fragmentController("login", false, false)
  }

    fun fragmentController(name: String, add:Boolean, animate:Boolean){
        when(name){
            "login"->{ //로그인으로 들어오면 로그인 프래그먼트 반환
                currentFragment = LoginFragment()
            }
            "join"->{// 조인으로 들어오면 join 프래그먼트를 반환함
                currentFragment = JoinFargment()
            }
            "nick_name"-> {
                currentFragment = NickNameFragment()
            }
        }

        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_container, currentFragment)

        if (add == true){
            trans.addToBackStack(name)
        }
        if (animate == true){
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit() // 트랜잭션을 실행함.

    }
}
