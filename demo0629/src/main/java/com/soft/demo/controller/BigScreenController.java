package com.soft.demo.controller;

import com.soft.demo.beans.XianzhiAndxuqiu;
import com.soft.demo.beans.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.soap.Text;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/BigScreen")
@RestController
@Slf4j
@Api("大屏相关接口")
public class BigScreenController {


    @Autowired//这个不能省略，否则不能使⽤，java需要⼀个创建对象的过程，在Spring中就靠它了。
    private JdbcTemplate jdbc;//建议成员变量都放在成员⽅法的上边

    @ApiOperation(value = "各类总需求，总闲置和近一周需求和闲置",notes ="各类总需求，总闲置和近一周需求和闲置")
    @GetMapping("/box4Data")
    public JsonResult<XianzhiAndxuqiu> box4Data(){
        log.warn("数据请求进来了");
        XianzhiAndxuqiu xianzhiAndxuqiu = new XianzhiAndxuqiu();
        List<Long>  xianzhis7 = new ArrayList<>();
        List<Long> xuqius7 = new ArrayList<>();
        List<Long> allxianzhis = new ArrayList<>();
        List<Long> allxuqius = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //去寻找各类的总的分布情况
        for(int i=0;i<=5;i++){
            Long allXianzhi = jdbc.queryForObject("SELECT count(*) FROM product where category = ?", Long.class,i);
            Long allxuqiu = jdbc.queryForObject("SELECT count(*) FROM seeker where category = ?", Long.class,i);
            allxianzhis.add(allXianzhi);
            allxuqius.add(allxuqiu);
        }
        xianzhiAndxuqiu.setAllxianzhi(allxianzhis);
        xianzhiAndxuqiu.setAllxuqiu(allxuqius);

        //寻找各类7天内的需求和闲置
        LocalDate requireDate = today.minusDays(7);
        LocalDate requireDate2 = today.minusDays(-1);
        String re2 = requireDate2.atStartOfDay().format(formatter);
        String requireDate1 = requireDate.atStartOfDay().format(formatter);
        log.info(requireDate1);
        log.info(re2);
        for(int i=0;i<=5;i++){
            Long Xianzhi7 = jdbc.queryForObject("SELECT count(*) FROM product where category = ? AND release_time >= ? AND release_time <= ?", Long.class,i,requireDate1,re2);
            Long xuqiu7 = jdbc.queryForObject("SELECT count(*) FROM seeker where category = ? AND release_time >= ? AND release_time <= ?", Long.class,i,requireDate1,re2);
            xianzhis7.add(Xianzhi7);
            xuqius7.add(xuqiu7);
        }
        xianzhiAndxuqiu.setXianzhi7(xianzhis7);
        xianzhiAndxuqiu.setXuqiu7(xuqius7);

        return new JsonResult<>(xianzhiAndxuqiu);
    }

