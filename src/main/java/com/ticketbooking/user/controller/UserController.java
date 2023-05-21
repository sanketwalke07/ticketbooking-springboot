package com.ticketbooking.user.controller;

import com.ticketbooking.user.model.TicketDetails;
import com.ticketbooking.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save-ticket")
    public ResponseEntity<TicketDetails> saveTicket(@RequestBody TicketDetails ticketDetails) {
        TicketDetails ticketDetails1 = userService.saveTicket(ticketDetails);
        return new ResponseEntity<>(ticketDetails1, HttpStatus.OK);
    }

    @GetMapping("/confirm-ticket/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public void confirmTicket(@PathVariable (value = "ticketId") String ticketId) {
        userService.confirmTicket(ticketId);
    }

    @GetMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TicketDetails> getTicket(@PathVariable (value = "ticketId") String ticketId) {
        return new ResponseEntity<>(userService.getTicket(ticketId), HttpStatus.OK);
    }
}
