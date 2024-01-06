package org.unibl.etf.fitness.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class RssFeedDTO {
    private String title;
    private String description;
    private String link;
    private List<RssItem> rssItems;
}
