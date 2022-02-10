package com.example.smartcar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.smartcar.databinding.ActivityMainBinding
import com.example.smartcar.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar : ActionBar? = supportActionBar
        actionBar?.title = "회원가입"

        binding.signupBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................
            val email: String = binding.editEmailSignUp.text.toString()
            val password: String = binding.editPasswordSignUp.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    binding.editEmailSignUp.text.clear()
                    binding.editPasswordSignUp.text.clear()
                    if (task.isSuccessful) {
                        //비밀번호 최소 6자 이상
                        // 메일 보내기
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener{ sendTask ->
                                if (sendTask.isSuccessful) {
                                    Toast.makeText(baseContext,
                                        "회원가입에 성공하였습니다. 전송된 메일을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(baseContext, "메일 전송 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }
    }
