package org.telegram.bot.scrapingbot.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesRequest;
import org.telegram.bot.scrapingbot.service.impl.SizesServiceImpl;

@Service
@Slf4j
public class KafkaTestListener {

    @Autowired
    SizesServiceImpl sizesService;

    @KafkaListener(topics = "${cloudkarafka.topic.sizesrequest}",
            containerFactory = "availableSizesConsumerFactory")
    public void consumeJson(AvailableSizesRequest messageReceived) {
        sizesService.receiveSizesRequest(messageReceived);
    }
}
