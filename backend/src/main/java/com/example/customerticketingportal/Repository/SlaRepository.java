package com.example.customerticketingportal.Repository;

import com.example.customerticketingportal.Model.SLA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlaRepository extends JpaRepository<SLA, Long> {
    List<SLA> findByActive(boolean active);
    List<SLA> findByNameContainingIgnoreCase(String name);
}
