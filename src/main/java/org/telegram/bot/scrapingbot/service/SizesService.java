package org.telegram.bot.scrapingbot.service;

import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesRequest;
import org.telegram.bot.scrapingbot.model.kafka.GarmentAdvice;

public interface SizesService {

    void receiveSizesRequest(AvailableSizesRequest availableSizesRequest);

    void sendMessageToKafka(GarmentAdvice garment);
}
