package in.scalive.votezy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// ✅ MODIFIED FILE: Removed voterId - now extracted from JWT token automatically
// Voter no longer needs to enter their own ID (security fix)
@Data
public class VoteRequestDTO {

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;
}