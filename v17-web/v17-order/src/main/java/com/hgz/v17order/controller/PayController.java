package com.hgz.v17order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    private AlipayClient alipayClient;

    @RequestMapping("generate")
    public void doPost(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,String orderNo) throws ServletException, IOException {
        //创建一个alipayClient对象
           /* AlipayClient alipayClient = new DefaultAlipayClient(
                    "https://openapi.alipaydev.com/gateway.do",
                    "2016101600701259",
                    "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCyE1hBqJmt5kpcsQSgdvXV8mHp/TiguDGxehMpKMQyWkjtx+0oCGb88Fewwlu+yeeOrJcFFU9pExN2rgT4m0L6mJhlTNP3m8o3gGZhmMpQHIHL9A8YwkhcXw1MBhAVuZOVNof1wCzfY0gXg1IlvKUR0QZooR8p3FnFaA4kWZlsc0NXPzLslEKbKkUe8ISzAPbaRYVChUGUH9qtlE+F+hOW9EG1a+Bcd2fzRRqfrLex6YIB3MZlD1o3TXUcMhsTnGOloQS96U3bc3rJOMXXd8lcSZE0ZqvQ3EWdCynWJ2CAEA1Sigjie5aCjRIEBbwK3jyHEZScSPhY0Mp1ISEVmLu/AgMBAAECggEADXBsTrg8sOUaY5hwcvPHZdgfD5uutGJyTaSa4ZZUCEDMFDXsbgMEUr6Iv6xLZEC0mw3nb9jF33YzF9fWRDTjfyoZBjwgGyXEwx+Lmn3/dsBQA2Zt5T50W5RI2p3yhNE2lCJDbIaOXGWPwUaF4pc0Ff4ib68JPQ2GMOHDf3bLn2WKzg9p4oLBsrwYhbFTzbva48MtOdvr9bUdPgXi3VY3R0Jw3xziZSZpa4WhIoiTd6YmkTQqT6Vif/7UMuXdfoSf1vK45vn6RlGlgoRZFgPVu1tZ96QQl7i689BozX65sZmtcoA4AQR64QxAB5E/FcZszh6OWCn3jcspKDsmUqL8YQKBgQD+F1yXjsgKpMlQqrbfkuu3q09O2YKOCPTFvrhurCFUApzEMq/ToWQX/NupVMbNLP6dhI2DQUVx12PwsWhZMvll5iWDkJU0uCvlwJkqKkJADHi8UzxmIPvj13b7daDyqs+4eLXszxSsDRwll8KqHcBA7LIFh9N7LTEtPhw79E/6RwKBgQCzacx09xMeG/dnrjP6RAV61GFNcOwKT5cAeyu/jvJ+FGh135pUDYmamjn/T5JGBHizdJOBd6dtvOf0VZBI2FlfE7eNguRw591HLgA2AM3TDvUZC+qYuDFm0OU0LdNiiF62Jgj7MlQvOBmaIrK7b5M9GgtFcPzns+txj9JZfsv2yQKBgGvAXHOpCQv6daTPwF1vYKay7x7w/nxdL+/rwSqttDR8Jx7Mn+OPPXMut7hHS0cXHaKO47QVqe1s+eTIVwf8peaggqgYWC3TLEpyMoG/Cfk/ULQMWEC3HBqy9LIOYAKIjS4dgoi6CHot1RHcGaEqnKzrs1LFFtnVgFU/UZnbMU9hAoGBAJSlQWpfEo3NqetS32moVcnwkDVzK31Xd8pEQnjRXIOtMOcIPXsKREm9IfQv3wb3yXEVUujfU+6Nprg+8+4GofUuBtPN8KrC0AibSj1z6RPGvrbjspiZcwS2alAS76IhTicn2CUXC7ybhEq6GM9Gn/ztyQzdkgv+e0F5YVQYw7BRAoGAORVYLqw1iU0AqJ9HPSHgNW7x3X2xXIKSaa/Yn2tPNjOyqC/fVRIS8BHMYtDHYsoMjuUA/vTBhi8id8Cul5WQpjBOz4nOnDIO8c0qxspIR8G/yP5MR2c+kyHoWRrQa0bKGlVMu8lg/g4CblD33s12xft3NTPHxZ/eRPR4FYKE6ag=",
                    "json",
                    "utf-8",
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAju5IC2OsH87rc9kcE++NuUgpW1tc/hmvXQO+uM5EfHOnJ/905uQcUpJ3uPenh4sQz3HBy7aYEmPhvE4LwKu0WyFuWjXad8syHBWVaHD4p8Sgxx5o6rAjJ6IakK2M+ZQ5z7xrgRp0jRple6+4a4YkaYt89fwpbBj+NjCGRIIsjZYfUF1tQl/FEMU8jc6UkSjCWGSWsK367pffiSgiJeRZd+77rakI1M3ZXQSb6hS4XFwgQZJpPcTAwOIDWje3rinjiK/4D2F0CcTKPcbK2GHuxZqHhaHCvc3NzJtC4rqEaHVytLFTbRWpVyJXuZ0pD8YchzJ2UTXh3T0bxePQ76Bq8QIDAQAB",
                    "RSA2"); //获得初始化的AlipayClient*/

        //创建交易请求对象，用于封装业务的相关参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
            alipayRequest.setReturnUrl("http://www.baidu.com");//TODO 页面的跳转，给用户看的
            //需要做内网穿透
            alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//TODO 异步调用，我们来判断是否调用成功
           //设置业务相关的参数
            alipayRequest.setBizContent("{" +
                        "    \"out_trade_no\":\""+orderNo+"\"," +
                       "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                       "    \"total_amount\":9999," +
                        "    \"subject\":\"HuaWei V30 512G\"," +
                        "    \"body\":\"HuaWei V30 512G\"" +
                        "  }");//填充业务参数

        //通过client发送请求
            String form="";
            try {
                   form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
                } catch (AlipayApiException e) {
                    e.printStackTrace();
                }
            httpResponse.setContentType("text/html;charset=utf-8");
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        }

        @RequestMapping("notifyPayResult")
        @ResponseBody
        public void notifyPayResult(HttpServletRequest request) throws AlipayApiException {
            //将异步通知中收到的所有参数都存放到map中
            Map<String, String> paramsMap = new HashMap<>();
            //所有的请求都会封装到request对象中
            Map<String, String[]> sourceMap = request.getParameterMap();
            //sourceMap-->paramsMap
            //values不同，String[]-->String
            Set<Map.Entry<String, String[]>> entries = sourceMap.entrySet();
            for (Map.Entry<String, String[]> entry : entries) {
                String[] values = entry.getValue();
                StringBuilder value = new StringBuilder();
                for (int i = 0; i < values.length - 1; i++) {
                    value.append(values[i]+",");
                }
                value.append(values[values.length -1]);
                paramsMap.put(entry.getKey(),value.toString());
            }

            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap,
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAju5IC2OsH87rc9kcE++NuUgpW1tc/hmvXQO+uM5EfHOnJ/905uQcUpJ3uPenh4sQz3HBy7aYEmPhvE4LwKu0WyFuWjXad8syHBWVaHD4p8Sgxx5o6rAjJ6IakK2M+ZQ5z7xrgRp0jRple6+4a4YkaYt89fwpbBj+NjCGRIIsjZYfUF1tQl/FEMU8jc6UkSjCWGSWsK367pffiSgiJeRZd+77rakI1M3ZXQSb6hS4XFwgQZJpPcTAwOIDWje3rinjiK/4D2F0CcTKPcbK2GHuxZqHhaHCvc3NzJtC4rqEaHVytLFTbRWpVyJXuZ0pD8YchzJ2UTXh3T0bxePQ76Bq8QIDAQAB",
                    "utf-8",
                    "RSA2"); //调用SDK验证签名
            if(signVerified){
                    // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                System.out.println("验签成功");
            }else{
                    // TODO 验签失败则记录异常日志，并在response中返回failure.
                System.out.println("验签失败");
                }
        }

}

