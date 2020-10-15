package com.weiqing.noctorrosmssend.entity.api.request;

import lombok.Data;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:48 AM
 */
@Data
public class SmsUpload {
    private String content;
    private String phoneNumber;
    private String brand;
    private String sendTime;

    private boolean isPhoneNumberValid() {
        for (char ch :
                phoneNumber.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValid() {
        return isPhoneNumberValid();
    }
}