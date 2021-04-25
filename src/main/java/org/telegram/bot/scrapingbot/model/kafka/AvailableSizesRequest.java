package org.telegram.bot.scrapingbot.model.kafka;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AvailableSizesRequest implements Serializable {

    private String clientId;
    private String chatId;
    private String language;
    private String url;
}
