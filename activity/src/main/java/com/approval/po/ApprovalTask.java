package com.approval.po;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Data
@AllArgsConstructor
public class ApprovalTask {
    private String activityId;
    private ParticipateSituation participateSituation;
}
