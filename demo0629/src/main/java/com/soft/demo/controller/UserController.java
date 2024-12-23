package com.soft.demo.controller;


import com.soft.demo.beans.JsonResult;
import com.soft.demo.beans.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;


@Api(tags = "用户接口")
@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired//这个不能省略，否则不能使⽤，java需要⼀个创建对象的过程，在Spring中就靠它了。
    private JdbcTemplate jdbc;//建议成员变量都放在成员⽅法的上边



    @ApiOperation(value = "用户登录",notes = "用户登录")
    //因为是接受json格式的数据，说以要加@ResponseBody
    @PostMapping("/doLogin")
    public JsonResult<User> doLogin(@RequestBody @ApiParam(name = "用户", value = "只需要账号（account）和密码（passpart），请发json格式",required = true) User user){

        //是否接受数据
        System.out.println(user.toString());
        log.info("成功接受数据");
        User user1;
        try {
            user1 = jdbc.queryForObject("select * from user where account = ? and password =?;",
                    new BeanPropertyRowMapper<>(User.class),user.getAccount(),user.getPassword() );
        }catch (Exception e){
            log.error("登录失败");
            return new JsonResult<>(201);
        }
        return new JsonResult<>(user1);
    }
    @ApiOperation(value = "用户注册",notes = "用户注册")
    @PostMapping("/doRegister")
    public JsonResult<User> doRegister(@RequestBody @ApiParam(name = "用户完整信息",value = "全部信息都要，发json格式",required = true) User user) throws UnknownHostException {
        //1.正常获取数据
        log.info("ll");
        int cows = jdbc.queryForObject("select count(*) from user where account = ?", int.class, user.getAccount());
        if(cows!=0) {
            return new JsonResult<>(201,"重复注册");
        }
        String avatar;
        if(user.getAvatarName()==""||user.getAvatarName()==null||user.getAvatarName()=="string"){
            avatar = "http://"+ InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认头像.png";
        }
        else{
            avatar = user.getAvatarName();
        }
        //2.操纵数据库
        jdbc.update("insert into user (account, password, nickname, registration_time, status, avatar_name) values (?,?,?,?,?,?);",
                user.getAccount(), user.getPassword(),user.getNickname(), new Timestamp(System.currentTimeMillis()),1,avatar);
        return new JsonResult<>(user);
    }


    @ApiOperation(value = "用户更新",notes = "用户更新")
    @PostMapping("/doUpdate")
    public JsonResult<User> doUpdate(@RequestBody @ApiParam(name = "用户",value = "全部用户信息，json格式",required = true) User user){
        log.info(user.toString());
        log.info("更新操作数据成功到达后端");
        try {
            jdbc.update("update user set account = ?, password = ?, nickname = ?, avatar_address = ? where id = ?",user.getAccount(),user.getPassword(),user.getNickname(),user.getAvatarName(),user.getId());
            log.info(user.toString());
            User user2 = jdbc.queryForObject("select * from user where id = ?", new BeanPropertyRowMapper<>(User.class),user.getId());
            log.info(user2.toString());
        }catch (Exception e){
            log.info(e.toString());
            return new  JsonResult<>(201);
        }
        return new  JsonResult<>();
    }

    @ApiOperation(value = "用户搜寻",notes = "用户搜索,只传用户id")
    @PostMapping("/doSearch")
    public JsonResult<User> doSearch(@RequestBody User user){
        try {
            long userId = user.getId();
            User user1 = jdbc.queryForObject("select * from user where id = ?", new BeanPropertyRowMapper<>(User.class), userId);
            return new JsonResult<>(user1);
        }catch (Exception e){
            log.info(e.toString());
            return new  JsonResult<>(201);
        }
    }


}
