package com.ticketbooking.user.repository;

import com.ticketbooking.user.model.TicketDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<TicketDetails, String> {
    Optional<TicketDetails> findByEmail(String email);
}
