package com.hgz.api;

import com.hgz.commons.base.IBaseService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TUser;

public interface IUserService extends IBaseService<TUser> {

    public ResultBean checkUserNameIsExists();

    public ResultBean checkPhoneIsExists();

    public ResultBean checkEmailIsExists();

    public ResultBean generateCode(String Identification);

    ResultBean selectCheckLogin(TUser user);

    ResultBean CheckIsLogin(String uuid);
}
