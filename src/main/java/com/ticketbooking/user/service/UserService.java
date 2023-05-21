package com.ticketbooking.user.service;

import com.ticketbooking.user.model.TicketDetails;

import java.util.List;

public interface UserService {
    TicketDetails saveTicket(TicketDetails ticketDetails);

    void confirmTicket(String ticketId);

    TicketDetails getTicket(String ticketId);

}
