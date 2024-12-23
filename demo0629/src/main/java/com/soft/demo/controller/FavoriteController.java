package com.soft.demo.controller;


import com.soft.demo.beans.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@RestController
@CrossOrigin
@Api(tags = "收藏夹接口")
public class FavoriteController {
    @Autowired
    private JdbcTemplate jdbc;


    @ApiOperation(value = "收藏商品")
    @GetMapping("/setFavorite")
    public JsonResult<String> setFavorite(@RequestParam @ApiParam(value = "商品id") int id, @RequestParam @ApiParam(value = "用户id") int userID) {
        JsonResult result = new JsonResult<String>();
        log.info("收藏商品");
        try {
            int n = jdbc.queryForObject("SELECT COUNT(*) FROM favorite where userID = ? and productID = ?;",int.class,userID,id);
            if (n==0){
                int num = jdbc.update("INSERT into favorite (userID , productID) VALUES (? ,?);", userID, id);

                result.setCode(200);
                System.out.println(num);
                result.setData("收藏成功");
            }else{
                result.setCode(202);
                result.setData("商品已收藏");
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("收藏失败");
        }
        return result;
    }

    @ApiOperation(value = "从收藏中删除某商品")
    @GetMapping("/deleteFavorite")
    public JsonResult<String> deleteFavorite(@RequestParam @ApiParam(value = "商品id") int id, @RequestParam @ApiParam(value = "用户id") int userID) {
        JsonResult result = new JsonResult<String>();
        log.info("删除收藏商品");
        try {
            int num = jdbc.update("DELETE from favorite where userID = ? and productID = ?;", userID, id);

            result.setCode(200);
            System.out.println(num);
            result.setData("删除成功");
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("删除失败");
        }
        return result;
    }

    @ApiOperation(value = "获取用户所有收藏商品")
    @GetMapping("/allFavorite")
    public JsonResult<String> allFavorite(@RequestParam @ApiParam(value = "用户id") int userID) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList();
        log.info("获取用户所有收藏商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT productID from favorite WHERE userID = ?;";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userID);
//            log.info(Integer.toString(userID));
            ResultSet rs = stmt.executeQuery();
            log.info("查询成功");
            while (rs.next()) {
                int productID = rs.getInt("productID");
                l.add(productID); // 将每个收藏的商品ID添加到列表中
                log.info(Integer.toString(productID));

            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("获取失败");
        }
        return result;
    }

    @ApiOperation(value = "返回是否收藏")
    @GetMapping("/isFavorited")
    public JsonResult isFavorited(@RequestParam @ApiParam(value = "用户id") int userID,@RequestParam @ApiParam(value = "商品id")int productID ) {
        JsonResult result = new JsonResult<>();
        log.info("返回是否收藏");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);
            String query = "SELECT count(*) from favorite WHERE userID = ? and productID = ?;";
            int n = jdbc.queryForObject(query,int.class,userID,productID);
            if (n==0){
                result.setData(1);
            }else{
                result.setData(0);
            }
            result.setCode(200);
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("获取失败");
        }
        return result;
    }

}
