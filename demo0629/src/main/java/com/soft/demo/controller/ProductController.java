package com.soft.demo.controller;


import com.soft.demo.beans.JsonResult;
import com.soft.demo.beans.Product;
import com.soft.demo.beans.Requires;
import com.soft.demo.beans.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@CrossOrigin
@Api(tags = "商品接口")
public class ProductController {
    // 初始化jdbc，要加这个语法才会初始化
    @Autowired
    private JdbcTemplate jdbc;

    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @ApiOperation(value = "发布商品")
    @PostMapping("/addProduct")
    public JsonResult<Product> addProduct(@RequestBody Product product) {

        JsonResult result = new JsonResult<Product>();
        System.out.println("请求来了");

        System.out.println(product.getReleaseTime());

        long currentTimeMillis = System.currentTimeMillis();

//        product.setReleaseTime(new java.sql.Timestamp(currentTimeMillis));
//        System.out.println(new java.sql.Timestamp(currentTimeMillis));
        product.setReleaseTime(new Timestamp(System.currentTimeMillis()));

        System.out.println(product.getReleaseTime());
        try {
            int num = jdbc.update("INSERT into product (name,description,price,user_id,delivery_area,category,onSale)" +
                            "VALUES (?,?,?,?,?,?,?);", product.getName(), product.getDescription(), product.getPrice(), product.getUserId(),
                    product.getDeliveryArea(), product.getCategory(), 1);
            // xxx.var自动生成变量
            System.out.println(num);


            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            String query = "SELECT MAX(id) FROM product";
            ResultSet rs = stmt.executeQuery(query);
            int maxId = 1;
            if (rs.next()) {
                maxId = rs.getInt(1);
                System.out.println("Max ID: " + maxId);
            }
            rs.close();


            Product p = jdbc.queryForObject("SELECT * from product WHERE id = ?;", new BeanPropertyRowMapper<>(Product.class),
                    maxId);
            result.setCode(200);
            System.out.println(num);
            result.setData(p);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("添加商品失败");
        }
        return result;
    }


    @ApiOperation(value = "修改商品信息")
    @PostMapping("/updateProduct")
    public JsonResult<Product> updateProduct(@RequestBody Product product) {
        JsonResult result = new JsonResult<Product>();

        try {
            int num = jdbc.update("UPDATE product set `name` = ?,description = ?,price = ?,delivery_area = ?,category = ?\n" +
                            "WHERE id = ?;", product.getName(), product.getDescription(), product.getPrice(),
                    product.getDeliveryArea(), product.getCategory(), product.getId());
            // xxx.var自动生成变量
            // jdbc.update()
            result.setCode(200);
            System.out.println(num);
            result.setData(product);
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("修改信息失败");
        }
        return result;
    }

    @ApiOperation(value = "获取商品信息")
    @GetMapping("/getProduct")
    public JsonResult<Product> getProduct(@RequestParam @ApiParam(value = "商品id") int id) {
        JsonResult result = new JsonResult<Product>();
        log.info("获取商品信息");
        try {
            Product p = jdbc.queryForObject("SELECT * from product WHERE id = ?;", new BeanPropertyRowMapper<>(Product.class), id);
            User user = jdbc.queryForObject("select * from `user` where id = ?;", new BeanPropertyRowMapper<>(User.class), p.getUserId());
            p.setUserName(user.getNickname());
            FileUploadController FUC = new FileUploadController();
            FUC.setJdbc(this.jdbc);
            String url=FUC.productDownloadViaId(p.getId()).getData();
            p.setURL(url);
            result.setData(p);
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("查询信息失败");
        }
        return result;
    }


    @ApiOperation(value = "下架商品")
    @GetMapping("/deleteProduct")
    public JsonResult<String> deleteProduct(@RequestParam @ApiParam(value = "商品id") int id) {

        JsonResult result = new JsonResult<String>();

        log.info("删除商品");
        try {
            int num = jdbc.update("UPDATE product set onSale = 0 where id = ?;", id);

            result.setCode(200);
            System.out.println(num);
            result.setData("商品下架成功");
        } catch (DataAccessException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("删除失败");
        }
        return result;
    }



