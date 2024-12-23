package com.example.demo;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 笑话接口示例代码
 */
public class Demo95 {
    //接口请求地址
    //按更新时间查询笑话
    public static final String URL_A = "http://v.juhe.cn/joke/content/list.php?key=%s&&time=%d&pagesize=%d";

    //最新笑话
    public static final String URL_B = "http://v.juhe.cn/joke/content/text.php?key=%s&pagesize=%d";

    //随机笑话
    public static final String URL_C = "http://v.juhe.cn/joke/randJoke.php?key=%s";

    //申请接口的请求key
    // TODO: 您需要改为自己的请求key
    public static final String KEY = "1de7214092c7f5d4a2498d6efa32fd8e";


    public static void main(String[] args) {

        // TODO: 日期

        //时间戳
        long time = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        //每页数量
        int pageSize = 2;

        System.out.println("------------按更新时间查询笑话-----------------");
        printA(time, pageSize);
        System.out.println("------------最新笑话-----------------");
        printB(pageSize);
        System.out.println("------------随机笑话-----------------");
        printC();
    }
    /**
     * 随机笑话
     *
     */
    public static void printC() {
        //发送http请求的url
        String url = String.format(URL_C, KEY);
        final String response = doGet(url);
        System.out.println("接口返回：" + response);
        try {
            JSONObject jsonObject = JSONObject.fromObject(response);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                System.out.println("调用接口成功");
                JSONArray result = jsonObject.getJSONArray("result");
                result.stream().map(JSONObject::fromObject).forEach(hour -> {
                    System.out.println("content：" + ((JSONObject) hour).getString("content"));
                    System.out.println("hashId：" + ((JSONObject) hour).getString("hashId"));
                    System.out.println("unixtime：" + ((JSONObject) hour).getString("unixtime"));
                });

            } else {
                System.out.println("调用接口失败：" + jsonObject.getString("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 最新笑话
     *
     * @param pageSize int 每页数量
     */
    public static void printB( int pageSize) {
        //发送http请求的url
        String url = String.format(URL_B, KEY, pageSize);
        final String response = doGet(url);
        System.out.println("接口返回：" + response);
        try {
            JSONObject jsonObject = JSONObject.fromObject(response);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                System.out.println("调用接口成功");
                JSONArray result = jsonObject.getJSONObject("result").getJSONArray("data");
                result.stream().map(JSONObject::fromObject).forEach(hour -> {
                    System.out.println("content：" + ((JSONObject) hour).getString("content"));
                    System.out.println("hashId：" + ((JSONObject) hour).getString("hashId"));
                    System.out.println("unixtime：" + ((JSONObject) hour).getString("unixtime"));
                    System.out.println("updatetime：" + ((JSONObject) hour).getString("updatetime"));
                });

            } else {
                System.out.println("调用接口失败：" + jsonObject.getString("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按更新时间查询笑话
     *
     * @param time     long 时间戳
     * @param pageSize int 每页数量
     */
    public static void printA(long time, int pageSize) {
        //发送http请求的url
        String url = String.format(URL_A, KEY, time, pageSize);

        final String response = doGet(url);
        System.out.println("接口返回：" + response);
        try {
            JSONObject jsonObject = JSONObject.fromObject(response);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                System.out.println("调用接口成功");
                JSONArray result = jsonObject.getJSONObject("result").getJSONArray("data");
                result.stream().map(JSONObject::fromObject).forEach(hour -> {
                    System.out.println("content：" + ((JSONObject) hour).getString("content"));
                    System.out.println("hashId：" + ((JSONObject) hour).getString("hashId"));
                    System.out.println("unixtime：" + ((JSONObject) hour).getString("unixtime"));
                    System.out.println("updatetime：" + ((JSONObject) hour).getString("updatetime"));
                });

            } else {
                System.out.println("调用接口失败：" + jsonObject.getString("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get方式的http请求
     *
     * @param httpUrl 请求地址
     * @return 返回结果
     */
    public static String doGet(String httpUrl) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 封装输入流，并指定字符集
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append(System.getProperty("line.separator"));
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return result;
    }


    /**
     * post方式的http请求
     *
     * @param httpUrl 请求地址
     * @param param   请求参数
     * @return 返回结果
     */
    public static String doPost(String httpUrl, String param) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 通过连接对象获取一个输出流
            outputStream = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            outputStream.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder sbf = new StringBuilder();
                String temp;
                // 循环遍历一行一行读取数据
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append(System.getProperty("line.separator"));
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}

