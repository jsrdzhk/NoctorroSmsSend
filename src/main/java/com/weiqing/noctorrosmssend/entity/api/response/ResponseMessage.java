package com.weiqing.noctorrosmssend.entity.api.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:47 AM
 */
@Data
@JsonAutoDetect
public class ResponseMessage {
    private Integer code = 0;
    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Object obj;

    public ResponseMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseMessage() {
    }
}
