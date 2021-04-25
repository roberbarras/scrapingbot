package org.telegram.bot.scrapingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.bot.scrapingbot.model.entity.Garment;

import java.util.List;

@Repository
public interface GarmentRepository extends JpaRepository<Garment, Long> {

    List<Garment> findByClientIdAndEnabledTrueOrderByCreationDateAsc(int clientId);

    List<Garment> findAllByNotifiedFalseOrderByCreationDateAsc();
}
