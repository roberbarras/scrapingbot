package org.telegram.bot.scrapingbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesRequest;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesResponse;
import org.telegram.bot.scrapingbot.model.kafka.GarmentAdvice;
import org.telegram.bot.scrapingbot.service.SizesService;

import java.util.Map;

@Service
public class SizesServiceImpl implements SizesService {

    @Autowired
    private RequestServiceImpl requestService;

    @Autowired
    private KafkaTemplate<String, AvailableSizesResponse> sizesResponseProducer;

    @Autowired
    private KafkaTemplate<String, GarmentAdvice> garmentProducerFactory;

    @Value("${cloudkarafka.topic.sizesresponse}")
    private String availableSizesResponseTopic;

    @Value("${cloudkarafka.topic.newalert}")
    private String newAlertTopic;

    public void receiveSizesRequest(AvailableSizesRequest availableSizesRequest) {
        Map<String, Boolean> sizes = requestService.getSizes(availableSizesRequest);
        AvailableSizesResponse availableSizesResponse = AvailableSizesResponse
                .builder()
                .clientId(availableSizesRequest.getClientId())
                .language(availableSizesRequest.getLanguage())
                .chatId(availableSizesRequest.getChatId())
                .url(availableSizesRequest.getUrl())
                .sizes(sizes)
                .build();
        sizesResponseProducer.send(availableSizesResponseTopic, availableSizesResponse);
    }

    public void sendMessageToKafka(GarmentAdvice messageToSend) {
        garmentProducerFactory.send(newAlertTopic, messageToSend);
        //CompletableFuture.runAsync(() -> messageService.save(messageToSend));
    }
}
