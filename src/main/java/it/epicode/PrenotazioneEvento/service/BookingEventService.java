package it.epicode.PrenotazioneEvento.service;


import it.epicode.PrenotazioneEvento.exception.BadRequestException;
import it.epicode.PrenotazioneEvento.exception.EntityNotFoundException;
import it.epicode.PrenotazioneEvento.model.BookingEvent;
import it.epicode.PrenotazioneEvento.model.BookingEventRequest;
import it.epicode.PrenotazioneEvento.model.Event;
import it.epicode.PrenotazioneEvento.model.User;
import it.epicode.PrenotazioneEvento.repository.BookingEventRepository;
import it.epicode.PrenotazioneEvento.repository.EventRepository;
import it.epicode.PrenotazioneEvento.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BookingEventService {

    public BookingEventService(EntityManager entityManager, EntityManagerFactory entityManagerFactory, BookingEventRepository bookingEventRepository, EventRepository eventRepository, SecurityContextRepository securityContextRepository, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.bookingEventRepository = bookingEventRepository;
        this.eventRepository = eventRepository;
        this.securityContextRepository = securityContextRepository;
        this.userRepository = userRepository;
    }

    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;


    private final BookingEventRepository bookingEventRepository;


    private  final EventRepository eventRepository;


    private final SecurityContextRepository securityContextRepository;


    private final UserRepository userRepository;



    public List<BookingEvent> findAll() {
        return bookingEventRepository.findAll();
    }

    public BookingEvent findById(Long id) {
        return bookingEventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BookingEvent with ID " + id + " not found"));
    }

    public BookingEvent save(BookingEventRequest bookingEventRequest) {
    UserDetails authenticationUser= (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findByUsername(authenticationUser.getUsername()).orElseThrow(() -> new EntityNotFoundException("User with ID " + authenticationUser.getUsername() + " not found"));

        Query query = entityManager.createQuery("SELECT e FROM BookingEvent e WHERE e.event_id = :event_id");
        query.setParameter("event_id", bookingEventRequest.getEvent());

        List<BookingEvent> bookingEvent = query.getResultList();

     Event event = eventRepository.findById(bookingEventRequest.getEvent()).orElseThrow(() -> new EntityNotFoundException("Event with ID " + bookingEventRequest.getEvent() + " not found"));
            int numeroPostiDisponibili = event.getNumeroPostiDisponibili() - bookingEvent.size();
            if (numeroPostiDisponibili < 1) throw new BadRequestException("No more available seats for the event");

        BookingEvent newBookingEvent = new BookingEvent(user, event, LocalDateTime.now());

        bookingEventRepository.save(newBookingEvent);
        return newBookingEvent;
    }







    public void delete(BookingEventRequest bookingEventRequest) {
        UserDetails authenticationUser= (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(authenticationUser.getUsername()).orElseThrow(() -> new EntityNotFoundException("User with ID " + authenticationUser.getUsername() + " not found"));
        Query query = entityManager.createQuery("SELECT e FROM BookingEvent e WHERE e.event_id = :event_id");
        query.setParameter("event_id", bookingEventRequest.getEvent());

        List<BookingEvent> bookingEvent = query.getResultList();

        Event event = eventRepository.findById(bookingEventRequest.getEvent()).orElseThrow(() -> new EntityNotFoundException("Event with ID " + bookingEventRequest.getEvent() + " not found"));

        Query query2 = entityManager.createQuery("SELECT e FROM BookingEvent e WHERE e.user_id = :user_id AND e.event_id = :event_id");
        query2.setParameter("user_id", user.getId());
        query2.setParameter("event_id", event.getId());
        List<BookingEvent> bookingEvent2 = query2.getResultList();
        if (bookingEvent2.isEmpty()) throw new BadRequestException("User has not booked for the event");
        bookingEventRepository.deleteById(bookingEvent2.get(0).getId());


    }
//
//    //metodo findByEventName
//
//    public List<BookingEvent> findByEventName(String eventName) {
//        if (eventName == null) {
//            throw new EntityNotFoundException("Event name is null");
//        }
//        return bookingEventRepository.findByEventName(eventName);
//    }
}
