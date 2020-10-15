package com.weiqing.noctorrosmssend.configuration;

import com.weiqing.noctorrosmssend.core.MessageQueue;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 11:57 AM
 */
@Configuration
public class MessageQueueConfig {

    public MessageQueue messageQueueMap() {
        return new MessageQueue();
    }
}
