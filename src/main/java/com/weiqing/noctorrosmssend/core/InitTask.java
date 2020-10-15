package com.weiqing.noctorrosmssend.core;

import com.weiqing.noctorrosmssend.service.SmsService;
import com.weiqing.noctorrosmssend.util.SpringContextUtils;
import com.weiqing.noctorrosmssend.util.ThreadManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 11:27 AM
 */
@Component
@Slf4j
public class InitTask {
    public void startThread() {
        setAnalyzerThread("main-send-sms", 1, SpringContextUtils.getBean(SmsService.class));
    }

    private void setAnalyzerThread(String threadName, int poolSize, Runnable command) {
        ThreadPoolExecutor thread = ThreadManager.generateThreadPoolProxy(poolSize, poolSize + 10, 1000, threadName);
        for (int i = 0; i < poolSize; i++) {
            thread.execute(command);
        }
    }
}
