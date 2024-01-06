package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitness.models.dto.RssFeedDTO;
import org.unibl.etf.fitness.services.RssFeedService;

@RestController
@RequestMapping("/api/v1/rss")
public class RssController {

    private final RssFeedService rssFeedService;

    public RssController(RssFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    @GetMapping
    public RssFeedDTO getRssFeed(){
        return rssFeedService.getRssFeed();
    }
}
