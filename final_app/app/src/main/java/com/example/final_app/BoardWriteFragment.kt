package com.example.final_app

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.hardware.input.InputManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.example.final_app.databinding.FragmentBoardReadBinding
import com.example.final_app.databinding.FragmentBoardWriteBinding
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread


class BoardWriteFragment : Fragment() {

    // 프래그먼트 바인딩
    lateinit var boardWriteFragmentBinding : FragmentBoardWriteBinding
    // 카메라 관련 기능
    lateinit var contentUri :Uri
    // end
    //이미지
    var uploadImage : Bitmap? = null
    val spinner_date = arrayOf("고객센터", "자유게시판")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val act = activity as BoardMainActivity
        boardWriteFragmentBinding = FragmentBoardWriteBinding.inflate(inflater) // 맨 위에서 바인딩할 변수를 만들고 여기서 바인딩을 했음.

        boardWriteFragmentBinding.boardWriteToolbar.title="게시글 작성"



        //메뉴 셋팅 / 클릭시 이동함
        boardWriteFragmentBinding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        boardWriteFragmentBinding.boardWriteToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.board_write_menu_camera ->{
                    val filePath = requireContext().getExternalFilesDir(null).toString()
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"
                    val file = File(picPath)
                    contentUri = FileProvider.getUriForFile(requireContext(), "com.example.final_app.camera.file_provider", file) // 파일 저장
                    if (contentUri != null){
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                        startActivityForResult(cameraIntent, 1) // 사진을 찍고 위의 경로에 저장이되고 끝남. 그걸 이미지로 보여줄 것. 아래에서
                    }

                    true
                }
                R.id.board_write_menu_gallery->{ // 카메라,갤러리  기능 구현
                    val albumeIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                    albumeIntent.type = "image/*"
                    val mimeType = arrayOf("image/*")

                    albumeIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                    startActivityForResult(albumeIntent, 2)

                    true
                }
                R.id.board_write_menu_upload ->{
                    val act = activity as BoardMainActivity
                    // 유효성 검사
                    val boardWriteSubject = boardWriteFragmentBinding.boardWriteSubject.text.toString()
                    val boardWriteText = boardWriteFragmentBinding.boardWriteText.text.toString()
                    val boardWriteType = act.boardIndexList[boardWriteFragmentBinding.boardWriteType.selectedItemPosition + 1]

                    val pref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
                    val boardWriterIdx = pref.getInt("login_user_idx", 0)
                    if ( boardWriteSubject == null || boardWriteSubject.length == 0){
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("제목 입력 오류")
                        dialogBuilder.setMessage("제목을 입력해주세요")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            boardWriteFragmentBinding.boardWriteSubject.requestFocus()
                        }
                        dialogBuilder.show()
                        return@setOnMenuItemClickListener true
                    }
                    if ( boardWriteText == null || boardWriteText.length == 0){
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("내용 입력 오류")
                        dialogBuilder.setMessage("내용을 입력해주세요")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            boardWriteFragmentBinding.boardWriteText.requestFocus()
                        }
                        dialogBuilder.show()
                        return@setOnMenuItemClickListener true
                    }
                    // end

                    // 유효성 검사 후 데이터베이스에 저장해야함. 즉 thread 가동
                    thread {
                        val client = OkHttpClient()
                        val site = "http://${ServerInfo.SERVER_IP}:8080/android_server/add_content.jsp"
                        val builder1 = MultipartBody.Builder() // 넘겨줄 데이터 formbody는 string만 가능함
                        //파일 데이터라고 명시해주는 과정
                        builder1.setType(MultipartBody.FORM)
                        // 여기만
                        builder1.addFormDataPart("content_board_idx", "$boardWriteType")
                        builder1.addFormDataPart("content_writer_idx", "$boardWriterIdx")
                        builder1.addFormDataPart("content_subject", boardWriteSubject)
                        builder1.addFormDataPart("content_text", boardWriteText)

                        // 파일 전송 기능 구현 , 만약 파일이 없으면 null값이 들어감
                        var file : File? = null
                        if(uploadImage != null){
                            val filePath = requireContext().getExternalFilesDir(null).toString()
                            val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                            val picPath = "$filePath/$fileName"
                            file = File(picPath)
                            val fos = FileOutputStream(file)
                            uploadImage?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            // 파일 정보들
                            builder1.addFormDataPart("content_image", file.name, file.asRequestBody(MultipartBody.FORM))
                        }

                        val formBody = builder1.build()
                        val request = Request.Builder().url(site).post(formBody).build()
                        val response = client.newCall(request).execute()

                        if(response.isSuccessful == true){

                            val resultText = response.body?.string()!!.trim()
                            act.readContentIdx = Integer.parseInt(resultText)
                            //Log.d("test", "${act.readContentIdx}")

                            activity?.runOnUiThread{ // 따로 작업하는 것이니까 또 thread를 띄워줌
                                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager// 키보드 내리기 위한 준비
                                inputMethodManager.hideSoftInputFromWindow(boardWriteFragmentBinding.boardWriteSubject.windowToken,0)
                                inputMethodManager.hideSoftInputFromWindow(boardWriteFragmentBinding.boardWriteText.windowToken, 0)

                                val dialogBuilder = AlertDialog.Builder(requireContext())
                                dialogBuilder.setTitle("작성 완료") // 팝업창 구현
                                dialogBuilder.setMessage("작성 완료하였습니다.")
                                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    act.fragmentRemoveBackStack("board_write")
                                    act.fragmentController("board_read", true, true)

                                }
                                dialogBuilder.show()
                            }
                        } else {
                            activity?.runOnUiThread {
                                val dialogBuilder = AlertDialog.Builder(requireContext())
                                dialogBuilder.setTitle("작성 오류")
                                dialogBuilder.setMessage("작성 오류가 발생하였습니다.")
                                dialogBuilder.setPositiveButton("확인", null)
                                dialogBuilder.show()
                            }
                        }
                    }
                    true
                }
                else -> false
            }

        }
        // 스피너 어댑터
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, act.boardNameList.drop(1))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)    // 어댑터 구성
        boardWriteFragmentBinding.boardWriteType.adapter = spinnerAdapter // 어댑터 적용

        if (act.selectedBoardType == 0){
            boardWriteFragmentBinding.boardWriteType.setSelection(0)
        } else {
            boardWriteFragmentBinding.boardWriteType.setSelection(act.selectedBoardType - 1)
        }
        return boardWriteFragmentBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 이미지 메뉴 선택시 옵션에 따라 달라짐
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){ // 갤러리
            1 -> {
                if(resultCode == Activity.RESULT_OK){
                    uploadImage = BitmapFactory.decodeFile(contentUri.path)
                    boardWriteFragmentBinding.boardWriteImage.setImageBitmap(uploadImage)
                    // 임시 파일 삭제
                    val file = File(contentUri.path)
                    file.delete()
                }

            }
            2 -> {
                if(resultCode == Activity.RESULT_OK){ // 카메라 사용

                    // 선택한 이미지 접근
                    val uri = data?.data
                    if (uri != null){
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            val source = ImageDecoder.createSource(activity?.contentResolver!!, uri)
                            uploadImage = ImageDecoder.decodeBitmap(source)
                            boardWriteFragmentBinding.boardWriteImage.setImageBitmap(uploadImage)
                        } else{
                            val cursor = activity?.contentResolver?.query(uri, null,null,null,null)
                            if (cursor != null){
                                cursor.moveToNext()
                                // 이미지 경로 가지고옴

                                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(index)
                                uploadImage = BitmapFactory.decodeFile(source)
                                boardWriteFragmentBinding.boardWriteImage.setImageBitmap(uploadImage)

                            }
                        }
                    }
                }
            }
        }
    }



}