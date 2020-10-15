package com.weiqing.noctorrosmssend.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:06 AM
 */
@Slf4j
public class MsAccessUtil {
    public static Connection connectAccessDatabase(String dbPath) throws SQLException {
        Connection connection = DriverManager.getConnection(String.format("jdbc:ucanaccess://%s", dbPath), "root", "gd2013");
        if (connection == null) {
            log.error("{} connect failed", dbPath);
        }
        return connection;
    }
}