package com.example.customerticketingportal.Repository;

import com.example.customerticketingportal.Model.Enums.Role;
import com.example.customerticketingportal.Model.Organization;
import com.example.customerticketingportal.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByOrganization(Organization organization);
    List<User> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<User> findByRole(Role role);
}
