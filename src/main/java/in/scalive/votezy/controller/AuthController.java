package in.scalive.votezy.controller;

import in.scalive.votezy.dto.LoginRequestDTO;
import in.scalive.votezy.dto.LoginResponseDTO;
import in.scalive.votezy.dto.VoterRegisterDTO;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// ✅ NEW FILE: Public endpoints for registration and login
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/register - Register a new voter
    @PostMapping("/register")
    public ResponseEntity<Voter> register(@RequestBody @Valid VoterRegisterDTO dto) {
        Voter savedVoter = authService.register(dto);
        return new ResponseEntity<>(savedVoter, HttpStatus.CREATED);
    }

    // POST /api/auth/login - Login and get JWT token
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}