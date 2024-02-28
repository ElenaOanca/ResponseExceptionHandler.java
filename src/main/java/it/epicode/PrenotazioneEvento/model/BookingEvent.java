package it.epicode.PrenotazioneEvento.model;

import jakarta.persistence.*;




import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "booking_event")
@NamedQuery(name = "BookingEvent.findBookingByEventId", query = "SELECT b FROM BookingEvent b WHERE b.event.id = :id")
@Data
public class BookingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private LocalDateTime bookingDate;

    public BookingEvent() {
    }

    public BookingEvent(User user, Event event, LocalDateTime bookingDate) {
        this.user = user;
        this.event = event;
        this.bookingDate = bookingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}
