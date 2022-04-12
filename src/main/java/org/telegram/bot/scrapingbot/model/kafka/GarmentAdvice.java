package org.telegram.bot.scrapingbot.model.kafka;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GarmentAdvice {

    private long id;
    private Long clientId;
    private String url;
    private String name;
    private String size;
    private boolean enabled;
    private boolean notified;
}
