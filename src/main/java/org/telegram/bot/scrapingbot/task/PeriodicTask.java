package org.telegram.bot.scrapingbot.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.bot.scrapingbot.model.entity.Garment;
import org.telegram.bot.scrapingbot.model.kafka.GarmentAdvice;
import org.telegram.bot.scrapingbot.repository.GarmentRepository;
import org.telegram.bot.scrapingbot.service.impl.SizesServiceImpl;
import org.telegram.bot.scrapingbot.service.impl.RequestServiceImpl;

import java.util.List;

@Component
public class PeriodicTask {

    @Autowired
    GarmentRepository garmentRepository;

    @Autowired
    RequestServiceImpl processHtmlService;

    @Autowired
    SizesServiceImpl sizesService;

    @Scheduled(fixedRate = 60000)
    public void scheduledCheck() {

        List<Garment> garmentsToSearch = garmentRepository.findAllByNotifiedFalseOrderByCreationDateAsc();

        garmentsToSearch
                .stream()
                .parallel()
                .filter(processHtmlService::checkForSize)
                .forEach(this::noticeAndDelete);

    }

    private void noticeAndDelete(Garment garment) {
        sizesService.sendMessageToKafka(GarmentAdvice.builder()
                .id(garment.getId())
                .clientId(garment.getClientId())
                .url(garment.getUrl())
                .name(garment.getName())
                .size(garment.getSize())
                .enabled(garment.isEnabled())
                .notified(garment.isNotified())
                .build());
        garmentRepository.findById(garment.getId()).ifPresent(elem -> {
            elem.setNotified(true);
            elem.setEnabled(false);
            garmentRepository.save(elem);
        });
    }


}
