package com.cbnu.android.server.appserver.content;


import com.cbnu.android.server.appserver.account.Account;
import com.cbnu.android.server.appserver.account.AccountService;
import com.cbnu.android.server.appserver.account.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private final UserRepository userRepository;
    private final ServletContext ctx;

    private final UserRepository userRepositroy;


    @PostMapping("CommunityServer/get_content_list.jsp")
    public List<JSONObject> getContentList(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        String page_num = request.getParameter("page_num");
        String content_board_idx = request.getParameter("content_board_idx");

        List<Content> contentList = contentService.getContentList(page_num,content_board_idx);
        List<JSONObject> appData = new ArrayList<>();
        for (Content content : contentList) {
            JSONObject obj = new JSONObject();
            obj.put("content_idx",content.getId());
            obj.put("content_nick_name",content.getWriteridx().getNickname());
            obj.put("content_write_date",content.getWriteDate());
            obj.put("content_subject",content.getSubject());
            appData.add(obj);

        }

        System.out.println("size!!! !   "+appData.size());



        return appData;
    }

    @PostMapping("CommunityServer/get_content.jsp")
    public JSONObject getContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String content_idx = request.getParameter("read_content_idx");
        Content a = contentService.getContent(content_idx);
        System.out.println("id : " + a.getWriteDate() + " | | " + content_idx);
        System.out.println("write idx " + a.getWriteridx().getId());

        Account account = a.getWriteridx();
        System.out.println(account.getId()+" || " + account.getNickname());
        System.out.println(content_idx);

        JSONObject obj = new JSONObject();
        obj.put("content_subject", a.getSubject());
        obj.put("content_nick_name", account.getNickname());
        obj.put("content_write_date", a.getWriteDate().toString());
        obj.put("content_text", a.getText());
        obj.put("content_image", a.getImage());
        obj.put("content_writer_idx", account.getId());
        obj.put("content_board_idx", a.getBoardidx());
        return obj;
    }

    @PostMapping("CommunityServer/modify_content.jsp")
    @Transactional
    public String modifyContent(MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Content content1 = contentService.getContent(request.getParameter("content_board_idx"));
        System.out.println(content1.getImage());
        if (file != null) {
            long size = file.getSize();
            String fileName = file.getOriginalFilename();
            System.out.printf("fileName:%s, fileSize: %d\n", fileName, size);

            //ServletContext cts = request.getServletContext();
            String webPath = "/CommunityServer/upload";
            String realPath = ctx.getRealPath(webPath);
            System.out.printf("realPath : %s\n", realPath);

            File savePath = new File(realPath); //realPath경로에 파일업로드하기위한 폴더가 있는지 없는지 확인
            if (!savePath.exists())
                savePath.mkdirs();//사이에 있는 경로에 폴더가 없으면 폴더를 만들어줌

            realPath += File.separator + fileName;  // "//" 시스템에 맞는 구분자 출력됨
            File saveFile = new File(realPath);
            file.transferTo(saveFile); //저장시키기
            System.out.println(fileName);
            content1.setImage(fileName);
        } else {
            content1.setImage(null);
        }


        content1.setBoardidx(Integer.parseInt(request.getParameter("content_board_idx")));

        content1.setSubject(request.getParameter("content_subject"));
        content1.setText(request.getParameter("content_text"));
        content1.setWriteridx(userRepositroy.findById(Integer.parseInt(request.getParameter("content_writer_idx"))));

        String successSave = contentService.save(content1);
        if (successSave.equals("fail")) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return "fail";
        }
        return "success";
    }

    @GetMapping("CommunityServer/upload/{content_image}")
    public ResponseEntity<byte[]> getImage(@PathVariable String content_image) throws IOException {

        System.out.println(content_image);
        String webPath = "/CommunityServer/upload/";
        String realPath = ctx.getRealPath(webPath);
        InputStream imageStream = new FileInputStream(realPath + content_image);
//		InputStream imageStream = new FileInputStream("/home/ubuntu/images/feed/" + imagename);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }

    @PostMapping("/CommunityServer/delete_content.jsp")
    public String deleteContent(HttpServletRequest request, HttpServletResponse response) {
        contentService.deleteContent(request.getParameter("content_idx"));
        return "success";
    }


    @PostMapping("/CommunityServer/add_content.jsp")
    public Integer saveContent(MultipartFile file,
                      HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException {
        Content content1 = new Content();
        if (file != null) {
            long size = file.getSize();
            String fileName = file.getOriginalFilename();
            System.out.printf("fileName:%s, fileSize: %d\n", fileName, size);

            //ServletContext cts = request.getServletContext();
            String webPath = "/CommunityServer/upload";
            String realPath = ctx.getRealPath(webPath);
            System.out.printf("realPath : %s\n", realPath);

            File savePath = new File(realPath); //realPath경로에 파일업로드하기위한 폴더가 있는지 없는지 확인
            if (!savePath.exists())
                savePath.mkdirs();//사이에 있는 경로에 폴더가 없으면 폴더를 만들어줌

            realPath += File.separator + fileName;  // "//" 시스템에 맞는 구분자 출력됨
            File saveFile = new File(realPath);
            file.transferTo(saveFile); //저장시키기
            content1.setImage(fileName);
        } else {
            content1.setImage(null);
        }


        content1.setBoardidx(Integer.parseInt(request.getParameter("content_board_idx")));

        content1.setSubject(request.getParameter("content_subject"));
        content1.setText(request.getParameter("content_text"));
        System.out.println("???"+request.getParameter("content_writer_idx"));
        Account account = userRepositroy.findById(Integer.parseInt(request.getParameter("content_writer_idx")));
        System.out.println(account.getNickname() + " || account " + account.getId());
        content1.setWriteridx(account);

        String successSave = contentService.save(content1);
        if (successSave.equals("fail")) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return null;
        }
        Integer id = (Integer) content1.getId();
        return id;
    }

}

@Data
@Builder
@AllArgsConstructor
class ContentDTO {
    private String content_subject;
    private String content_nick_name;
    private String content_write_date;
    private String content_text;
    private String content_image;
    private int content_writer_idx;
    private int content_board_idx;

}
