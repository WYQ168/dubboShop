package com.hgz.api;

public interface IMailService {

    public void SendSimpleMail(String to,String subject,String content);

    public void SendHTMLMail(String to,String subject,String content);

}
