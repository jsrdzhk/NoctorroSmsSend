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
    private int index;
    private int item;
    private int count;
    private int waitTime;
    private int n1;
    private int n2;
    private int n3;
    private int n4;
    private int n5;
    private String taskName;
    private String timeE;
    private String scode;
    private String netflow;
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;
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