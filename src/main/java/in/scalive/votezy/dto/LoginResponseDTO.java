package in.scalive.votezy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// ✅ NEW FILE: Login response - returns token + user info
@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email;
    private String name;
    private String role;
}