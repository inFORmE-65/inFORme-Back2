package com.informe.informeapisb.src.serviceDetail.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetServiceDetailBySVCIDRes {
    private Integer totalCount;
    private Integer matchCount;
    private List<GetServiceDetailData> data;
}
