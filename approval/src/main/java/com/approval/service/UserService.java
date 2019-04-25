package com.approval.service;

import com.approval.dto.ActivityDto;
import com.approval.dto.UserDto;
import com.approval.po.Activity;
import com.approval.po.User;
import com.approval.repository.ActivityRepository;
import com.approval.repository.UserRepository;
import com.approval.utils.WrappedBeanCopier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Created by guowanyi on 2019/3/12.
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private ActivityRepository activityRepository;

    public User getUserByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User deleteUser(String userId){
        User user = userRepository.findById(userId).orElse(null);
        if(Objects.isNull(user)){
            return null;
        }
        userRepository.delete(user);
        return user;
    }

    public List<ActivityDto> getActivitiesByUserName(String userName) {
        List<Activity> activities = activityRepository.findAllByUserName(userName);
        List<ActivityDto> activityDtos = WrappedBeanCopier.copyPropertiesOfList(activities, ActivityDto.class);
        activityDtos.forEach(activityDto -> {
            List<UserDto> userDtos = WrappedBeanCopier.copyPropertiesOfList(activityDto.getParticipants(), UserDto.class);
            activityDto.setParticipants(userDtos);
        });
        return activityDtos;
    }
}
