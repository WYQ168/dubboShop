package com.hgz.v17smsservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hgz.api.ISMS;
import com.hgz.api.pojo.SMSResponse;

import java.io.IOException;

@Service
public class SMSServiceImpl implements ISMS {


    @Override
    public SMSResponse sendCodeMessage(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FgrwNeNtBStecYBYMz6", "o66g0xJlcLfUVCWIu3rrlHRFSKMjlc");
        IAcsClient client = new DefaultAcsClient(profile);
        //固定的配置，组合请求的参数
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("SignName", "2号小巫");
        request.putQueryParameter("TemplateCode", "SMS_177257428");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            //发送请求，得到结果
            CommonResponse response = client.getCommonResponse(request);
            //将返回结果的String转换成一个对象
            /*ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getData(),SMSResponse.class);*/

            Gson gson = new Gson();
            gson.fromJson(response.getData(),SMSResponse.class);

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
