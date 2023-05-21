package com.ticketbooking.user.service;

import com.ticketbooking.user.model.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
}
