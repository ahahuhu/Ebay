package com.soft.demo.controller;

import com.soft.demo.beans.JsonResult;
import com.soft.demo.beans.Product;
import com.soft.demo.beans.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "商品搜索接口")
public class ProductSearchController {

    @Autowired
    private JdbcTemplate jdbc;

    @ApiOperation(value = "关键字搜索商品")
    @GetMapping("/searchProductByName")
    public JsonResult<List> searchProductByName(@RequestParam @ApiParam(value = "搜索name关键字") String words) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("搜索商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT id FROM product WHERE name LIKE ? and onSale = 1";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, "%" + words + "%"); // 设置参数值，注意不需要引号
            ResultSet rs = stmt.executeQuery();
            log.info("搜索成功");
            ProductController PC = new ProductController();
            PC.setJdbc(this.jdbc);
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = PC.getProduct(productID).getData();
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                l.add(p); // 将每个收藏的商品ID添加到列表中
            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("搜索失败");
        }
        return result;
    }

    @ApiOperation(value = "类别搜索商品")
    @GetMapping("/searchProductByCategory")
    public JsonResult<List> searchProductByCategory(@RequestParam @ApiParam(value = "类别") int cate) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("搜索商品");
        try {
            // int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT id FROM product WHERE category = ? and onSale = 1";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, cate); // 设置参数值，注意不需要引号
            ResultSet rs = stmt.executeQuery();
            log.info("搜索成功");
            ProductController PC = new ProductController();
            PC.setJdbc(this.jdbc);
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = PC.getProduct(productID).getData();
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                l.add(p); // 将每个收藏的商品ID添加到列表中
            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("搜索失败");
        }
        return result;
    }

    @ApiOperation(value = "关键字搜索商品，按价格排序")
    @GetMapping("/searchProductByNamePrice")
    public JsonResult<List> searchProductByNamePrice(@RequestParam @ApiParam(value = "搜索name关键字") String words ,@RequestParam @ApiParam(value = "1升序，0降序") int asc) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("搜索商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query;
            if (asc==1){
                query = "SELECT id FROM product WHERE name LIKE ? and onSale = 1 order by price";
            }else{
                query = "SELECT id FROM product WHERE name LIKE ? and onSale = 1 order by price desc";
            }
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, "%" + words + "%"); // 设置参数值，注意不需要引号
            ResultSet rs = stmt.executeQuery();
            log.info("搜索成功");
            ProductController PC = new ProductController();
            PC.setJdbc(this.jdbc);
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = PC.getProduct(productID).getData();
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                l.add(p); // 将每个收藏的商品ID添加到列表中
            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("搜索失败");
        }
        return result;
    }

    @ApiOperation(value = "类别搜索商品，按价格排序")
    @GetMapping("/searchProductByCategoryPrice")
    public JsonResult<List> searchProductByCategoryPrice(@RequestParam @ApiParam(value = "类别") int cate,@RequestParam @ApiParam(value = "1升序，0降序") int asc) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("搜索商品");
        try {
            // int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query;
            if (asc==1){
                query = "SELECT id FROM product WHERE category = ? and onSale = 1 order by price";
            }else{
                query = "SELECT id FROM product WHERE category = ? and onSale = 1 order by price desc";
            }
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, cate); // 设置参数值，注意不需要引号
            ResultSet rs = stmt.executeQuery();
            log.info("搜索成功");
            ProductController PC = new ProductController();
            PC.setJdbc(this.jdbc);
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = PC.getProduct(productID).getData();
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                l.add(p); // 将每个收藏的商品ID添加到列表中
            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("搜索失败");
        }
        return result;
    }


    @ApiOperation(value = "关键字搜索商品，按时间排序")
    @GetMapping("/searchProductByNameTime")
    public JsonResult<List> searchProductByNameTime(@RequestParam @ApiParam(value = "搜索name关键字") String words ,@RequestParam @ApiParam(value = "1升序，0降序") int asc) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("搜索商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query;
            if (asc==1){
                query = "SELECT id FROM product WHERE name LIKE ? and onSale = 1 order by release_time";
            }else{
                query = "SELECT id FROM product WHERE name LIKE ? and onSale = 1 order by release_time desc";
            }
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, "%" + words + "%"); // 设置参数值，注意不需要引号
            ResultSet rs = stmt.executeQuery();
            log.info("搜索成功");
            ProductController PC = new ProductController();
            PC.setJdbc(this.jdbc);
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = PC.getProduct(productID).getData();
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                l.add(p); // 将每个收藏的商品ID添加到列表中
            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("搜索失败");
        }
        return result;
    }

    @ApiOperation(value = "类别搜索商品，按时间排序")
    @GetMapping("/searchProductByCategoryTime")
    public JsonResult<List> searchProductByCategoryTime(@RequestParam @ApiParam(value = "类别") int cate,@RequestParam @ApiParam(value = "1升序，0降序") int asc) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("搜索商品");
        try {
            // int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query;
            if (asc==1){
                query = "SELECT id FROM product WHERE category = ? and onSale = 1 order by release_time";
            }else{
                query = "SELECT id FROM product WHERE category = ? and onSale = 1 order by release_time desc";
            }
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, cate); // 设置参数值，注意不需要引号
            ResultSet rs = stmt.executeQuery();
            log.info("搜索成功");
            ProductController PC = new ProductController();
            PC.setJdbc(this.jdbc);
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = PC.getProduct(productID).getData();
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                l.add(p); // 将每个收藏的商品ID添加到列表中
            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("搜索失败");
        }
        return result;
    }
}
