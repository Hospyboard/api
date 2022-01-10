package com.hospyboard.api.notification;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReactiveScheduledPushMessages implements InitializingBean {


    public ReactiveScheduledPushMessages(SimpMessagingTemplate simpMessagingTemplate) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
