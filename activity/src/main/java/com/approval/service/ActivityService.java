package com.approval.service;

import com.approval.dto.AcitivitySearchCriteria;
import com.approval.dto.ActivityDto;
import com.approval.dto.UserDto;
import com.approval.po.Activity;
import com.approval.po.ParticipateSituation;
import com.approval.po.User;
import com.approval.repository.ActivityRepository;
import com.approval.repository.UserRepository;
import com.approval.solace.MessageSender;
import com.approval.utils.WrappedBeanCopier;
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

    public List<ActivityDto> getActivityByCriteria(AcitivitySearchCriteria searchCriteria) {
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
        List<ActivityDto> activityDtos = WrappedBeanCopier.copyPropertiesOfList(activities, ActivityDto.class);
        activityDtos.forEach(activityDto -> {
            List<UserDto> userDtos = WrappedBeanCopier.copyPropertiesOfList(activityDto.getParticipants(), UserDto.class);
            activityDto.setParticipants(userDtos);
        });
        return activityDtos;
    }

    public String participateActivity(String activityId, String username, String awards) {
        User user = userRepository.findByUserName(username);
        if (Objects.isNull(user)) {
            return "can not find user";
        }
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (Objects.isNull(activity)) {
            return "can not find activity";
        }
        List<ParticipateSituation> existedParticipateSituation = activity.getParticipateSituation();
        ParticipateSituation participateSituation = new ParticipateSituation(user, awards, PENDING);
        if (existedParticipateSituation != null) {
            boolean hasParticipate = existedParticipateSituation.stream()
                    .anyMatch(situation -> situation.getParticipant().getUserName().equals(user.getUserName()));
            if (hasParticipate) {
                return "has already participate";
            }
            existedParticipateSituation.add(participateSituation);
        }
        else {
            List<ParticipateSituation> participateSituations = new ArrayList<>();
            participateSituations.add(participateSituation);
            activity.setParticipateSituation(participateSituations);
        }
        activityRepository.save(activity);
        messageSender.sendMessageToTopic(OUTPUT_TOPIC, "send message");
        return "";
    }
}