    @ApiOperation(value = "最近10天内每12小时新增闲置数量")
    @GetMapping("/recent12HourUploadProduct")
    public JsonResult<List<Long>> recent12HourUploadProduct() {
        log.warn("数据请求进来了");
        List<Long> res = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 20; i++) {
            LocalDateTime endTime = now.minusHours(i * 12);
            LocalDateTime startTime = endTime.minusHours(12);

            String start = startTime.format(formatter);
            String end = endTime.format(formatter);

            Long query = jdbc.queryForObject(
                    "SELECT count(*) FROM seeker WHERE release_time > ? AND release_time < ?",
                    Long.class,
                    start,
                    end
            );

            res.add(query);
        }
        Collections.reverse(res);
        return new JsonResult<>(res);
    }


    @ApiOperation(value = "最近10天内每12小时新增求购数量")
    @GetMapping("/recent12HourUploadSeeker")
    public JsonResult<List<Long>> recent12HourUploadSeeker() {
        List<Long> res = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 20; i++) {
            LocalDateTime endTime = now.minusHours(i * 12);
            LocalDateTime startTime = endTime.minusHours(12);

            String start = startTime.format(formatter);
            String end = endTime.format(formatter);

            Long query = jdbc.queryForObject(
                    "SELECT count(*) FROM product WHERE release_time > ? AND release_time < ?",
                    Long.class,
                    start,
                    end
            );

            res.add(query);
        }
        Collections.reverse(res);
        return new JsonResult<>(res);
    }


    @ApiOperation(value = "5天内新增数量")
    @GetMapping("/fiveDaysIncre")
    public JsonResult<List<List>> fiveDaysIncre() {
        JsonResult<List<List>> result = new JsonResult<>();
        List<Integer> counts1 = new ArrayList<>();
        List<Integer> counts2= new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Calculate the counts for products
        for (int i = 4; i > -1; i--) {
            LocalDate requireDate = today.minusDays(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            int count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM product WHERE release_time >= ? AND release_time < ?",
                    new Object[]{requireDate1, requireDate2},
                    Integer.class
            );
            counts1.add(count);
        }

        // Calculate the counts for seekers
        for (int i = 4; i > -1; i--) {
            LocalDate requireDate = today.minusDays(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            int count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM seeker WHERE release_time >= ? AND release_time < ?",
                    new Object[]{requireDate1, requireDate2},
                    Integer.class
            );
            counts2.add(count);
        }
        List<List> counts3 = new ArrayList<>();
        counts3.add(counts1);
        counts3.add(counts2);
        result.setData(counts3);
        return result;
    }


    @ApiOperation(value = "7天内每日的商品发布情况")
    @GetMapping("/SevenXianZhi")
    public JsonResult<List<Long>> SevenXianZhi() {
        List<Long> res = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 6; i >= 0; i--) {
            LocalDate requireDate = today.minusDays(i);
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            log.warn(requireDate1);
            log.warn(requireDate2);
            Long query1 = jdbc.queryForObject("SELECT count(*) FROM product WHERE release_time > ? AND release_time < ?", Long.class, requireDate1, requireDate2);
            res.add(query1);
            log.info(String.valueOf(query1));
        }
        return new JsonResult<>(res);
    }

    @ApiOperation(value = "7天内每日的商品求购情况")
    @GetMapping("/SevenXuQiu")
    public JsonResult<List<Long>> SevenXuQiu() {
        log.warn("数据请求进来了");
        List<Long> res = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 6; i >= 0; i--) {
            LocalDate requireDate = today.minusDays(i);
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            log.warn(requireDate1);
            log.warn(requireDate2);
            Long query1 = jdbc.queryForObject("SELECT count(*) FROM seeker WHERE release_time > ? AND release_time < ?", Long.class, requireDate1, requireDate2);
            res.add(query1);
            log.info(String.valueOf(query1));
        }
        return new JsonResult<>(res);
    }

    @ApiOperation(value = "7天内每日的商品销售预测情况")
    @GetMapping("/SevenSalesForecast")
    public JsonResult<List<Long>> SevenSalesForecast() {
        log.warn("数据请求进来了");
        List<Long> res = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 6; i >= 0; i--) {
            LocalDate requireDate = today.minusDays(i);
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            Long query1 = jdbc.queryForObject("SELECT count(*) FROM seeker WHERE release_time > ? AND release_time < ?", Long.class, requireDate1, requireDate2);
            Long query2 = jdbc.queryForObject("SELECT count(*) FROM product WHERE release_time > ? AND release_time < ?", Long.class, requireDate1, requireDate2);
            log.warn(requireDate1);
            log.warn(requireDate2);
            res.add(query1/3+query2/4);
            log.info(String.valueOf(query1/3+query2/4));
        }
        return new JsonResult<>(res);
    }

    @ApiOperation(value = "获取最新发布的闲置 3条")
    @GetMapping("/LatestXianZhi")
    public JsonResult<List<String>> LatestXianzhi() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String t = jdbc.queryForObject(
                    "SELECT name FROM product ORDER BY release_time DESC LIMIT 1 OFFSET ?",
                    new Object[]{i},
                    String.class
            );
            String t2 = jdbc.queryForObject(
                    "SELECT description FROM product ORDER BY release_time DESC LIMIT 1 OFFSET ?",
                    new Object[]{i},
                    String.class
            );
            t=t+" "+t2;
            res.add(t);
        }
        return new JsonResult<>(res);
    }

    @ApiOperation(value = "获取最新发布的求购 3条")
    @GetMapping("/LatestXuQiu")
    public JsonResult<List<String>> LatestXuQiu() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String t = jdbc.queryForObject(
                    "SELECT name FROM seeker ORDER BY release_time DESC LIMIT 1 OFFSET ?",
                    new Object[]{i},
                    String.class
            );
            String t2 = jdbc.queryForObject(
                    "SELECT description FROM seeker ORDER BY release_time DESC LIMIT 1 OFFSET ?",
                    new Object[]{i},
                    String.class
            );
            t = t+" "+t2;
            res.add(t);
        }
        return new JsonResult<>(res);
    }

    @ApiOperation(value = "今天新增求购数量")
    @GetMapping("/todaySeeker")
    public JsonResult<List> todaySeeker() {
        JsonResult<List> result = new JsonResult<>();
        List<Integer> counts1 = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int counter = 0;
        LocalDate requireDate = today.minusDays(0);
        // Calculate the counts for products
        for (int i = 0; i < 6; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            int n = jdbc.queryForObject("SELECT COUNT(*) FROM seeker WHERE release_time >= ? AND release_time < ? and category = ?",int.class,requireDate1,requireDate2,i);
            counts1.add(n);
            counter+=n;
        }
        counts1.add(0,counter);
        result.setData(counts1);
        return result;
    }

    @ApiOperation(value = "今天新增商品数量")
    @GetMapping("/todayProduct")
    public JsonResult<List> todayProduct() {
        JsonResult<List> result = new JsonResult<>();
        List<Integer> counts1 = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int counter = 0;
        LocalDate requireDate = today.minusDays(0);
        // Calculate the counts for products
        for (int i = 0; i < 6; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String requireDate1 = requireDate.atStartOfDay().format(formatter);
            String requireDate2 = requireDate.atTime(23, 59).format(formatter);
            int n = jdbc.queryForObject("SELECT COUNT(*) FROM product WHERE release_time >= ? AND release_time < ? and category = ?",int.class,requireDate1,requireDate2,i);
            counts1.add(n);
            counter+=n;
        }
        counts1.add(0,counter);
        result.setData(counts1);
        return result;
    }

    @ApiOperation(value = "总用户，总发布，总求购")
    @GetMapping("/AllData")
    public JsonResult<List<Long>> AllDate() {
        List<Long> res = new ArrayList<>();
        Long users = jdbc.queryForObject("select count(*) from user;",Long.class);
        Long products = jdbc.queryForObject("select count(*) from product where onSale = 1;",Long.class);
        Long seekers = jdbc.queryForObject("select count(*) from seeker where isOpen = 1;",Long.class);
        res.add(users);
        res.add(products);
        res.add(seekers);
        return new JsonResult<>(res);
    }

    @ApiOperation(value = "书籍漂流瓶数目")
    @GetMapping("/BookData")
    public JsonResult<Long> BookData() {
        Long aLong = jdbc.queryForObject("select count(*) from book;", Long.class);
        log.warn(String.valueOf(aLong));
        return new JsonResult<>(aLong);
    }




}
