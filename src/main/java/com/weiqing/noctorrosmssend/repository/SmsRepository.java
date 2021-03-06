package com.weiqing.noctorrosmssend.repository;

import com.weiqing.noctorrosmssend.entity.dto.sms.TSms;
import com.weiqing.noctorrosmssend.util.DateUtil;
import com.weiqing.noctorrosmssend.util.MsAccessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:04 AM
 */
@Repository
@Slf4j
public class SmsRepository {
    private Connection smsConnection;

    @Value("${msaccess.sms}")
    private String smsDbPath;

    @Value("${date-fmt}")
    private String dateFmt;

    @PostConstruct
    private void init() throws SQLException {
        smsConnection = MsAccessUtil.connectAccessDatabase(smsDbPath);
    }

    @PreDestroy
    private void destroy() throws SQLException {
        smsConnection.close();
    }

    public List<TSms> getSms(String sendTime) throws SQLException {
        Statement statement = smsConnection.createStatement();
        String sql = String.format("select * from L_SMS where L_SMS.time >= '%s';", sendTime);
        ResultSet resultSet = statement.executeQuery(sql);
        List<TSms> tSmsList = new ArrayList<>();
        while (resultSet.next()) {
            TSms tSms = TSms.builder().
                    comPort(resultSet.getInt("pcui")).
                    content(resultSet.getString("content")).
                    imsi(resultSet.getString("imsi")).
                    iccid(resultSet.getString("iccid")).
                    sendPhoneNumber(resultSet.getString("number")).
                    receivePhoneNumber(resultSet.getString("simnum")).
                    type(resultSet.getInt("type")).
                    build();
            String timeStr = resultSet.getString("time");
            try {
                Date dateTime = DateUtil.parse(timeStr, dateFmt);
                tSms.setTime(dateTime);
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
            tSmsList.add(tSms);
        }
        return tSmsList;
    }

    public boolean sendSms(TSms tSms) throws SQLException {
        Statement statement = smsConnection.createStatement();
        String currentTime = DateUtil.format(tSms.getTime(), dateFmt);
        String sql = String.format("insert into L_SMS (pcui,content,imsi,iccid,number,simnum,type,time,index,item,count,waittime,n1,n2,n3,n4,n5,s2) " +
                        "values (%d,'%s','%s','%s','%s','%s',%d,'%s'," +
                        "-1,0,1,0,-1,-1,-1,-1,-1," +
                        "'0891683110304105F0240DA0019630337427F30008020101418172233A30108DA359346761301160A876849A8C8BC17801662F003600350034003430025982975E672C4EBA64CD4F5CFF0C8BF75FFD7565672C77ED4FE1');",
                tSms.getComPort(),
                tSms.getContent(),
                tSms.getImsi(),
                tSms.getIccid(),
                tSms.getSendPhoneNumber(),
                tSms.getReceivePhoneNumber(),
                tSms.getType(),
                currentTime);
        return statement.execute(sql);
    }
}
