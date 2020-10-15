package com.weiqing.noctorrosmssend.configuration.constant;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 11:58 AM
 */
public enum SourceType {
    //sms
    SMS(1);
    private final int value;
    SourceType(int value){
        this.value=value;
    }
    public int value() {
        return this.value;
    }
}
