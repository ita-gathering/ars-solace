package com.approval.solace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Slf4j
@Component
public class SolaceErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable throwable) {
        try {
            SolaceBrokerException error = SolaceBrokerException.getInstance(throwable);
            if (error != null) {
                log.warn(error.toString());
            }
        } catch (Exception e) {
            log.error("[Solace-Error] Meet Exception during exception handling", e);
        }
    }
}
