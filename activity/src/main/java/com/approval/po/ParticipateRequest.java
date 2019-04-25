package com.approval.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Data
public class ParticipateRequest {
    @Id
    private String id;
    private String activityId;
    private String activityName;
    private String userName;
    private String status;
    @CreatedDate
    private LocalDateTime createTime;

    public ParticipateRequest(String activityId, String activityName, String userName, String status) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.userName = userName;
        this.status = status;
    }
}
