package com.soft.demo.controller;


import com.soft.demo.beans.Image;
import com.soft.demo.beans.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.soft.demo.beans.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;


@Api(tags = "上传下载文件图片接口")
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/file")
public class FileUploadController {



    @Autowired//这个不能省略，否则不能使⽤，java需要⼀个创建对象的过程，在Spring中就靠它了。
    private JdbcTemplate jdbc;//建议成员变量都放在成员⽅法的上边

    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final String UPLOAD_DIR = "E:\\test\\avatar\\";
    private static final String UPLOADIMAGE_DIR = "E:\\test\\image\\";
    private Map<Long, List<String>> userFiles = new HashMap<>();

    @ApiOperation(value = "上传用户头像文件",notes = "上传用户头像文件")
    @PostMapping("/upload")
    public JsonResult<ResponseEntity<String>>  uploadFile(@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new JsonResult<>(ResponseEntity.badRequest().body("文件为空"));
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = originalFilename.toString();
            //String fileName = System.currentTimeMillis() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            Path path = Paths.get(UPLOAD_DIR + fileName);
            String localHost = "http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/"+fileName;
            // 确保目录存在
            Files.createDirectories(path.getParent());

            // 将文件写入到目标位置
            Files.write(path, file.getBytes());

            // 保存文件信息到内存（模拟数据库）
            jdbc.update("update user set avatar_name = ? where id = ?;",localHost, userId);
            //userFiles.computeIfAbsent(userId, k -> new ArrayList<>()).add(fileName);

            return new JsonResult<>(ResponseEntity.ok("文件上传成功: " + localHost));
        } catch (IOException e) {
            return new JsonResult<>(ResponseEntity.status(500).body("文件上传失败: " + e.getMessage()));
        }
    }

/*    @ApiModelProperty(value = "查看当前用户有多少头像")
    @GetMapping("/list")
    public ResponseEntity<List<String>> listUserFiles(@RequestParam("userId") Long userId) {
        List<String> files = userFiles.getOrDefault(userId, new ArrayList<>());
        return ResponseEntity.ok(files);
    }*/

/*    @ApiOperation(value = "下载用户头像文件", notes = "下载用户头像文件")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("userId") Long userId) {
        try {
            String avatarAddress = jdbc.queryForObject("SELECT avatar_name FROM user WHERE id = ?", new Object[]{userId}, String.class);

            if (avatarAddress == null) {
                return ResponseEntity.status(404).body(null);
            }

            Path filePath = Paths.get(avatarAddress);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 设置内容类型和响应头
                String contentType = Files.probeContentType(filePath);
                contentType = (contentType == null) ? "application/octet-stream" : contentType;

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            log.error("文件下载失败: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @ApiOperation(value = "下载用户头像文件2", notes = "下载用户头像文件2")
    @GetMapping("/download2")
    public JsonResult<ResponseEntity<Resource>>  downloadFile2(@RequestParam("userId") Long userId) {
        try {
            String avatarAddress = jdbc.queryForObject("SELECT avatar_name FROM user WHERE id = ?", new Object[]{userId}, String.class);

            if (avatarAddress == null) {
                return new JsonResult<>(ResponseEntity.status(404).body(null));
            }

            Path filePath = Paths.get(avatarAddress);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 设置内容类型和响应头
                String contentType = Files.probeContentType(filePath);
                contentType = (contentType == null) ? "application/octet-stream" : contentType;

                return new JsonResult<>(ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                        .body(resource));
            } else {
                return new JsonResult<>(ResponseEntity.status(404).body(null));
            }
        } catch (Exception e) {
            log.error("文件下载失败: " + e.getMessage());
            return new JsonResult<>(ResponseEntity.status(500).body(null));
        }
    }


    @ApiOperation(value = "下载用户头像文件3", notes = "下载用户头像文件3")
    @GetMapping("/download3")
    public JsonResult<Resource>  downloadFile3(@RequestParam("userId") Long userId) {
        try {
            String avatarAddress = jdbc.queryForObject("SELECT avatar_name FROM user WHERE id = ?", new Object[]{userId}, String.class);

            if (avatarAddress == null) {
                return new JsonResult<>(201);
            }

            Path filePath = Paths.get(avatarAddress);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 设置内容类型和响应头
                String contentType = Files.probeContentType(filePath);
                contentType = (contentType == null) ? "application/octet-stream" : contentType;

                return new JsonResult<>(resource);
            } else {
                return new JsonResult<>(201);
            }
        } catch (Exception e) {
            log.error("文件下载失败: " + e.getMessage());
            return new JsonResult<>(201);
        }
    }*/


