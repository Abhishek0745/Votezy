package in.scalive.votezy.service;

import in.scalive.votezy.dto.LoginRequestDTO;
import in.scalive.votezy.dto.LoginResponseDTO;
import in.scalive.votezy.dto.VoterRegisterDTO;
import in.scalive.votezy.entity.Role;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.DuplicateResourceException;
import in.scalive.votezy.repository.VoterRepository;
import in.scalive.votezy.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// ✅ NEW FILE: Handles login and registration with JWT
@Service
@AllArgsConstructor
public class AuthService {

    private final VoterRepository voterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Register a new voter
    public Voter register(VoterRegisterDTO dto) {
        // Check if email already exists
        if (voterRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                "Email " + dto.getEmail() + " is already registered"
            );
        }

        Voter voter = new Voter();
        voter.setName(dto.getName());
        voter.setEmail(dto.getEmail());
        voter.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash password
        voter.setRole(Role.VOTER);

        return voterRepository.save(voter);
    }

    // Login - verify password and return JWT token
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Voter voter = voterRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // Verify password against stored hash
        if (!passwordEncoder.matches(dto.getPassword(), voter.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(voter.getEmail(), voter.getRole().name());

        return new LoginResponseDTO(
            token,
            voter.getEmail(),
            voter.getName(),
            voter.getRole().name()
        );
    }
}