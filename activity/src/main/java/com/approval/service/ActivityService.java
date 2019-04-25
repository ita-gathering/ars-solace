package com.approval.service;

import com.approval.dto.AcitivitySearchCriteria;
import com.approval.po.*;
import com.approval.repository.ActivityRepository;
import com.approval.repository.ParticipateRequestRepository;
import com.approval.repository.UserRepository;
import com.approval.solace.MessageSender;
import com.approval.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Service
public class ActivityService {

    public static final String PENDING = "pending";
    @Resource
    private ActivityRepository activityRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private ParticipateRequestRepository participateRequestRepository;
    @Resource
    private MessageSender messageSender;
    private static final String OUTPUT_TOPIC = "ARS/ACTIVITY";


    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity getActivityById(String activityId) {
        return activityRepository.findById(activityId).orElse(null);
    }

    public boolean updateActivity(String activityId, Activity newActivity) {
        Activity existedActivity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(existedActivity)) {
            return false;
        }
        existedActivity.setTitle(newActivity.getTitle());
        existedActivity.setContent(newActivity.getContent());
        existedActivity.setStartDate(newActivity.getStartDate());
        existedActivity.setClosingDate(newActivity.getClosingDate());
        activityRepository.save(existedActivity);
        return true;
    }

    public Activity deleteActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(activity)) {
            return null;
        }
        activityRepository.delete(activity);
        return activity;
    }

    public List<Activity> getActivityByCriteria(AcitivitySearchCriteria searchCriteria) {
        List<Activity> activities;
        if (searchCriteria.getAuthor() != null) {
            if (searchCriteria.getTitle() == null) {
                activities = activityRepository.findAllByAuthor(searchCriteria.getAuthor());
            } else {
                activities = activityRepository.findAllByTitleLikeAndAuthor(searchCriteria.getTitle(), searchCriteria.getAuthor());
            }
        } else {
            if (searchCriteria.getTitle() != null) {
                activities = activityRepository.findAllByTitleLike(searchCriteria.getTitle());
            } else {
                activities = activityRepository.findAll();
            }
        }
        return activities;
    }

    public String participateActivity(String activityId, String username, String awards) throws JsonProcessingException {
        User user = userRepository.findByUserName(username);
        if (Objects.isNull(user)) {
            return "can not find user";
        }
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(activity)) {
            return "can not find activity";
        }
        if (activity.getParticipants() != null) {
            boolean hasParticipate = activity.getParticipants().stream()
                    .anyMatch(participant -> participant.getUserName().equals(user.getUserName()));
            if (hasParticipate) {
                return "has already participate";
            }
        }
        ParticipateRequest participateRequest = new ParticipateRequest(activityId,activity.getTitle(),username, PENDING);
        participateRequest = participateRequestRepository.save(participateRequest);
        messageSender.sendMessageToTopic(OUTPUT_TOPIC, JsonUtils.objectToJson(participateRequest));
        return "";
    }

    public List<ParticipateRequest> getAllRequestByUserName(String userName) {
        return participateRequestRepository.findAllByUserName(userName);
    }

//    public void updateParticipateStatus(ApprovalTask approvalTask) {
//        Activity activity = activityRepository.findById(approvalTask.getActivityId()).orElse(null);
//        ParticipateSituation participateSituation1 = activity.getParticipateSituation().stream()
//                .filter(participateSituation -> participateSituation.getParticipant().getUserName().equals(approvalTask.getParticipateSituation().getParticipant().getUserName()))
//                .findFirst().orElse(null);
//        participateSituation1.setStatus(approvalTask.getParticipateSituation().getStatus());
//        activityRepository.save(activity);
//    }
}
