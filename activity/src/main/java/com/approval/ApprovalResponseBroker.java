//package com.approval;
//
//import com.approval.po.ApprovalTask;
//import com.approval.service.ActivityService;
//import com.approval.solace.MessageSender;
//import com.approval.utils.JsonUtils;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.jms.TextMessage;
//
///**
// * @author Ocean Liang
// * @date 4/25/2019
// */
//@Component
//public class ApprovalResponseBroker {
//    private final String LISTENED_QUEUE = "ARS/APPROVAL/EVT";
//    private static final String DEFAULT_CONCURRENCY = "10";
//    private static final String QUEUE_LISTENER_FACTORY = "queueListenerFactory";
//
//    @Resource
//    private MessageSender messageSender;
//    @Resource
//    private ActivityService activityService;
//
//    //    @Retryable(value = {SolaceBrokerException.class}, backoff = @Backoff(random = true, multiplier = 0))
//    @JmsListener(destination = LISTENED_QUEUE, containerFactory = QUEUE_LISTENER_FACTORY, concurrency = DEFAULT_CONCURRENCY)
//    public void processMessage(TextMessage originalMessage) throws Exception {
//        ApprovalTask approvalTask = JsonUtils.jsonToObject(originalMessage.getText(), ApprovalTask.class);
//        activityService.updateParticipateStatus(approvalTask);
//    }
//}
