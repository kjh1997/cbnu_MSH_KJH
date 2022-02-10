package com.example.smartcar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.smartcar.databinding.ActivityLoginBinding
import com.example.smartcar.databinding.ActivityMainBinding
import com.example.smartcar.databinding.ActivitySignUpBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar : ActionBar? = supportActionBar
        actionBar?.title = "이메일 로그인"

        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email: String = binding.editEmail.text.toString()
            val password: String = binding.editPassword.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    binding.editEmail.text.clear()
                    binding.editPassword.text.clear()
                    if (task.isSuccessful) {
                        if (MyApplication.checkAuth()){
                            // 로그인 성공
                            MyApplication.email = email
                        } else {
                            // 발송된 메일로 인증 확인을 안 한 경우
                            Toast.makeText(baseContext,"전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}