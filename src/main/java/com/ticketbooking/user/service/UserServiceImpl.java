package com.ticketbooking.user.service;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Unit;
import com.ticketbooking.user.model.EmailDetails;
import com.ticketbooking.user.model.TicketDetails;
import com.ticketbooking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public TicketDetails saveTicket(TicketDetails ticketDetails) {
        String uniqueId = UUID.randomUUID().toString();
        if (ticketDetails.getUniqueId() == null) {
            ticketDetails.setUniqueId(uniqueId);
        }

        if (ticketDetails.getStatus() == null) {
            ticketDetails.setStatus("NOT USED");
        }
        ticketDetails.setFare(String.valueOf(getFare(ticketDetails.getOrigin(), ticketDetails.getDestination())));
        userRepository.save(ticketDetails);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(ticketDetails.getEmail());
        emailDetails.setSubject("Ticket booking successful: " + ticketDetails.getOrigin()
                + " to " + ticketDetails.getDestination());

        String messageBody = "Hi " + ticketDetails.getFirstName() + " " + ticketDetails.getLastName() + ", \n"
                + "Your ticket from " + ticketDetails.getOrigin() + " to " + ticketDetails.getDestination()
                + " is booked successfully. Please find your ticket details below: \n"
                + "\nFrom: " + ticketDetails.getOrigin()
                + "\nTo: " + ticketDetails.getDestination()
                + "\nUnique Id: " + ticketDetails.getUniqueId()
                + "\nFare: " + ticketDetails.getFare()
                + "\n\n Please contact us at +91 9988776655 in case of any issues or queries."
                + "\n\nVisit Again!"
                + "\nThanks!";

        emailDetails.setMsgBody(messageBody);

        String emailresponse = emailService.sendSimpleMail(emailDetails);
        System.out.println(emailresponse);

        return ticketDetails;
    }

    @Override
    public void confirmTicket(String ticketId) {
        Optional<TicketDetails> optionalTicket = userRepository.findById(ticketId);

        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        } else {
            TicketDetails ticketDetails = optionalTicket.get();

            if (ticketDetails.getUniqueId().equals(ticketId) && ticketDetails.getStatus().equals("NOT USED")) {
                ticketDetails.setStatus("USED");
                saveTicket(ticketDetails);
            } else {
                throw new RuntimeException("Ticket Not Valid");
            }
        }
    }

    @Override
    public TicketDetails getTicket(String ticketId) {
        Optional<TicketDetails> optionalTicket = userRepository.findById(ticketId);

        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        } else {
            return optionalTicket.get();
        }
    }

    private double getFare(String origin, String destination) {
        String api_key = "AIzaSyC5C4GemZ-3-3Uja2Ob6zs5hxOkPmzEBMc";
        final double fare_multiplier = 10.0;

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(api_key)
                .build();

        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context);
        DistanceMatrix matrix = null;
        try {
            matrix = request.origins(origin)
                    .destinations(destination)
                    .units(Unit.METRIC)
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        Distance distance = matrix.rows[0].elements[0].distance;
        double kms = Double.parseDouble(distance.toString().replace(" km", ""));

        System.out.println("Distance: " + kms);

        return kms * fare_multiplier;
    }
}