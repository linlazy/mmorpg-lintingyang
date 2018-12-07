package com.linlazy.email.type;


/**
 * 排行版邮件类型
 */
public class RankEmailType extends AbstractEmailType{
    @Override
    protected int emailType() {
        return 1;
    }

    @Override
    public void sendEmail() {

    }
}
