    package com.example.customerticketingportal.Controller;

    import com.example.customerticketingportal.Dto.AuthRequest;
    import com.example.customerticketingportal.Dto.AuthResponse;
    import com.example.customerticketingportal.Model.User;
    import com.example.customerticketingportal.Repository.UserRepository;
    import com.example.customerticketingportal.Security.JwtService;
    import io.swagger.v3.oas.annotations.Operation;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/auth")
    @CrossOrigin(origins = "*")
    public class AuthController {

        @Autowired
        private UserRepository userRepo;
        @Autowired
        private JwtService jwtService;
        @Autowired
        private BCryptPasswordEncoder encoder;


        @Operation(
                summary = "User Login",
                description = "Allows user to login and returns JWT token"
        )

        @PostMapping("/login")
        public AuthResponse login(@RequestBody AuthRequest request){
            User user = userRepo.findByEmail(request.getEmail()).
                    orElseThrow(() -> new RuntimeException("User not found"));

            if(!encoder.matches(request.getPassword(), user.getPassword())){
                throw new RuntimeException("Invlaid password");
            }
            String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
            return new AuthResponse(
                    user.getId(),
                    user.getOrganization().getId(),
                    token,
                    user.getRole().name(),
                    user.getName()
            );
        }
    }
