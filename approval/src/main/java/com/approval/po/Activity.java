package com.approval.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Data
public class Activity {
    @Id
    private String id;
    private String author;
    private String title;
    private String content;
    private List<User> participants;
    private LocalDateTime startDate;
    private LocalDateTime closingDate;
    private Long originCount;
    private volatile Long remainCount;

    public Activity(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }
}