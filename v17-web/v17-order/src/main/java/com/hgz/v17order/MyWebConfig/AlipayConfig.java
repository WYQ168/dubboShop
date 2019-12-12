package com.hgz.v17order.MyWebConfig;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @Bean
    public AlipayClient alipayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016101600701259",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCyE1hBqJmt5kpcsQSgdvXV8mHp/TiguDGxehMpKMQyWkjtx+0oCGb88Fewwlu+yeeOrJcFFU9pExN2rgT4m0L6mJhlTNP3m8o3gGZhmMpQHIHL9A8YwkhcXw1MBhAVuZOVNof1wCzfY0gXg1IlvKUR0QZooR8p3FnFaA4kWZlsc0NXPzLslEKbKkUe8ISzAPbaRYVChUGUH9qtlE+F+hOW9EG1a+Bcd2fzRRqfrLex6YIB3MZlD1o3TXUcMhsTnGOloQS96U3bc3rJOMXXd8lcSZE0ZqvQ3EWdCynWJ2CAEA1Sigjie5aCjRIEBbwK3jyHEZScSPhY0Mp1ISEVmLu/AgMBAAECggEADXBsTrg8sOUaY5hwcvPHZdgfD5uutGJyTaSa4ZZUCEDMFDXsbgMEUr6Iv6xLZEC0mw3nb9jF33YzF9fWRDTjfyoZBjwgGyXEwx+Lmn3/dsBQA2Zt5T50W5RI2p3yhNE2lCJDbIaOXGWPwUaF4pc0Ff4ib68JPQ2GMOHDf3bLn2WKzg9p4oLBsrwYhbFTzbva48MtOdvr9bUdPgXi3VY3R0Jw3xziZSZpa4WhIoiTd6YmkTQqT6Vif/7UMuXdfoSf1vK45vn6RlGlgoRZFgPVu1tZ96QQl7i689BozX65sZmtcoA4AQR64QxAB5E/FcZszh6OWCn3jcspKDsmUqL8YQKBgQD+F1yXjsgKpMlQqrbfkuu3q09O2YKOCPTFvrhurCFUApzEMq/ToWQX/NupVMbNLP6dhI2DQUVx12PwsWhZMvll5iWDkJU0uCvlwJkqKkJADHi8UzxmIPvj13b7daDyqs+4eLXszxSsDRwll8KqHcBA7LIFh9N7LTEtPhw79E/6RwKBgQCzacx09xMeG/dnrjP6RAV61GFNcOwKT5cAeyu/jvJ+FGh135pUDYmamjn/T5JGBHizdJOBd6dtvOf0VZBI2FlfE7eNguRw591HLgA2AM3TDvUZC+qYuDFm0OU0LdNiiF62Jgj7MlQvOBmaIrK7b5M9GgtFcPzns+txj9JZfsv2yQKBgGvAXHOpCQv6daTPwF1vYKay7x7w/nxdL+/rwSqttDR8Jx7Mn+OPPXMut7hHS0cXHaKO47QVqe1s+eTIVwf8peaggqgYWC3TLEpyMoG/Cfk/ULQMWEC3HBqy9LIOYAKIjS4dgoi6CHot1RHcGaEqnKzrs1LFFtnVgFU/UZnbMU9hAoGBAJSlQWpfEo3NqetS32moVcnwkDVzK31Xd8pEQnjRXIOtMOcIPXsKREm9IfQv3wb3yXEVUujfU+6Nprg+8+4GofUuBtPN8KrC0AibSj1z6RPGvrbjspiZcwS2alAS76IhTicn2CUXC7ybhEq6GM9Gn/ztyQzdkgv+e0F5YVQYw7BRAoGAORVYLqw1iU0AqJ9HPSHgNW7x3X2xXIKSaa/Yn2tPNjOyqC/fVRIS8BHMYtDHYsoMjuUA/vTBhi8id8Cul5WQpjBOz4nOnDIO8c0qxspIR8G/yP5MR2c+kyHoWRrQa0bKGlVMu8lg/g4CblD33s12xft3NTPHxZ/eRPR4FYKE6ag=",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAju5IC2OsH87rc9kcE++NuUgpW1tc/hmvXQO+uM5EfHOnJ/905uQcUpJ3uPenh4sQz3HBy7aYEmPhvE4LwKu0WyFuWjXad8syHBWVaHD4p8Sgxx5o6rAjJ6IakK2M+ZQ5z7xrgRp0jRple6+4a4YkaYt89fwpbBj+NjCGRIIsjZYfUF1tQl/FEMU8jc6UkSjCWGSWsK367pffiSgiJeRZd+77rakI1M3ZXQSb6hS4XFwgQZJpPcTAwOIDWje3rinjiK/4D2F0CcTKPcbK2GHuxZqHhaHCvc3NzJtC4rqEaHVytLFTbRWpVyJXuZ0pD8YchzJ2UTXh3T0bxePQ76Bq8QIDAQAB",
                "RSA2");
        return alipayClient;
    }

}
