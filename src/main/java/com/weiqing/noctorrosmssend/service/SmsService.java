package com.weiqing.noctorrosmssend.service;

import com.weiqing.noctorrosmssend.configuration.MessageQueueConfig;
import com.weiqing.noctorrosmssend.configuration.constant.SmsCard;
import com.weiqing.noctorrosmssend.configuration.constant.SourceType;
import com.weiqing.noctorrosmssend.entity.dto.sms.TSms;
import com.weiqing.noctorrosmssend.entity.service.BaseData;
import com.weiqing.noctorrosmssend.entity.service.SmsData;
import com.weiqing.noctorrosmssend.repository.SmsRepository;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:49 AM
 */
@Service
@Slf4j
public class SmsService implements Runnable{
    @Autowired
    private SmsCard smsCard;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private MessageQueueConfig messageQueueConfig;

    public boolean sendSms(String content) throws SQLException {
        BaseData baseData=(BaseData) SmsData.builder().content(content).build();
        return messageQueueConfig.messageQueueMap().pushMessage(SourceType.SMS,baseData);

    }

    @Override
    public void run() {
        try {
            while (true){
                try {
                    SmsData smsData=(SmsData)messageQueueConfig.messageQueueMap().popMessageWait(SourceType.SMS);
                    TSms tSms=TSms.builder().
                            comPort(smsCard.getComPort()).
                            imsi(smsCard.getImsi()).
                            iccid(smsCard.getIccid()).
                            sendPhoneNumber(smsCard.getSendNum()).
                            receivePhoneNumber(smsCard.getRcvNum()).
                            type(smsCard.getType()).
                            content(smsData.getContent()).
                            time(new Date()).
                            build();
                    smsRepository.sendSms(tSms);
                } catch (Exception e) {
                    if (e instanceof InterruptedException){
                        throw e;
                    }else {
                        log.error(e.getMessage());
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
