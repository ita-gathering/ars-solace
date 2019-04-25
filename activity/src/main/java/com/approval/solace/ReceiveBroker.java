package com.approval.solace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.TextMessage;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Component
@Slf4j
public class ReceiveBroker {

    private final String LISTENED_QUEUE = "RECEIVE";
    private static final String DEFAULT_CONCURRENCY = "10";
    private static final String QUEUE_LISTENER_FACTORY = "queueListenerFactory";

    @Resource
    private MessageSender messageSender;

    @Retryable(value = {SolaceBrokerException.class}, backoff = @Backoff(random = true, multiplier = 0))
    @JmsListener(destination = LISTENED_QUEUE, containerFactory = QUEUE_LISTENER_FACTORY, concurrency = DEFAULT_CONCURRENCY)
    public void processMessage(TextMessage originalMessage) throws Exception {
        System.out.println("receive from queue");
        System.out.println(originalMessage.getText());

        messageSender.sendMessageCreatorToTopic("EXCEPTION", new ErrorMessageCreator(originalMessage, new RuntimeException()));


    }
}
