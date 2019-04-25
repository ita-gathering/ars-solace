package com.approval.solace;

import lombok.Data;

import javax.jms.TextMessage;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Data
public class SolaceBrokerException extends Exception {

    private final transient TextMessage originMessage;
    private Long elapsedTime;
    public SolaceBrokerException(Throwable t, TextMessage originMessage) {
        super(t);
        this.originMessage = originMessage;
    }

    public static SolaceBrokerException getInstance(Throwable t) {
        if (t == null) {
            return null;
        }

        if (t instanceof SolaceBrokerException) {
            return (SolaceBrokerException) t;
        }

        if (t.getCause() instanceof SolaceBrokerException) {
            return (SolaceBrokerException) t.getCause();
        }

        return getInstance(t.getCause());
    }


}
