package com.informe.informeapisb.src.news.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class GetTopRes {
    List<GetTopData> data;
}
