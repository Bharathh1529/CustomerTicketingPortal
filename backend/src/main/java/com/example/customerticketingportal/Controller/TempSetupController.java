package com.example.customerticketingportal.Controller;

import com.example.customerticketingportal.Model.Enums.Role;
import com.example.customerticketingportal.Model.Organization;
import com.example.customerticketingportal.Model.User;
import com.example.customerticketingportal.Repository.OrganizationRepository;
import com.example.customerticketingportal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/setup")
public class TempSetupController {

    private final OrganizationRepository orgRepo;
    private final UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public TempSetupController(OrganizationRepository orgRepo, UserRepository userRepo) {
        this.orgRepo = orgRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/org")
    public Organization createOrg(@RequestParam String name, @RequestParam String domain) {
        var org = new Organization();
        org.setName(name);
        org.setDomain(domain);
        return orgRepo.save(org);
    }

    @PostMapping("/user")
    public User createUser(@RequestParam String email,
                           @RequestParam String name,
                           @RequestParam Long orgId,
                           @RequestParam(defaultValue = "USER") Role role,
                           @RequestParam String password) {

        var org = orgRepo.findById(orgId).orElseThrow();

        var u = new User();
        u.setEmail(email);
        u.setName(name);
        u.setOrganization(org);
        u.setRole(role);
        u.setCreatedAt(LocalDateTime.now());

        u.setPassword(encoder.encode(password));

        return userRepo.save(u);
    }

    @GetMapping("/orgs")
    public List<Organization> getOrg() {
        return orgRepo.findAll();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepo.findAll();
    }
}