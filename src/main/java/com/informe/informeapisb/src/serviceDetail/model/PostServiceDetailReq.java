package com.informe.informeapisb.src.serviceDetail.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostServiceDetailReq {
    private Integer page;
    private Integer perPage;
    private Integer totalCount;
    private Integer currentCount;
    private Integer matchCount;
    private List<data> data;
}
