package com.approval.solace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Slf4j
public class ErrorMessageCreator implements MessageCreator {
    private static final List<String> EXCLUDED_HEADERS = Arrays
            .asList("JMSDestination", "JMSReplyTo", "JMSMessageID", "JMSTimestamp", "JMSExpiration",
                    "JMSType");
    private TextMessage originMessage;
    private Throwable error;

    public ErrorMessageCreator(TextMessage solaceMessage, Throwable throwable) {
        this.originMessage = solaceMessage;
        this.error = throwable;
    }


    @Override
    public Message createMessage(Session session) throws JMSException {
        return null;
    }
}
