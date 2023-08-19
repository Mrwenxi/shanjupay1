package com.shanjupay.merchant.common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/** @Author wen
 * @Description //TODO 
 * @Date 10:07 2023/8/19
 * @Param 
 * @return 
 **/
public class QiniuUtils {


    public static void  upload2qiniu(String accessKey,String secretKey,String bucket,
                                     byte[] bytes,String fileName) throws RuntimeException{

        //构造一个带指定 Region 对象的配置类，指定存储区域，和存储空间选择的区域一致
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作 为文件名
        String key = fileName;
        try {

            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            //认证通过后得到token（令牌）
            String upToken = auth.uploadToken(bucket);
            try {
                //上传文件,参数：字节数组，key，token令牌
                //key: 建议我们自已生成一个不重复的名称
                Response response = uploadManager.put(bytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
//                LOGGER.error("上传文件到七牛：{}",ex.getMessage());
                try {
//                    LOGGER.error(r.bodyString());
                } catch (Exception ex2) {
                    //ignore
                }
                throw new RuntimeException(r.bodyString());
            }
        } catch (Exception ex) {
//            LOGGER.error("上传文件到七牛：{}",ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
    //测试文件上传
    private static void testUpload() {
        //构造一个带指定 Region 对象的配置类，指定存储区域，和存储空间选择的区域一致
        Configuration cfg = new Configuration(Region.huanan());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "-E37WKbM3oIC9icSGjf-skIF2emTQU4uO0vZByND";
        String secretKey = "NKtxZ4KD_AWb4s1n3ADoDjHzMrEO3JV2fp-jB0KZ";
        String bucket = "shanjupay-xiwen";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString()+".png";
        FileInputStream fileInputStream = null;
        try {

            String filePath = "D:\\头像图片\\1 (12).png";
            fileInputStream = new FileInputStream(new File(filePath));
            //得到本地文件的字节数组
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            //认证通过后得到token（令牌）
            String upToken = auth.uploadToken(bucket);
            try {
                //上传文件,参数：字节数组，key，token令牌
                //key: 建议我们自已生成一个不重复的名称
                Response response = uploadManager.put(bytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            //ignore
        }

    }

/*    private static void getdownloadurl() throws UnsupportedEncodingException {
        String fileName = "283d041b-2a49-4a04-8409-516043804870.png";
        String domainOfBucket = "http://q2cdf3ibb.bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        String accessKey = "vG3krn6ZU9j_KCJEsOW-76A3RxEbgCkN6tkTDOf_";
        String secretKey = "p04hR8a1151UXAb0K3GpYznPxAGuSj_9LbegmJuS";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
    }*/

    public static void main(String[] args) throws UnsupportedEncodingException {
        //上传测试
        //QiniuUtils.getdownloadurl();
        //testUpload();
    }
}
