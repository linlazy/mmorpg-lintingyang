package com.linlazy.mmorpglintingyang.module.email.dao;

import com.linlazy.mmorpglintingyang.module.email.entity.Email;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailDao {

    @Insert({"insert into email (emailId,sourceId,receiver,titleArgs,contentArgs,templateId,rewards)",
            "values(#{emailId},#{sourceId},#{receiver},#{titleArgs},#{contentArgs},#{templateId},#{rewards})"})
    void addEmail(Email email);
}
