package com.hgz.v17smsservice;

import com.hgz.api.ISMS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SmsServiceApplicationTests {

    @Autowired
    private ISMS sms;

    @Test
    public void contextLoads() {
            sms.sendCodeMessage("17770142663","9999");
    }

}
