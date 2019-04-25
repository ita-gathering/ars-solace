package com.approval.controller;

import com.approval.dto.ResponseDto;
import com.approval.po.User;
import com.approval.service.ActivityService;
import com.approval.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by guowanyi on 2019/3/12.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    @PostMapping
    public ResponseDto createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseDto.success(user);
    }

    @GetMapping("/{userName}")
    public ResponseDto getUserByUserName(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);
        if (Objects.isNull(user)) {
            return ResponseDto.fail("can not find user");
        }
        return ResponseDto.fail("can not login, password error");
    }

    @DeleteMapping("/{userId}")
    public ResponseDto deleteUser(@PathVariable String userId) {
        User deletedUser = userService.deleteUser(userId);
        if (Objects.isNull(deletedUser)) {
            return ResponseDto.fail("delete user failed");
        }
        return ResponseDto.success(deletedUser);
    }

    @GetMapping("/{userName}/activity")
    public ResponseDto getUserParticipatedActivitiesByUserName(@PathVariable String userName) {
        return ResponseDto.success(userService.getActivitiesByUserName(userName));
    }
}
