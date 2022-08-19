package com.informe.informeapisb.src.supportConditions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostSupportConditionsReq {
    private Integer page;
    private Integer perPage;
    private Integer totalCount;
    private Integer currentCount;
    private Integer matchCount;
    private List<SupportConditionsData> data;
}
