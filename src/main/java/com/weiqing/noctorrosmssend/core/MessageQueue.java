package com.weiqing.noctorrosmssend.core;


import com.weiqing.noctorrosmssend.configuration.constant.SourceType;
import com.weiqing.noctorrosmssend.entity.service.BaseData;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 11:42 AM
 */
@Slf4j
public class MessageQueue {
    private final Map<SourceType, BlockingQueue<BaseData>> queueMap = new HashMap<>();

    public MessageQueue() {
        queueMap.put(SourceType.SMS, new LinkedBlockingQueue<>());
    }

    public boolean isEmpty(SourceType queueType) {
        return queueMap.get(queueType).isEmpty();
    }

    public synchronized boolean pushMessage(@NonNull SourceType sourceType, @NonNull BaseData data) {
        BlockingQueue<BaseData> targetQueue = queueMap.get(sourceType);
        if (targetQueue == null) {
            return false;
        }
        return targetQueue.offer(data);
    }

    public Object popMessage(@NonNull SourceType sourceType) {
        BlockingQueue<BaseData> queue = queueMap.get(sourceType);
        if (queue == null) {
            return null;
        }
        return queue.poll();
    }

    public Object popMessageWait(@NonNull SourceType sourceType) throws InterruptedException {
        Object obj;
        BlockingQueue<BaseData> queue = queueMap.get(sourceType);
        if (queue == null) {
            throw new NullPointerException(sourceType + " queue is not exist");
        }
        obj = queue.take();
        return obj;
    }

}
