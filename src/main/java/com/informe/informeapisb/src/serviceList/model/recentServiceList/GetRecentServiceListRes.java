package com.informe.informeapisb.src.serviceList.model.recentServiceList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRecentServiceListRes {
    private List<GetRecentServiceInfoRes> getRecentServiceInfoRes;
}
