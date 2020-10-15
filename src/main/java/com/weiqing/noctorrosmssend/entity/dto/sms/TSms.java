package com.weiqing.noctorrosmssend.entity.dto.sms;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:05 AM
 */
@Data
@SuperBuilder
public class TSms implements Comparable<TSms> {
    private int comPort;
    private String sendPhoneNumber;
    private String receivePhoneNumber;
    private String content;
    private Date time;
    private String imsi;
    private String iccid;
    private int type;

    @Override
    public int compareTo(TSms other) {
        return this.time.compareTo(other.time);
    }
}