package com.hgz.api;

import com.hgz.api.pojo.SMSResponse;

public interface ISMS {

    public SMSResponse sendCodeMessage(String phone, String code);
}
