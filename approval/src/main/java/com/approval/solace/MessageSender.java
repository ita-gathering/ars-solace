package com.approval.solace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Slf4j
@Component
public class MessageSender {

    @Resource
    private JmsTemplate jmsTopicTemplate;

    public void sendMessageCreatorToTopic(String destination, MessageCreator messageCreator) {
        log.debug("Sending event to topic: " + destination);
        jmsTopicTemplate.send(destination, messageCreator);
    }
    public void sendMessageToTopic(String destination, String message) {
        log.debug("Sending event to topic: " + destination);
        jmsTopicTemplate.convertAndSend(destination, message);
    }
}
