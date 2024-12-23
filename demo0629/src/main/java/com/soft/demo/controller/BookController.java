package com.soft.demo.controller;


import com.soft.demo.beans.Book;
import com.soft.demo.beans.JsonResult;
import com.soft.demo.beans.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

@Api(tags = "书籍接口")
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/book")
public class BookController {

    private static final String UPLOAD_DIR = "E:\\test\\books\\";
    private static final String UPLOADIMAGE_DIR = "E:\\test\\image\\";

    @Autowired//这个不能省略，否则不能使⽤，java需要⼀个创建对象的过程，在Spring中就靠它了。
    private JdbcTemplate jdbc;//建议成员变量都放在成员⽅法的上边

    @ApiOperation(value = "插入书籍",notes = "插入书籍")
    @PostMapping("/insert")
    public JsonResult<Book> insert(@RequestBody @ApiParam(name = "书籍",value = "全部信息都要，发json格式",required = true)Book book) throws UnknownHostException {
        String avatar;
        if(book.getBookimage()==""||book.getBookimage()==null||book.getBookimage()=="string"){
            int i = (int) (Math.random()*3);

            avatar = "http://"+ InetAddress.getLocalHost().getHostAddress()+":8016/test/books/默认书籍"+i+".png";
        }
        else{
            avatar=book.getBookimage();
        }
        //2.操纵数据库
        log.warn("hhh");
        jdbc.update("insert into book(userid, bookname, bookimage) values (?,?,?);",
                book.getUserid(), book.getBookname(), avatar);
        int id = jdbc.queryForObject("select id from book where id = (select max(id) from book);",int.class);
        book.setId(id);
        return new JsonResult<>(book);
    }

    @ApiOperation(value = "上传书籍图片",notes = "上传书籍图片")
    @PostMapping("/upload")
    public JsonResult<ResponseEntity<String>>  uploadFile(@RequestParam("bookId") Long bookId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new JsonResult<>(ResponseEntity.badRequest().body("文件为空"));
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = originalFilename.toString();
            //String fileName = System.currentTimeMillis() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            Path path = Paths.get(UPLOAD_DIR + fileName);
            String localHost = "http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/books/"+fileName;

            // 确保目录存在
            Files.createDirectories(path.getParent());
            // 将文件写入到目标位置
            Files.write(path, file.getBytes());

            // 保存文件信息到内存（模拟数据库）

            //userFiles.computeIfAbsent(userId, k -> new ArrayList<>()).add(fileName);
            jdbc.update("update book set bookimage = ? where id = ?;",localHost,bookId);
            return new JsonResult<>(ResponseEntity.ok("文件上传成功: " + localHost));
        } catch (IOException e) {
            return new JsonResult<>(ResponseEntity.status(500).body("文件上传失败: " + e.getMessage()));
        }
    }

    @ApiOperation(value = "删除书籍",notes = "删除书籍")
    @GetMapping("/delete")
    public JsonResult<String> delete(int id) throws UnknownHostException {

        Integer integer = jdbc.queryForObject("select count(id) from book where id = ?;", int.class, id);
        if(integer==0){
            return new JsonResult<>(201,"删除失败，商品不存在");
        }
        jdbc.update("delete from book where id = ?;",id);
        return new JsonResult<>("删除成功");
    }

    @ApiOperation(value = "获取所有书籍",notes = "获取所有书籍")
    @GetMapping("/list")
    public JsonResult<List<Book> > list() throws UnknownHostException {
        List<Book> books=jdbc.query("select * from book;",new BeanPropertyRowMapper<>(Book.class));
        return new JsonResult<>(books);
    }

}
