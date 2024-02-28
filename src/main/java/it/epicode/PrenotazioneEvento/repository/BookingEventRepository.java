package it.epicode.PrenotazioneEvento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.epicode.PrenotazioneEvento.model.BookingEvent;
import java.util.List;

@Repository
public interface BookingEventRepository extends JpaRepository<BookingEvent, Long> {
    List<BookingEvent> findByUserId(Long userId);
    List<BookingEvent> findByEventName(String eventName);



}
