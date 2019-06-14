package com.baymin.restroomapi.ret.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RetFuckFlowContrast {

    private Integer thisWeek;
    private Integer lastWeek;
    private Integer thisMonth;
    private Integer lastMonth;
    private List<Map<String, Object>> list;
}
