package org.unibl.etf.fitness.services.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.models.dto.RssFeedDTO;
import org.unibl.etf.fitness.models.dto.RssItem;
import org.unibl.etf.fitness.services.RssFeedService;

import java.net.URL;
import java.util.stream.Collectors;

@Service
public class RssFeedServiceImpl implements RssFeedService {

    @Value("${rss.feed.url}")
    private String feedUrl;

    @Override
    public RssFeedDTO getRssFeed() {
        try {
            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            RssFeedDTO rssFeed = new RssFeedDTO();
            rssFeed.setTitle(feed.getTitle());

            // Dodaj description i link iz <channel> taga
            rssFeed.setDescription(feed.getDescription());
            rssFeed.setLink(feed.getLink());

            rssFeed.setRssItems(feed.getEntries().stream()
                    .map(this::mapToRssItem)
                    .collect(Collectors.toList()));

            return rssFeed;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching or parsing RSS feed", e);
        }
    }

    private RssItem mapToRssItem(SyndEntry entry) {
        RssItem rssItem = new RssItem();
        rssItem.setTitle(entry.getTitle());
        rssItem.setDescription(entry.getDescription().getValue());
        rssItem.setLink(entry.getLink());

        // Dodaj kategoriju iz <category> taga
        if (entry.getCategories() != null && !entry.getCategories().isEmpty()) {
            rssItem.setCategory(entry.getCategories().get(0).getName());
        }

        return rssItem;
    }
}
