package com.example.final_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.final_app.databinding.ActivityBoardMainBinding
import com.example.final_app.databinding.ActivityMainBinding
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.jar.Manifest
import kotlin.concurrent.thread

class BoardMainActivity : AppCompatActivity() {

    // 위치한 액티비티 바인딩
    lateinit var boardMainActivityBinding : ActivityBoardMainBinding

    // 프래그먼트 바인딩 여기는 엑티비티라 프래그먼트를 바인딩함
    lateinit var currentFragment : Fragment

    // 게시판 가져오기
    val boardNameList = ArrayList<String>()
    val boardIndexList = ArrayList<Int>()
    var selectedBoardType = 0  // 보드의 인덱스번호 초기는 전체 게시판

    // 현재 볼 글 번호
    var readContentIdx = 0

    // 페이지 번호
    var nowPage = 1

    //권한 확인
    val permissionList = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_MEDIA_LOCATION
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boardMainActivityBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(boardMainActivityBinding.root)

        //권환 확인
        requestPermissions(permissionList, 0)
        boardNameList.add("전체 게시판")
        boardIndexList.add(0)
        // 게시물 가지고옴
        thread {
            val client = OkHttpClient()
            val site = "http://${ServerInfo.SERVER_IP}:8080/android_server/get_board_list.jsp"
            val request = Request.Builder().url(site).get().build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful == true){
                val resultText = response.body?.string()!!.trim()
                val root = JSONArray(resultText)

                for( i in 0 until root.length()){
                    val obj = root.getJSONObject(i)
                    val boardIdx = obj.getInt("board_idx")
                    val boardName = obj.getString("board_name")
                    boardIndexList.add(boardIdx)
                    boardNameList.add(boardName)
                }
            }
        }

        fragmentController("board_main", false, false)
    }

    fun fragmentController(name:String, add:Boolean, animate:Boolean){
        when(name){
            "board_main" -> {
                currentFragment = BoardMainFragment()
            }
            "board_read" -> {
                currentFragment = BoardReadFragment()
            }
            "board_write" -> {
                currentFragment = BoardWriteFragment()
            }
            "board_modify" ->{
                currentFragment = BoardModifyFragment()
            }
        }

        // 트랜잭션을 실행하기위한 설정들
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.board_main_container, currentFragment)

        if(add == true){
            trans.addToBackStack(name)  // 뒤로가기 구현을 위한 백스택택
       }
        if (animate == true){
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) // 이걸 넣는 이유는 이름을 지정해서 백스택에서 프래그먼트를 안전하게 제거할 수 있다고 함
        }
        trans.commit()

    }
    fun fragmentRemoveBackStack(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE) // 백스택에서 뺌. 이름을 기준으로. 지금 바로 빼라고 하는 두번째 인자값
    }
}