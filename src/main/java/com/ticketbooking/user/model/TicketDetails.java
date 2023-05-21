package com.ticketbooking.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TICKETS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetails {
    private String email;
    private String firstName;
    private String lastName;
    private String origin;
    private String destination;
    private String fare;
    private String status;
    @Id
    private String uniqueId;
}
