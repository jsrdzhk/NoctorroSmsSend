package com.weiqing.noctorrosmssend.configuration.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 11:00 AM
 */
@ConfigurationProperties(prefix = "sms-send")
@Data
@Component
public class SmsCard {
    private int comPort;
    private String imsi;
    private String iccid;
    private String rcvNum;
    private String sendNum;
    private int type;
}
