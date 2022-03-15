package com.example.final_app

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_app.databinding.BoardMainRecyclerItemBinding
import com.example.final_app.databinding.FragmentBoardMainBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.sql.Driver
import kotlin.concurrent.thread

class BoardMainFragment : Fragment() {

    lateinit var boardMainFragmentBinding : FragmentBoardMainBinding

    val contentIdxList = ArrayList<Int>()
    val contentWriterList = ArrayList<String>()
    val contentWriteDateList = ArrayList<String>()
    val contentSubjectList = ArrayList<String>()

    val boardListData = arrayOf(
        "전체글", "게시판1", "게시판2", "게시판3", "게시판4"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val act = activity as BoardMainActivity

        boardMainFragmentBinding = FragmentBoardMainBinding.inflate(inflater)
        boardMainFragmentBinding.boardMainToolbar.title = act.boardNameList[act.selectedBoardType]

        boardMainFragmentBinding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
        boardMainFragmentBinding.boardMainToolbar.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.board_main_menu_board_list -> {

                    val act = activity as BoardMainActivity

                    val boardListBuilder = AlertDialog.Builder(requireContext())
                    boardListBuilder.setTitle("게시판 목록")
                    boardListBuilder.setNegativeButton("취소", null)
                    boardListBuilder.setItems(act.boardNameList.toTypedArray()){ dialogInterface: DialogInterface, i: Int ->
                        act.selectedBoardType = i

                        getContentList(true)

                        boardMainFragmentBinding.boardMainToolbar.title = act.boardNameList[act.selectedBoardType]
                    }
                    boardListBuilder.show()
                    true
                }
                R.id.board_main_menu_write -> {
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_write", true, true)
                    true
                }
                else -> false
            }
        }


        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        boardMainFragmentBinding.boardMainRecycler.adapter = boardMainRecyclerAdapter

        boardMainFragmentBinding.boardMainRecycler.layoutManager = LinearLayoutManager(requireContext())
        boardMainFragmentBinding.boardMainRecycler.addItemDecoration(DividerItemDecoration(requireContext(), 1))
        boardMainFragmentBinding.boardMainRecycler.addOnScrollListener(object  : RecyclerView.OnScrollListener(){ // 스크롤 발생 시 발생
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 현재 화면에 보이는 항목 중 제일 마지막 항목의 인덱스를 가지고 옴
                val index1 = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                // recycler view가 관리하는 항목의 총 개수
                val count1 = recyclerView.adapter?.itemCount

                if(index1 +1 == count1){
                    act.nowPage = act.nowPage + 1
                    getContentList( false) // 화면 내려도 안없어지게 함
                }
            }
        })

        getContentList(true)
        // 새로고침
        boardMainFragmentBinding.boardMainSwipe.setOnRefreshListener{
            getContentList(true)
            boardMainFragmentBinding.boardMainSwipe.isRefreshing = false // 돌아가는거 없애줌
        }

        return boardMainFragmentBinding.root
    }

    inner class BoardMainRecyclerAdapter : RecyclerView.Adapter<BoardMainRecyclerAdapter.ViewHolderClass>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

            val boardMainRecyclerItemBinding = BoardMainRecyclerItemBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(boardMainRecyclerItemBinding)

            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            boardMainRecyclerItemBinding.root.layoutParams = layoutParams
            boardMainRecyclerItemBinding.root.setOnClickListener(holder)

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.boardMainItemNickname.text = contentWriterList[position]
            holder.boardMainItemWriteDate.text = contentWriteDateList[position]
            holder.boardMainItemSubject.text = contentSubjectList[position]
        }

        override fun getItemCount(): Int {
            return contentIdxList.size
        }

        inner class ViewHolderClass(boardMainRecyclerItemBinding:BoardMainRecyclerItemBinding)
            : RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener{

            val boardMainItemNickname = boardMainRecyclerItemBinding.boardMainItemNickname
            val boardMainItemSubject = boardMainRecyclerItemBinding.boardMainItemSubject
            val boardMainItemWriteDate = boardMainRecyclerItemBinding.boardMainItemWriteDate

            override fun onClick(v: View?) {
                val act = activity as BoardMainActivity

                act.readContentIdx = contentIdxList[adapterPosition]

                act.fragmentController("board_read", true, true)
            }
        }
    }

    fun getContentList(clear:Boolean) {

        if(clear == true){
            contentIdxList.clear()
            contentWriterList.clear()
            contentWriteDateList.clear()
            contentSubjectList.clear()

            val act = activity as BoardMainActivity
            act.nowPage = 1
        }

        thread {
            val client = OkHttpClient()

            val site = "http://${ServerInfo.SERVER_IP}:8080/android_server/get_content_list.jsp"

            val act = activity as BoardMainActivity

            val builder1 = FormBody.Builder()
            builder1.add("content_board_idx", "${act.selectedBoardType}")
            builder1.add("page_num", "${act.nowPage}") // 페이지 번호 삽입
            val formBody = builder1.build()

            val request = Request.Builder().url(site).post(formBody).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful == true){
                val resultText = response.body?.string()!!.trim()
                val root = JSONArray(resultText)

                for(i in 0 until root.length()){
                    val obj = root.getJSONObject(i)

                    contentIdxList.add(obj.getInt("content_idx"))
                    contentWriterList.add(obj.getString("content_nick_name"))
                    contentWriteDateList.add(obj.getString("content_write_date"))
                    contentSubjectList.add(obj.getString("content_subject"))
                }

                // 가지고 오는 것이 하나도 없으면 페이지를 하나 빼줌 // 무한 스크롤 기능
                if(root.length() == 0){
                    act.nowPage = act.nowPage - 1
                }

                activity?.runOnUiThread {
                    boardMainFragmentBinding.boardMainRecycler.adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}






