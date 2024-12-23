package com.soft.demo.controller;


import com.soft.demo.beans.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "求购搜索接口")
public class SeekerSearchController {

    @Autowired
    private JdbcTemplate jdbc;

    @ApiOperation(value = "关键字搜索求购")
    @GetMapping("/searchSeekerByName")
    public JsonResult<List> searchSeekerByName(@RequestParam @ApiParam(value = "搜索name关键字") String words) throws UnknownHostException {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Requires>();
        log.info("搜索求购");
        List<Seeker> seekers = jdbc.query("SELECT * FROM seeker WHERE name LIKE ? and isOpen = 1", new BeanPropertyRowMapper<>(Seeker.class),"%"+words+"%");
        for(Seeker seeker:seekers){
            User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
            Requires requires1= new Requires(user,seeker);
            l.add(requires1);
        }
        result.setData(l);
        return result;
    }

    @ApiOperation(value = "类别搜索求购")
    @GetMapping("/searchSeekerByCategory")
    public JsonResult<List> searchSeekerByCategory(@RequestParam @ApiParam(value = "类别") int cate) throws UnknownHostException {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Requires>();
        log.info("搜索求购");
        List<Seeker> seekers = jdbc.query("SELECT * FROM seeker WHERE category = ? and isOpen = 1", new BeanPropertyRowMapper<>(Seeker.class),cate);
        for(Seeker seeker:seekers){
            User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
            Requires requires1= new Requires(user,seeker);
            l.add(requires1);
        }
        result.setData(l);
        return result;
    }

    @ApiOperation(value = "关键字搜索求购，按价格排序")
    @GetMapping("/searchSeekerByNamePrice")
    public JsonResult<List> searchSeekerByNamePrice(@RequestParam @ApiParam(value = "搜索name关键字") String words, @RequestParam @ApiParam(value = "1升序，0降序") int asc) throws UnknownHostException {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Requires>();
        log.info("搜索求购");
        List<Seeker> seekers;
        if (asc==1){
            seekers = jdbc.query("SELECT * FROM seeker WHERE name LIKE ? and isOpen = 1 order by price", new BeanPropertyRowMapper<>(Seeker.class),"%"+words+"%");
        }else{
            seekers = jdbc.query("SELECT * FROM seeker WHERE name LIKE ? and isOpen = 1 order by price desc", new BeanPropertyRowMapper<>(Seeker.class),"%"+words+"%");
        }
        for(Seeker seeker:seekers){
            User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
            Requires requires1= new Requires(user,seeker);
            l.add(requires1);
        }
        result.setData(l);
        return result;
    }

    @ApiOperation(value = "类别搜索求购，按价格排序")
    @GetMapping("/searchSeekerByCategoryPrice")
    public JsonResult<List> searchSeekerByCategoryPrice(@RequestParam @ApiParam(value = "类别") int cate, @RequestParam @ApiParam(value = "1升序，0降序") int asc) throws UnknownHostException {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Requires>();
        log.info("搜索求购");
        List<Seeker> seekers;
        if (asc==1){
            seekers = jdbc.query("SELECT * FROM seeker WHERE category = ? and isOpen = 1 order by price", new BeanPropertyRowMapper<>(Seeker.class),cate);
        }else{
            seekers = jdbc.query("SELECT * FROM seeker WHERE category = ? and isOpen = 1 order by price desc", new BeanPropertyRowMapper<>(Seeker.class),cate);
        }
        for(Seeker seeker:seekers){
            User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
            Requires requires1= new Requires(user,seeker);
            l.add(requires1);
        }
        result.setData(l);
        return result;
    }


    @ApiOperation(value = "关键字搜索求购，按时间排序")
    @GetMapping("/searchSeekerByNameTime")
    public JsonResult<List> searchSeekerByNameTime(@RequestParam @ApiParam(value = "搜索name关键字") String words, @RequestParam @ApiParam(value = "1升序，0降序") int asc) throws UnknownHostException {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Requires>();
        log.info("搜索求购");
        List<Seeker> seekers;
        if (asc==1){
            seekers = jdbc.query("SELECT * FROM seeker WHERE name LIKE ? and isOpen = 1 order by release_time", new BeanPropertyRowMapper<>(Seeker.class),"%"+words+"%");
        }else{
            seekers = jdbc.query("SELECT * FROM seeker WHERE name LIKE ? and isOpen = 1 order by release_time desc", new BeanPropertyRowMapper<>(Seeker.class),"%"+words+"%");
        }
        for(Seeker seeker:seekers){
            User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
            Requires requires1= new Requires(user,seeker);
            l.add(requires1);
        }
        result.setData(l);
        return result;
    }

    @ApiOperation(value = "类别搜索求购，按时间排序")
    @GetMapping("/searchSeekerByCategoryTime")
    public JsonResult<List> searchSeekerByCategoryTime(@RequestParam @ApiParam(value = "类别") int cate, @RequestParam @ApiParam(value = "1升序，0降序") int asc) throws UnknownHostException {
        JsonResult result = new JsonResult<List>();
        List l = new ArrayList<Requires>();
        log.info("搜索求购");
        List<Seeker> seekers;
        if (asc==1){
            seekers = jdbc.query("SELECT * FROM seeker WHERE category = ? and isOpen = 1 order by release_time", new BeanPropertyRowMapper<>(Seeker.class),cate);
        }else{
            seekers = jdbc.query("SELECT * FROM seeker WHERE category = ? and isOpen = 1 order by release_time desc", new BeanPropertyRowMapper<>(Seeker.class),cate);
        }
        for(Seeker seeker:seekers){
            User user = jdbc.queryForObject("select * from user where id = ?;", new BeanPropertyRowMapper<>(User.class), seeker.getUserId());
            Requires requires1= new Requires(user,seeker);
            l.add(requires1);
        }
        result.setData(l);
        return result;
    }
}