    @ApiOperation(value = "获取用户发布的所有商品")
    @GetMapping("/userProduct")
    public JsonResult<Product> userProduct(@RequestParam @ApiParam(value = "用户id") int userid) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("获取用户的所有商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT id from product WHERE user_id = ?;";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userid);
//            log.info(Integer.toString(userID));
            ResultSet rs = stmt.executeQuery();
            log.info("查询成功");
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = jdbc.queryForObject("SELECT * from product WHERE id = ? and onSale = 1;", new BeanPropertyRowMapper<>(Product.class),
                        productID);
                int userId = (int) p.getUserId();
                User user1 = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), userId);
                p.setUserName(user1.getNickname());
                //根据productID去寻找图片的一张图片的URL
                List<String> imageNames = jdbc.queryForList(
                        "SELECT URL FROM image WHERE productID = ?",
                        new Object[]{productID},
                        String.class
                );
                if (imageNames.isEmpty()) {
                    return new JsonResult<>(201,"not found");
                }

                // 获取第一张图片的URL
                String imageUrl = imageNames.get(0);
                p.setURL(imageUrl);
                if(p.getURL()==""||p.getURL()==null){
                   imageUrl = "http://"+ InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认商品.png";
                    p.setURL(imageUrl);
                }
                l.add(p); // 将每个收藏的商品ID添加到列表中
//                log.info(Integer.toString(productID));

            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException | UnknownHostException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("获取失败");
        }
        return result;
    }



    @ApiOperation(value = "获取所有在售的商品")
    @GetMapping("/allProduct")
    public JsonResult<List> allProduct() {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Product>();
        log.info("获取用户的所有商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT id from product where onSale = 1 order by rand();";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
//            log.info(Integer.toString(userID));
            ResultSet rs = stmt.executeQuery();
            log.info("查询成功");
            while (rs.next()) {
                int productID = rs.getInt("id");
                Product p = jdbc.queryForObject("SELECT * from product WHERE id = ?;", new BeanPropertyRowMapper<>(Product.class),
                        productID);
                User u = jdbc.queryForObject("SELECT nickname from user where id = ?",new BeanPropertyRowMapper<>(User.class),p.getUserId());
                p.setUserName(u.getNickname());
                FileUploadController FUC = new FileUploadController();
                FUC.setJdbc(this.jdbc);
                p.setURL(FUC.productDownloadViaId(p.getId()).getData());
                if(p.getURL()==""||p.getURL()==null){
                    String imageUrl = "http://"+ InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认商品.png";
                    p.setURL(imageUrl);
                }
                l.add(p); // 将每个收藏的商品ID添加到列表中
//                log.info(Integer.toString(productID));

            }
            rs.close();

            result.setCode(200);
            result.setData(l);
        } catch (DataAccessException | SQLException | UnknownHostException e) {
            e.printStackTrace();
            result.setCode(201);
            result.setData("获取失败");
        }
        return result;
    }

    @ApiOperation(value = "购买商品，更改商品状态")
    @GetMapping("/changeProduct")
    public JsonResult<String> changeProduct(int id) {
        try {
            Integer integer = jdbc.queryForObject("select count(*) from product where id= ?;", new BeanPropertyRowMapper<>(int.class), id);
            if(integer==0){
                return new JsonResult<>(201,"商品已经出售");
            }
            jdbc.update("update product set onSale = 0 where id = ?;",id);
            return new JsonResult<>(200);
        } catch (Exception e) {
            return new JsonResult<>(e.toString());
        }
    }

    @ApiOperation(value = "获取用户发布的所有商品ID")
    @GetMapping("/userProductOnlyID")
    public JsonResult<List> userProductOnlyID(@RequestParam @ApiParam(value = "用户id") int userID) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<>();
        log.info("获取用户的所有商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT id from product WHERE user_id = ?;";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userID);
//            log.info(Integer.toString(userID));
            ResultSet rs = stmt.executeQuery();
            log.info("查询成功");
            while (rs.next()) {
                int productID = rs.getInt("id");
                l.add(productID); // 将每个收藏的商品ID添加到列表中
//                log.info(Integer.toString(productID));

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

    @ApiOperation(value = "获取用户发布的所有在售的商品ID")
    @GetMapping("/userProductSaleOnlyID")
    public JsonResult<List> userProductSaleOnlyID(@RequestParam @ApiParam(value = "用户id") int userID) {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<>();
        log.info("获取用户的所有商品");
        try {
//            int num = jdbc.query("SELECT productID from favorite WHERE userID = ?;", int.class, userID);

            String url = "jdbc:mysql://localhost:3306/xianyu";
            String username = "root";
            String password = "123456";
            String query = "SELECT id from product WHERE user_id = ? and onSale = 1;";
            // 创建Connection对象
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userID);
//            log.info(Integer.toString(userID));
            ResultSet rs = stmt.executeQuery();
            log.info("查询成功");
            while (rs.next()) {
                int productID = rs.getInt("id");
                l.add(productID); // 将每个收藏的商品ID添加到列表中
//                log.info(Integer.toString(productID));

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

}
