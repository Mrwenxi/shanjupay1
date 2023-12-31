package com.shanjupay.merchant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.common.util.EncryptUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenTemp {

    @Reference
    MerchantService merchantService;

    @Test
    public void createTestToken() {
        Long merchantId = 1692365955852623874L;//填写用于测试的商户id
        System.out.println("------");
        System.out.println("------");
        System.out.println("------");
        System.out.println("------");
        System.out.println("------");
        System.out.println("------");
        System.out.println("------");

        MerchantDTO merchantDTO = merchantService.querymerchantbyid(merchantId);


        System.out.println("==============");
        System.out.println("==============");
        System.out.println("==============");
        System.out.println("==============");
        JSONObject token = new JSONObject();
        token.put("mobile", merchantDTO.getMobile());
        token.put("user_name", merchantDTO.getUsername());
        token.put("merchantId", merchantId);

        String jwt_token = "Bearer " + EncryptUtil.encodeBase64(JSON.toJSONString(token).getBytes());
        System.out.println(jwt_token);
    }
}
