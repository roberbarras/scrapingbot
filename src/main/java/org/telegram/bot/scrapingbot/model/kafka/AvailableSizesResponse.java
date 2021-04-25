package org.telegram.bot.scrapingbot.model.kafka;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AvailableSizesResponse implements Serializable {

    private String clientId;
    private String chatId;
    private String language;
    private String url;
    private Map<String, Boolean> sizes;
}
