package com.weiqing.noctorrosmssend.entity.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 11:42 AM
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class SmsData extends BaseData {
    String content;
}
