package com.alibaba.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class HttpUtil extends HttpCommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 设置连接超时时间,单位毫秒
     */
    private static final int CONNECT_TIMEOUT = 180000;
    /*
     * 从连接池获取到连接的超时,单位毫秒
     * */
    private static final int CONNECTION_REQUEST_TIMEOUT = 180000;
    /*
     * 请求获取数据的超时时间,单位毫秒
     * */
    private static final int SOCKET_TIMEOUT = 180000;
    // 连接主机超时（30s）
    public static final int HTTP_CONNECT_TIMEOUT_30S = 180 * 1000;
    // 从主机读取数据超时（3min）
    public static final int HTTP_READ_TIMEOUT_3MIN = 180 * 1000;


    public static String doProxyPostJson(String weather_url, String json,String headerName,String header) {
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost(proxyHostName, proxyPort, proxyTcp);

        CloseableHttpResponse response = null;
        String resultString = "";
        String paramsString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(weather_url);
            // 设置请求超时 20+10+25=55s 配合业务设置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间,单位毫秒。
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    // 从连接池获取到连接的超时,单位毫秒。
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    // 请求获取数据的超时时间,单位毫秒;
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    //设置代理
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            if (!StringUtils.isEmpty(header)) {
                httpPost.setHeader(headerName, header);
            }
            StringEntity stringEntityentity = new StringEntity(json, StandardCharsets.UTF_8);//解决中文乱码问题
            stringEntityentity.setContentEncoding(StandardCharsets.UTF_8.toString());
            stringEntityentity.setContentType("application/json");
            httpPost.setEntity(stringEntityentity);

            //log参数
//            paramsString = JSONObject.toJSONString(paramsMap);
            // 执行http请求
            BasicResponseHandler handler = new BasicResponseHandler();
            resultString = httpClient.execute(httpPost, handler);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",e.getMessage(), weather_url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, httpClient, weather_url, paramsString);
        }
        return resultString;
    }

    public static String doProxyPostJson(String weather_url, String json, Map<String, String> headers, String type) {
        logger.info("POST请求 doProxyPostJson url:{}", weather_url);
        logger.info("POST请求 doProxyPostJson body:{}", json);
        logger.info("POST请求 doProxyPostJson headers:{}", headers);
        // 创建HttpClientBuilder
        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        String resultString = "";
        String paramsString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(weather_url);
            // 设置请求超时 20+10+25=55s 配合业务设置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间,单位毫秒。
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    // 从连接池获取到连接的超时,单位毫秒。
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    // 请求获取数据的超时时间,单位毫秒;
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    //设置代理
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            for (Map.Entry entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
            // 创建请求内容
            StringEntity stringEntityentity = new StringEntity(json, StandardCharsets.UTF_8);//解决中文乱码问题
            stringEntityentity.setContentEncoding(StandardCharsets.UTF_8.toString());
            stringEntityentity.setContentType("application/json");
            httpPost.setEntity(stringEntityentity);

            // 执行http请求
            BasicResponseHandler handler = new BasicResponseHandler();
            resultString = httpClient.execute(httpPost, handler);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    type, e.getMessage(), weather_url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, httpClient, weather_url, paramsString);
        }
        logger.info("POST请求 doProxyPostJson result:{}", resultString);
        return resultString;
    }

    /**
     * 需要设置代理POST请求（带json参数） multipart/form-data
     *
     * @param url 请求地址
     */
    public static String doProxyPostJsonMultipart(String url,
                                         Map<String, String> paramsMap, String type, Integer userId,String header) {
        // 设置代理IP、端口、协议
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost(proxyHostName, proxyPort, proxyTcp);

        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse response = null;
        String resultString = "";
        String paramsString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 设置请求超时 20+10+25=55s 配合业务设置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间,单位毫秒。
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    // 从连接池获取到连接的超时,单位毫秒。
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    // 请求获取数据的超时时间,单位毫秒;
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    //设置代理
//                    .setProxy(proxy)
                    .build();
            //如果访问一个接口,多少时间内无法返回数据,就直接放弃此次调用。
            httpPost.setConfig(requestConfig);
            //不复用TCP SOCKET
//            httpPost.setHeader("connection", "close");
            httpPost.setHeader("Content-Type", "multipart/form-data");
            if (!StringUtils.isEmpty(header)) {
                httpPost.setHeader("Authorization", header);
            }
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : paramsMap.keySet()) {
                list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
            }
            logger.info("POST请求 commonRequest userId:{},paramsMap:{}", userId, paramsMap);
            // 创建请求内容
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);

            //log参数
            paramsString = JSONObject.toJSONString(paramsMap);
            logger.info("POST请求 commonRequest userId:{},paramsString:{}", userId, paramsString);
            closeableHttpClient = httpClientBuilder.build();
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            // 此句关闭了流
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.error("httplog {}:{} doProxyPostJson occur error:{}, url:{}, proxyHost:{}, proxyPort:{}, originParams:{}",
                    userId, type, e.getMessage(), url, paramsString, e);
            return resultString;
        } finally {
            HttpCommonUtils.closeHttpClientAndResponse(response, closeableHttpClient, url, paramsString);
        }
        return resultString;
    }




    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private static RequestConfig.Builder buildDefaultRequestConfigBuilder() {
        return RequestConfig.custom()
                .setSocketTimeout(HTTP_READ_TIMEOUT_3MIN)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT_30S);
    }
}
