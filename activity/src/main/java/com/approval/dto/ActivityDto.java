package com.approval.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ocean Liang
 * @date 3/13/2019
 */
@Data
public class ActivityDto {
    private String id;
    private String author;
    private String title;
    private String content;
    private List<UserDto> participants;
    private LocalDateTime startDate;
    private LocalDateTime closingDate;

}
