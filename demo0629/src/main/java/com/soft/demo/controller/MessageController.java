package com.soft.demo.controller;


import com.soft.demo.beans.JsonResult;
import com.soft.demo.beans.Message;
import com.soft.demo.beans.MessageUsers;
import com.soft.demo.beans.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "消息接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/order")
public class MessageController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation("发送信息")
    @PostMapping("/message")
    public int sendMessage(@RequestBody Message message) {
        String sql = "INSERT INTO message (sender_id, receiver_id, content, timestamp,visited) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, message.getSenderId(), message.getReceiverId(), message.getContent(), new Timestamp(System.currentTimeMillis()),0);
    }

    @ApiOperation("请求对方信息")
    @GetMapping("/requiremessage/{senderId}/{receiverId}")
    public JsonResult<List<Message>> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        String sql = "SELECT * FROM message WHERE (sender_id = ? AND receiver_id = ? AND visited = 0) ORDER BY timestamp DESC";
        try {
            List<Message> messages = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Message.class), senderId, receiverId);

            if (!messages.isEmpty() && messages.get(0).getVisited() == 0) {
                jdbcTemplate.update("UPDATE message SET visited = 1 WHERE receiver_id = ?", messages.get(0).getReceiverId());
                return new JsonResult<>(messages);
            }
            return new JsonResult<>(201, "No new messages");
        } catch (Exception e) {
            return new JsonResult<>(201, e.toString());
        }
    }

    @ApiOperation("请求发送给当前用户未读的消息")
    @GetMapping("/requireHistory/{userId}")
    public JsonResult<List<MessageUsers>> requireHistory(@PathVariable long userId){
        //去找哪些用户给当前用户发送了信息
        String sql = "SELECT m.sender_id, m.timestamp, m.content,m.visited\n" +
                "FROM message m\n" +
                "         JOIN (\n" +
                "    SELECT sender_id, MAX(timestamp) AS max_timestamp\n" +
                "    FROM message\n" +
                "    WHERE receiver_id = ?\n" +
                "    GROUP BY sender_id\n" +
                ") latest ON m.sender_id = latest.sender_id AND m.timestamp = latest.max_timestamp\n" +
                "ORDER BY m.timestamp DESC;";
        List<Message> sender_messages = new ArrayList<>();
        List<MessageUsers> messageusers=new ArrayList<>();
        try {
            sender_messages = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Message.class), userId);
            //遍历每一个sender_id，去数据库里面寻找
            for(Message message: sender_messages){
                MessageUsers t = new MessageUsers();
                t.setId(message.getSenderId());
                User user1 = new User();
                try {
                    user1 = jdbcTemplate.queryForObject("select * from user where id = ?", new BeanPropertyRowMapper<>(User.class), message.getSenderId());
                }catch (Exception e){
                    log.info(e.toString());
                    return new  JsonResult<>(201);
                }

                String avatar;
                if(user1.getAvatarName()==""||user1.getAvatarName()==null||user1.getAvatarName()=="string"){
                    avatar = "http://"+InetAddress.getLocalHost().getHostAddress()+":8016/test/avatar/默认头像.png";
                }
                else{
                    avatar = user1.getAvatarName();
                }
                t.setAvatar_name(avatar);
                t.setName(user1.getNickname());
                t.setTime(message.getTimestamp().toString());
                t.setMsg(message.getContent());
                t.setRead((int) message.getVisited());
                messageusers.add(t);
            }
            return new JsonResult<>(messageusers);
        }catch (Exception e){
            return new JsonResult<>(201,e.toString());
        }
    }



}
