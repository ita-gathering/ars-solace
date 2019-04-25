package com.approval.po;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
@Data
@AllArgsConstructor
public class ParticipateSituation {
    private User participant;
    private String awards;
    private String status;
}