/*    @Value("${upload.path}")
    private String uploadPath;

    @ApiOperation(value = "更新用户头像",notes = "更新用户头像")
    @PostMapping("/imagesDownload")
    public ResponseEntity<Resource> getImage(@RequestBody User user) throws IOException {
        long userId = user.getId();
        log.warn("abc");
        String imageName = jdbc.queryForObject("select avatar_name from user where id=?",
                new Object[]{userId}, String.class);
        if(imageName==null||imageName==""){
            return ResponseEntity.status(404).body(null);
        }
        File file = new File(uploadPath + imageName);
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }*/

    @ApiOperation(value = "上传商品图片",notes = "上传商品图片")
    @PostMapping("/uploadProductImage")
    public ResponseEntity<String> uploadProductImage(@RequestParam("productId") Long productId,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("图片为空");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = originalFilename.toString();
            //String fileName = System.currentTimeMillis() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            Path path = Paths.get(UPLOADIMAGE_DIR + fileName);
            // 确保目录存在
            Files.createDirectories(path.getParent());
            // 将文件写入到目标位置
            Files.write(path, file.getBytes());
            String localHost = "http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/image/"+fileName;
            // 保存文件信息到内存（模拟数据库）
            jdbc.update("insert into image (productID, url) VALUES (?,?);",productId,localHost);
            //userFiles.computeIfAbsent(userId, k -> new ArrayList<>()).add(fileName);

            return ResponseEntity.ok("文件上传成功: " + localHost);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "删除该商品全部图片",notes = "删除该商品全部图片")
    @GetMapping("/deleteProductImage")
    public JsonResult<String> deleteProductImage(Long productId) {
        //先去查看是否有该图片
        try {
            Long n = jdbc.queryForObject("select count(*) from image where productId = ?;", Long.class, productId);
            if(n==0){
                return new JsonResult<>(201,"商品不存在");
            }
        }catch (Exception e){
            return new JsonResult<>(201,e.toString());
        }
        try {
            jdbc.update("delete from image where productId = ?;",productId);
            return new JsonResult<>();
        } catch (Exception e) {
            return new JsonResult<>(201,e.toString());
        }
    }


    @Value("${upload.Imagepath}")
    private String uploadImagePath;

