package com.informe.informeapisb.src.news.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedData {
    private String title;
    private String part;
    private String link;
    private String imgUrl;
    private String date;
    private String description;
}