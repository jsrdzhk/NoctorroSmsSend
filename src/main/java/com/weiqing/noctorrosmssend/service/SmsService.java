package com.weiqing.noctorrosmssend.service;

import com.weiqing.noctorrosmssend.configuration.constant.SmsCard;
import com.weiqing.noctorrosmssend.configuration.constant.SourceType;
import com.weiqing.noctorrosmssend.core.MessageQueue;
import com.weiqing.noctorrosmssend.entity.dto.sms.TSms;
import com.weiqing.noctorrosmssend.entity.service.BaseData;
import com.weiqing.noctorrosmssend.entity.service.SmsData;
import com.weiqing.noctorrosmssend.repository.SmsRepository;
import com.weiqing.noctorrosmssend.util.DateUtil;
import com.weiqing.noctorrosmssend.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:49 AM
 */
@Service
@Slf4j
public class SmsService implements Runnable {
    @Autowired
    private SmsCard smsCard;

    @Autowired
    private SmsRepository smsRepository;

    private final MessageQueue sms = new MessageQueue();

    private final Random random = new Random();

    public boolean sendSms(String content, String phoneNum) {
        BaseData baseData = SmsData.builder().content(content).phoneNum(phoneNum).uuid(UUIDUtil.generate32()).build();
        return sms.pushMessage(SourceType.SMS, baseData);
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    SmsData smsData = (SmsData) sms.popMessageWait(SourceType.SMS);
                    TSms tSms = TSms.builder().
                            comPort(smsCard.getComPort()).
                            imsi(smsCard.getImsi()).
                            iccid(smsCard.getIccid()).
                            sendPhoneNumber(smsData.getPhoneNum()).
                            receivePhoneNumber(smsCard.getRcvNum()).
                            type(smsCard.getType()).
                            content(smsData.getContent()).
                            time(DateUtil.getCurrentTimestamp()).
                            build();
                    Thread.sleep(random.nextInt(30) * 1000);
                    smsRepository.sendSms(tSms);
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        throw e;
                    } else {
                        log.error(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