/*
    @ApiOperation(value = "获取产品的图片的URL", notes = "根据产品ID获取图片URL")
    @GetMapping("/imagesURL")
    public JsonResult<List<String>> getImagesURL(long productId) throws IOException {
        log.warn("okok");
        // 查询数据库获取图片名称列表
        List<String> imageNames = jdbc.queryForList(
                "SELECT `Name` FROM image WHERE productID = ?",
                new Object[]{productId},
                String.class
        );

        if (imageNames.isEmpty()) {
            return new JsonResult<>(201,"没找到图片");
        }

        // 构建图片URL列表
        List<String> imageUrls = new ArrayList<>();
        for (String imageName : imageNames) {
            String imageUrl = uploadImagePath + imageName;
            imageUrls.add(imageUrl);
        }

        return new JsonResult<>(imageUrls);
    }
*/

    @ApiOperation(value = "通过用户的ID获取头像的URL", notes = "通过用户的ID获取头像的URL")
    @GetMapping("/avatarURLStragt")
    public JsonResult<String> getAvatarURLStragt(@RequestParam("userId") long userId) {
        try {
            // 查询数据库获取图片名称列表
            String URL = jdbc.queryForObject(
                    "SELECT avatar_name FROM `user` WHERE id = ?",
                    String.class,userId
            );
            log.warn(URL);
            if (URL.isEmpty()) {
                return new JsonResult<>(201, "没找到图片");
            }
            return new JsonResult<>(URL);
        } catch (Exception e) {
            return new JsonResult<>(500,e.toString());
        }
    }

    @ApiOperation(value = "通过产品的ID直接获取图片的URL", notes = "通过产品的ID直接获取图片的URL")
    @GetMapping("/imagesURLStragt")
    public JsonResult<List<Image>> getImagesURLStragt(@RequestParam("productId") long productId) {
        try {
            // 查询数据库获取图片名称列表
            List<Image> images = jdbc.query(
                    "SELECT * FROM image WHERE productID = ?",
                    new BeanPropertyRowMapper<>(Image.class),productId
            );
            if (images.isEmpty()||images==null) {
                String imageUrl = "http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认商品.png";
                Image image = new Image();
                image.setUrl(imageUrl);
                images.add(image);
                return new JsonResult<>(images);
            }
            return new JsonResult<>(images);
        } catch (Exception e) {
            return new JsonResult<>(500,e.toString());
        }
    }

/*    @ApiOperation(value = "根据产品URL获取图片",notes = "根据产品URL获取图片")
    @GetMapping("/imagesProductDownload")
    public ResponseEntity<Resource> getProductImage(String URL) throws IOException {
        log.warn("abc");
        if(URL==""){
            return ResponseEntity.status(404).body(null);
        }
        File file = new File(URL);
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }*/

    @ApiOperation(value = "根据产品Id获取第一张图片URL", notes = "根据产品Id获取第一张图片")
    @GetMapping("/productDownloadViaId")
    public JsonResult<String> productDownloadViaId(@RequestParam("productId") long productId) {
        try {
            // 查询数据库获取图片URL列表
            List<String> imageNames = jdbc.queryForList(
                    "SELECT URL FROM image WHERE productID = ?",
                    new Object[]{productId},
                    String.class
            );

            if (imageNames.isEmpty()||imageNames==null) {
                return new JsonResult<>("http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认商品.png");
            }

            // 获取第一张图片的URL
            String imageUrl = imageNames.get(0);

            return new JsonResult<>(imageUrl);
        } catch (Exception e) {
            log.error("Error while fetching the image: ", e);
            return new JsonResult<>(201,e.toString());
        }
    }

    @ApiOperation(value = "根据产品Id获取第一张图片文件", notes = "根据产品Id获取第一张图片文件2")
    @GetMapping("/productDownloadViaId2")
    public ResponseEntity<Resource> productDownloadViaId2(@RequestParam("productId") long productId) {
        try {
            // 查询数据库获取图片URL列表
            List<String> imageUrls = jdbc.queryForList(
                    "SELECT URL FROM image WHERE productID = ?",
                    new Object[]{productId},
                    String.class
            );

            if (imageUrls.isEmpty()||imageUrls==null) {
                String imageUrl = "http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认商品.png";
                Path imagePath = Paths.get(new URI(imageUrl).getPath());
                File file = new File("E:" + imagePath.toString());  // 假设文件在 E: 盘下
                Resource resource = new UrlResource(file.toURI());

                // 确定文件的 MIME 类型
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            }
            // 获取第一张图片的URL
            String imageUrl = imageUrls.get(0);

            // 将 URL 转换为文件路径
            Path imagePath = Paths.get(new URI(imageUrl).getPath());
            File file = new File("E:" + imagePath.toString());  // 假设文件在 E: 盘下

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            Resource resource = new UrlResource(file.toURI());

            // 确定文件的 MIME 类型
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            log.error("Error while fetching the image: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
