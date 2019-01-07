package com.linlazy.mmorpglintingyang.server.db;

import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author linlazy
 */
@Component
public class DbConnectionPoolUtils {


    public static   Connection getConnection(){
        DataSource dataSource = SpringContextUtil.getApplicationContext().getBean(DataSource.class);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
