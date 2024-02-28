package it.epicode.PrenotazioneEvento.service;


import it.epicode.PrenotazioneEvento.exception.EntityNotFoundException;
import it.epicode.PrenotazioneEvento.model.Event;
import it.epicode.PrenotazioneEvento.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event newEvent) {

        if (newEvent.getData().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        return eventRepository.save(newEvent);
    }

    public Event updateEvent(Long eventId, Event updatedEvent) {
        return eventRepository.findById(eventId).map(event -> {
            event.setTitolo(updatedEvent.getTitolo());
            event.setDescrizione(updatedEvent.getDescrizione());
            event.setData(updatedEvent.getData());
            event.setLuogo(updatedEvent.getLuogo());
            event.setNumeroPostiDisponibili(updatedEvent.getNumeroPostiDisponibili());
            event.setTipo(updatedEvent.getTipo());

            return eventRepository.save(event);
        }).orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
    }



}
