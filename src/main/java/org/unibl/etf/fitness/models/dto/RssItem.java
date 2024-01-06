package org.unibl.etf.fitness.models.dto;

import lombok.Data;

@Data
public class RssItem {
    private String title;
    private String description;
    private String link;
    private String category;
}
