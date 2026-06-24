package in.scalive.votezy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// ✅ MODIFIED FILE: Added password and role fields for JWT authentication
@Entity
@Getter
@Setter
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    // ✅ NEW: Password field (stored as BCrypt hash)
    @NotBlank(message = "Password is required")
    @JsonIgnore // Never send password in API response
    private String password;

    // ✅ NEW: Role field (VOTER or ADMIN)
    @Enumerated(EnumType.STRING)
    private Role role = Role.VOTER; // Default role is VOTER

    private boolean hasVoted = false;

    @OneToOne(mappedBy = "voter", cascade = CascadeType.ALL)
    @JsonIgnore
    private Vote vote;
}