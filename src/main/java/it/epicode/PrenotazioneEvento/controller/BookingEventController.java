package it.epicode.PrenotazioneEvento.controller;



import it.epicode.PrenotazioneEvento.model.BookingEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import it.epicode.PrenotazioneEvento.model.BookingEvent;
import it.epicode.PrenotazioneEvento.service.BookingEventService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/bookingEvents")
@Validated
public class BookingEventController {

    private final BookingEventService bookingEventService;

    @Autowired
    public BookingEventController(BookingEventService bookingEventService) {
        this.bookingEventService = bookingEventService;
    }

    @GetMapping
    public ResponseEntity<List<BookingEvent>> findAll() {
        List<BookingEvent> bookingEvents = bookingEventService.findAll();
        return ResponseEntity.ok(bookingEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingEvent> findById(@PathVariable Long id) {
        BookingEvent bookingEvent = bookingEventService.findById(id);
        return ResponseEntity.ok(bookingEvent);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<BookingEvent> save(@Valid @RequestBody BookingEvent bookingEvent) {
        BookingEvent savedBookingEvent = bookingEventService.save(new BookingEventRequest());
        return new ResponseEntity<>(savedBookingEvent, HttpStatus.CREATED);
    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<BookingEvent> update(@PathVariable Long id, @Valid @RequestBody BookingEvent bookingEvent) {
//        BookingEvent updatedBookingEvent = bookingEventService.update(bookingEvent);
//        return ResponseEntity.ok(updatedBookingEvent);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
//    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
//        bookingEventService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Metodo per la ricerca di prenotazioni per nome evento
//    @GetMapping("/search")
//    public ResponseEntity<List<BookingEvent>> findByEventName(@RequestParam String eventName) {
//        List<BookingEvent> bookingEvents = bookingEventService.findByEventName(eventName);
//        return ResponseEntity.ok(bookingEvents);
//    }
}