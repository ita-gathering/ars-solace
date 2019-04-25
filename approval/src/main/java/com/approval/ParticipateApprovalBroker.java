package com.approval;

import com.approval.po.Activity;
import com.approval.po.ApprovalTask;
import com.approval.po.ParticipateRequest;
import com.approval.po.User;
import com.approval.repository.ActivityRepository;
import com.approval.repository.ParticipateRequestRepository;
import com.approval.repository.UserRepository;
import com.approval.solace.MessageSender;
import com.approval.solace.SolaceBrokerException;
import com.approval.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */

@Component
@Slf4j
public class ParticipateApprovalBroker {
    public static final String PASS = "pass";
    public static final String REJECT = "reject";
    private final String LISTENED_QUEUE = "ARS/ACTIVITY/EVT";
    private static final String DEFAULT_CONCURRENCY = "10";
    private static final String QUEUE_LISTENER_FACTORY = "queueListenerFactory";
    private static final String OUTPUT_TOPIC = "ARS/APPROVAL";

    @Resource
    private MessageSender messageSender;
    @Resource
    private ActivityRepository activityRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private ParticipateRequestRepository participateRequestRepository;

    //    @Retryable(value = {SolaceBrokerException.class}, backoff = @Backoff(random = true, multiplier = 0))
    @JmsListener(destination = LISTENED_QUEUE, containerFactory = QUEUE_LISTENER_FACTORY, concurrency = DEFAULT_CONCURRENCY)
    public void processMessage(TextMessage originalMessage) throws Exception {
        ParticipateRequest participateRequest = JsonUtils.jsonToObject(originalMessage.getText(), ParticipateRequest.class);
        processApproval(participateRequest);
    }

@Transactional
    public void processApproval(ParticipateRequest participateRequest) {
        //todo:approval logic
        Activity activity = activityRepository.findById(participateRequest.getActivityId()).orElse(null);
//        ParticipateRequest existedParticipateRequest = participateRequestRepository.findByActivityIdAndUserName(participateRequest.getActivityId(),participateRequest.getUserName());
        ParticipateRequest existedParticipateRequest = participateRequestRepository.findById(participateRequest.getId()).orElse(null);
        if (activity.getRemainCount() > 0) {
            existedParticipateRequest.setStatus(PASS);
            activity.setRemainCount(activity.getRemainCount() - 1);
            User participant = userRepository.findByUserName(existedParticipateRequest.getUserName());
            if (activity.getParticipants()==null){
                List<User> users = new ArrayList<>();
                users.add(participant);
                activity.setParticipants(users);
            }else{
                activity.getParticipants().add(participant);
            }
            activityRepository.save(activity);
        }else {
            existedParticipateRequest.setStatus(REJECT);
        }
        participateRequestRepository.save(existedParticipateRequest);
    }
}
