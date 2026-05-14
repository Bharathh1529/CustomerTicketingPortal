package com.example.customerticketingportal.Controller;

import com.example.customerticketingportal.Dto.*;
import com.example.customerticketingportal.Model.Enums.Role;
import com.example.customerticketingportal.Model.Ticket;
import com.example.customerticketingportal.Model.User;
import com.example.customerticketingportal.Repository.UserRepository;
import com.example.customerticketingportal.Service.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {
    private final TicketService ticketService;
    private final UserRepository userRepo;
    public TicketController(TicketService ts, UserRepository ur){
        this.ticketService = ts;
        this.userRepo = ur;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody @Valid TicketCreateRequest request){
        var response = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable String id){
        return ticketService.getTicketById(id);
    }

    @PatchMapping("/{id}")
    public TicketResponse updateTicket(@PathVariable String id, @RequestBody TicketPatchRequest request){
        return ticketService.updateTicket(id, request);
    }

    @PostMapping("/{id}/messages")
    public TicketMessageResponse addMessage(@PathVariable String id, @RequestBody TicketMessageCreateRequest request){
        return ticketService.addMessage(id, request);
    }

    @PatchMapping("{id}/close")
    public TicketResponse closeTicket(@PathVariable String id, @RequestParam Long agentId){
        return ticketService.closeTicket(id, agentId);
    }
    @GetMapping("/requester/{id}")
    public List<TicketResponse> getTicketsByRequester(@PathVariable Long id) {
        return ticketService.getTicketsByRequester(id);
    }

    @GetMapping("/assignee/{agentId}")
    public List<TicketResponse> getTicketAssignedToAgent(@PathVariable Long agentId){
        return ticketService.getTicketAssignedToAgent(agentId);
    }
    @GetMapping("/unassigned")
    public List<TicketResponse> getUnassignedTickets() {
        return ticketService.getUnassignedTickets();
    }
    @PatchMapping("/{ticketId}/assign")
    public TicketResponse assignToMe(
            @PathVariable String ticketId,
            @RequestParam Long agentId) {

        return ticketService.assignToAgent(ticketId, agentId);
    }

    // dashboard stats
    @GetMapping("/agent/dashboard/{agentId}")
    public Map<String , Long> getDashboardStats(@PathVariable Long agentId) {
        return ticketService.getAgentDashboardStats(agentId);
    }

    @GetMapping("/agents")
    public List<User> getAgents(){
        return userRepo.findByRole(Role.AGENT);
    }
}
