package com.linlazy.mmorpg.module.email.dao;

import com.linlazy.mmorpg.module.email.entity.Email;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author linlazy
 */
@Mapper
public interface EmailDao {

    /**
     * 增加邮件
     * @param email 邮件信息
     */
    @Insert({"insert into email (emailId,sourceId,receiver,titleArgs,contentArgs,templateId,rewards)",
            "values(#{emailId},#{sourceId},#{receiver},#{titleArgs},#{contentArgs},#{templateId},#{rewards})"})
    void addEmail(Email email);
}
