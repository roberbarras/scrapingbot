package org.telegram.bot.scrapingbot.service;

import org.telegram.bot.scrapingbot.model.entity.Garment;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesRequest;

import java.util.Map;

public interface RequestService {

    Map<String, Boolean> getSizes(AvailableSizesRequest garment);

    boolean checkForSize(Garment garment);
}
