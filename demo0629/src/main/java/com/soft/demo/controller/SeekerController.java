package com.soft.demo.controller;

import com.soft.demo.beans.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.TimestampsCacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "求购接口")
public class SeekerController {

    // 初始化jdbc，要加这个语法才会初始化
    @Autowired
    private JdbcTemplate jdbc;

    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @ApiOperation(value = "发布求购")
    @PostMapping("/addSeeker")
    public JsonResult<Seeker> addSeeker(@RequestBody Seeker seeker) {

        JsonResult result = new JsonResult<Seeker>();
        seeker.setReleaseTime(new Timestamp(System.currentTimeMillis()));

        System.out.println(seeker.getReleaseTime());
        try {
            int num = jdbc.update("INSERT into seeker (name,description,price,userID,address,category,isOpen)" +
                            "VALUES (?,?,?,?,?,?,?);", seeker.getName(), seeker.getDescription(), seeker.getPrice(), seeker.getUserId(),
                    seeker.getAddress(), seeker.getCategory(), 1);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            String query = "SELECT MAX(seekerID) FROM seeker";
            ResultSet rs = stmt.executeQuery(query);
            int maxId = 1;
            if (rs.next()) {
                maxId = rs.getInt(1);
                System.out.println("Max ID: " + maxId);
            }
            rs.close();


            Seeker s = jdbc.queryForObject("SELECT * from seeker WHERE seekerID = ?;", new BeanPropertyRowMapper<>(Seeker.class),
                    maxId);
            result.setCode(200);
            System.out.println(num);
            result.setData(s);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("添加商品失败");
        }
        return result;
    }


    @ApiOperation(value = "获取求购信息")
    @GetMapping("/getSeeker")
    public JsonResult<Seeker> getSeeker(@RequestParam @ApiParam(value = "求购id") int seekerID) {
        JsonResult result = new JsonResult<Seeker>();
        log.info("获取求购信息");
        try {
            Seeker s = jdbc.queryForObject("SELECT * from seeker WHERE seekerID = ?;", new BeanPropertyRowMapper<>(Seeker.class), seekerID);
            User u = jdbc.queryForObject("SELECT * from user WHERE id = ?;", new BeanPropertyRowMapper<>(User.class), s.getUserId());
            Requires r = new Requires(u,s);
            result.setCode(200);
            System.out.println(r);
            result.setData(r);
        } catch (DataAccessException | UnknownHostException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("查询信息失败");
        }
        return result;
    }


    @ApiOperation(value = "修改求购信息")
    @PostMapping("/updateSeeker")
    public JsonResult<Seeker> updateSeeker(@RequestBody Seeker seeker) {
        JsonResult result = new JsonResult<Seeker>();

        try {
            int num = jdbc.update("UPDATE seeker set name = ?,description = ?,price = ?,address = ?,category = ?\n" +
                            "WHERE seekerID = ?;", seeker.getName(), seeker.getDescription(), seeker.getPrice(),
                    seeker.getAddress(), seeker.getCategory(), seeker.getSeekerId());
            // xxx.var自动生成变量
            // jdbc.update()
            result.setCode(200);
            System.out.println(num);
            Seeker seeker1 = jdbc.queryForObject("select * from seeker where seekerID = ?",new BeanPropertyRowMapper<>(Seeker.class),seeker.getSeekerId());
            result.setData(seeker1);
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("修改信息失败");
        }
        return result;
    }

    @ApiOperation(value = "关闭求购")
    @GetMapping("/closeSeeker")
    public JsonResult<String> closeSeeker(@RequestParam @ApiParam(value = "求购id") int id) {

        JsonResult result = new JsonResult<String>();

        log.info("关闭求购");
        try {
            int num = jdbc.update("UPDATE seeker set isOpen = 0 where seekerID = ?;", id);

            result.setCode(200);
            System.out.println(num);
            result.setData("求购已关闭");
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("关闭失败");
        }
        return result;
    }


    @ApiOperation(value = "获取用户的所有求购")
    @GetMapping("/userSeeker")
    public JsonResult<List<Requires>> userSeeker(@RequestParam @ApiParam(value = "用户id") int userID) {
        try {
            List<Requires> requires = new ArrayList<>();
            List<Seeker> seekers = jdbc.query("select * from seeker where isOpen = 1 and userID = ?;", new BeanPropertyRowMapper<>(Seeker.class),userID);
            for(Seeker seeker:seekers){
                User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
                Requires requires1= new Requires(user,seeker);
                requires.add(requires1);
            }
            return new JsonResult<>(requires);
        }catch (Exception e){
            return new JsonResult<>(201,e.toString());
        }
    }

    @ApiOperation(value = "获取all用户的所有求购")
    @GetMapping("/allUsersSeeker")
    public JsonResult<List<Requires>> getAllUsersSeeker(){
        try {
            List<Requires> requires = new ArrayList<>();
            List<Seeker> seekers = jdbc.query("select * from seeker where isOpen = 1 order by release_time desc;", new BeanPropertyRowMapper<>(Seeker.class));
            for(Seeker seeker:seekers){
                User user = jdbc.queryForObject("select * from `user` where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
                Requires requires1= new Requires(user,seeker);

                Timestamp t = requires1.getReleaseTime();
                // 创建SimpleDateFormat对象，并指定日期时间格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // 使用format方法将Timestamp转换为字符串
                String formattedDate = sdf.format(t);
                requires1.setGoodTime(formattedDate);
                requires.add(requires1);
            }
            return new JsonResult<>(requires);
        }catch (Exception e){
            return new JsonResult<>(201,e.toString());
        }

    }

}

