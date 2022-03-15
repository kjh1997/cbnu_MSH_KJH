package com.example.final_app

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.final_app.databinding.FragmentNickNameBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class NickNameFragment : Fragment() {

    // nickfragment 바인딩
    lateinit var nickNameFragmentBinding : FragmentNickNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        nickNameFragmentBinding = FragmentNickNameBinding.inflate(inflater)
        nickNameFragmentBinding.nicknameToolbar.title = "닉네임 입력"

        nickNameFragmentBinding.nicknameJoinBtn.setOnClickListener{

            val nickNameNickname = nickNameFragmentBinding.nicknameNickname.text.toString()
            if (nickNameNickname == null || nickNameNickname.length == 0){
                // 팝업창
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("닉네임 입력 오류")
                dialogBuilder.setMessage("닉네임을 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    nickNameFragmentBinding.nicknameNickname.requestFocus()
                }
                dialogBuilder.show()
                return@setOnClickListener
            }
            val act = activity as MainActivity
            act.userNickName = nickNameNickname
            Log.d("test","${act.userId}")
            Log.d("test","${act.userPw}")
            Log.d("test","${act.userId}")
            // 백그라운드로 따로 실행하기 위하여 thread를 사용함, 네트워크 작업은 항상 분리시켜 작업해야함.
            thread {
                val client = OkHttpClient()
                val site  ="http://${ServerInfo.SERVER_IP}:8080/android_server/join_user.jsp"
                val builder1 = FormBody.Builder()
                builder1.add("user_id",act.userId)
                builder1.add("user_pw",act.userPw)
                builder1.add("user_nick_name",act.userNickName)
                val formBody = builder1.build()

                val request = Request.Builder().url(site).post(formBody).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful == true){
                    activity?.runOnUiThread{
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("가입 완료")
                        dialogBuilder.setMessage("가입이 완료되었습니다.")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            val mainIntent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(mainIntent)
                            activity?.finish()
                        }
                        dialogBuilder.show()
                   }
                }else{
                    activity?.runOnUiThread{
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("가입오류")
                        dialogBuilder.setMessage("가입 오류 발생함")
                        dialogBuilder.setPositiveButton("확인",null)
                        dialogBuilder.show()
                    }
                }
            }

        }
        return nickNameFragmentBinding.root

    }


}