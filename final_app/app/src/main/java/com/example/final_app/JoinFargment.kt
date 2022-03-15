package com.example.final_app

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.final_app.databinding.FragmentJoinBinding


class JoinFargment : Fragment() {

    //바인딩 : 프래그먼트
    lateinit var joinFargmentBinding : FragmentJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 사용 : 크리에이트시 만들어짐
        joinFargmentBinding = FragmentJoinBinding.inflate(inflater)
        joinFargmentBinding.joinToolbar.title = "회원가입"
        joinFargmentBinding.joinNextBtn.setOnClickListener{
            val joinId = joinFargmentBinding.joinId.text.toString()
            val joinPw = joinFargmentBinding.joinPw.text.toString()

            if (joinId == null || joinId.length == 0){
                val dialogBuiler = AlertDialog.Builder(requireContext())
                dialogBuiler.setTitle("아이디 입력 오류")
                dialogBuiler.setMessage("아이디를 확인해주세요요")
               dialogBuiler.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    joinFargmentBinding.joinId.requestFocus()
               }
                dialogBuiler.show()
                return@setOnClickListener
            }
            if (joinPw == null || joinPw.length == 0) {
                val dialogBuiler = AlertDialog.Builder(requireContext())
                dialogBuiler.setTitle("비밀번호 입력 오류")
                dialogBuiler.setMessage("비밀번호를 확인해주세요요")
                dialogBuiler.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                    joinFargmentBinding.joinPw.requestFocus()
                }
                dialogBuiler.show()
                return@setOnClickListener
            }

                val act = activity as MainActivity

                act.userId = joinId
                act.userPw = joinPw


            act.fragmentController("nick_name",true,true)
        }
        return joinFargmentBinding.root  // 만들고 반환 (뷰 반환)


    }



}