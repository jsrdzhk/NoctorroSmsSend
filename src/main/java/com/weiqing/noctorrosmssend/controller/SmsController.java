package com.weiqing.noctorrosmssend.controller;

import com.weiqing.noctorrosmssend.entity.api.response.ResponseMessage;
import com.weiqing.noctorrosmssend.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:44 AM
 */
@RestController
@Slf4j
public class SmsController {
    @Autowired
    private SmsService smsService;

    @PostMapping("/modem_pool/send_sms")
    public ResponseMessage sendSms(String content, String phoneNum) {
        ResponseMessage responseMessage = new ResponseMessage();
        if (smsService.sendSms(content, phoneNum)) {
            responseMessage.setCode(0);
            responseMessage.setMessage("success");
        } else {
            responseMessage.setCode(-1);
            responseMessage.setMessage("failed");
        }
        return responseMessage;
    }
}
