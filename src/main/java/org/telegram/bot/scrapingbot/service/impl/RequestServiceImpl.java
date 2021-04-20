package org.telegram.bot.scrapingbot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.telegram.bot.scrapingbot.model.entity.Garment;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesRequest;
import org.telegram.bot.scrapingbot.service.RequestService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    @Override
    public Map<String, Boolean> getSizes(AvailableSizesRequest garment) {
        return parseHtml(garment.getUrl());
    }

    @Override
    public boolean checkForSize(Garment garment) {
        return parseHtml(garment.getUrl())
                .getOrDefault(garment.getSize(), false);
    }

    private Map<String, Boolean> parseHtml(String url) {
        Map<String, Boolean> map = new HashMap<>();
        Function<Element, Boolean> getAvailability = elem -> !elem.hasClass("product-size-selector__size-list-item--is-disabled");
        Function<Element, String> getSize = elem -> elem.child(0).child(0).child(0).childNodes().get(0).toString();
        try {
            Jsoup.connect(url).get()
                    .getElementsByClass("product-size-selector__size-list")
                    .select("li")
                    .forEach(elem -> map.put(getSize.apply(elem), getAvailability.apply(elem)));
            log.info("URL: {}", url);
            log.info("SIZES: {}", map);
        } catch (IOException e) {
            log.error("ERROR al solicitar la url {}", url);
            log.error(e.getMessage());

        }
        return map;
    }


}