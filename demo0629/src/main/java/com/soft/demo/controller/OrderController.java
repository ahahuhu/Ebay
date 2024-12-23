package com.soft.demo.controller;

        import com.soft.demo.beans.JsonResult;
        import com.soft.demo.beans.Order;
        import com.soft.demo.beans.User;
        import io.swagger.annotations.Api;
        import io.swagger.annotations.ApiOperation;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.jdbc.core.BeanPropertyRowMapper;
        import org.springframework.jdbc.core.JdbcTemplate;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

@Api(tags = "订单管理接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JdbcTemplate jdbc;

    @ApiOperation(value = "创建订单", notes = "创建一个新的订单,创建成功会返回订单的所有信息,传json格式，不需要id，creation_time和paymentt_time")
    @PostMapping("/create")
    public JsonResult<Order> createOrder(@RequestBody Order order) {
        try {
            java.sql.Timestamp creationTime = new java.sql.Timestamp(System.currentTimeMillis());
            String sql = "insert into `order` (buyer_id, seller_id, product_id, order_status, total_price, payment_method) values (?,?,?,?,?,?);";
            jdbc.update(sql, order.getBuyerId(),order.getSellerId(),order.getProductId(),"PENGDING",order.getTotalPrice(),order.getPaymentMethod());
            order = jdbc.queryForObject("SELECT * FROM `order` WHERE id = (SELECT LAST_INSERT_ID());", new BeanPropertyRowMapper<>(Order.class));
            return new JsonResult<>(order);
        } catch (Exception e) {
            log.error("订单创建失败: " + e.getMessage());
            return new JsonResult<>(201,"订单创建失败: " + e.getMessage());
        }
    }


    @ApiOperation(value = "删除订单", notes = "根据订单ID删除订单,不需要json格式，只穿orderId")
    @GetMapping("/delete")
    public JsonResult<String> deleteOrder(@RequestParam("orderId") Long orderId) {
        try {
            String sql = "DELETE FROM `order` WHERE id = ?";
            int rows = jdbc.update(sql, orderId);
            if (rows > 0) {
                return new JsonResult<>();
            } else {
                return new JsonResult<>(201,"订单未找到");
            }
        } catch (Exception e) {
            log.error("订单删除失败: " + e.getMessage());
            return new JsonResult<>(201,"订单删除失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "更新订单", notes = "更新订单信息,需要id，buyer_id，seller_id，product_id，order_status，total_price，payment_method")
    @PostMapping("/update")
    public JsonResult<String> updateOrder(@RequestBody Order order) {
        try {
            String sql = "update `order` set buyer_id = ?, seller_id=?,product_id=?,order_status=?,total_price=?,payment_method=? where id = ?;";
            log.info(String.valueOf(sql));
            int rows = jdbc.update(sql,order.getBuyerId(),order.getSellerId(),order.getProductId(),order.getOrderStatus(),order.getTotalPrice(),order.getPaymentMethod(),order.getId());
            if (rows > 0) {
                return new JsonResult<>(200, "订单更新成功");
            } else {
                return new JsonResult<>(404, "订单未找到");
            }
        } catch (Exception e) {
            log.error("订单更新失败: " + e.getMessage());
            return new JsonResult<>(500, "订单更新失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "查询订单", notes = "根据订单ID查询订单详情")
    @GetMapping("/get")
    public JsonResult<Order> getOrder(@RequestParam("orderId") Long orderId) {
        try {
            String sql = "SELECT * FROM `order` WHERE id = ?";
            Order order = jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Order.class), orderId);
            return new JsonResult<>(order);
        } catch (Exception e) {
            log.error("订单查询失败: " + e.getMessage());
            return new JsonResult<>(201,"订单查询失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "查询用户所有订单", notes = "根据用户ID查询用户所有订单,只需要传用户的id")
    @GetMapping("/list")
    public JsonResult<List<Order>> listUserOrders(Long buyer_id) {
        try {
            String sql = "SELECT * FROM `order` WHERE buyer_id = ?";
            List<Order> orders = jdbc.query(sql, new BeanPropertyRowMapper<>(Order.class), buyer_id);
            return new JsonResult<>(orders);
        } catch (Exception e) {
            log.error("用户订单查询失败: " + e.getMessage());
            return new JsonResult<>(201,"用户订单查询失败: " + e.getMessage());
        }
    }

}

