package it.epicode.PrenotazioneEvento.model;

public class BookingEventRequest {

    private Long id;
    private Long organizerEventId;

    public BookingEventRequest() {
    }

    public BookingEventRequest(Long id, Long organizerEventId) {
        this.id = id;
        this.organizerEventId = organizerEventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEvent() {
        return organizerEventId;
    }

    public void setEvent(Long organizerEventId) {
        this.organizerEventId = organizerEventId;
    }


}
