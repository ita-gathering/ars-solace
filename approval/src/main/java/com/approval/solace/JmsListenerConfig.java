package com.approval.solace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Configuration
@EnableJms
public class JmsListenerConfig {
    @Bean
    public JmsListenerContainerFactory queueListenerFactory(ConnectionFactory connectionFactory, SolaceErrorHandler handler) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(handler);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory topicListenerFactory(ConnectionFactory connectionFactory, SolaceErrorHandler handler) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(handler);
        return factory;
    }

    @Bean
    public JmsTemplate jmsQueueTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTopicTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
}
