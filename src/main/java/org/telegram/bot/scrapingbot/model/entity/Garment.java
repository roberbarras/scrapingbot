package org.telegram.bot.scrapingbot.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Garment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int clientId;

    @Column
    private String url;

    @Column
    private String name;

    @Column
    private String size;

    @Column
    @Setter
    private boolean enabled;

    @Column
    private BigDecimal price;

    @Column
    @Setter
    private boolean notified;

    @Column
    private LocalDateTime creationDate;

    public void setSize(String size) {
        this.size = size;
    }
}
