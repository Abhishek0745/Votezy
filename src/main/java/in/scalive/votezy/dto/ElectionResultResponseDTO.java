package in.scalive.votezy.dto;

import lombok.Data;

// ✅ MODIFIED FILE: Added winnerName, votePercentage and winningMargin
@Data
public class ElectionResultResponseDTO {
    private String electionName;
    private int totalVotes;
    private long winnerId;
    private String winnerName;       // ✅ NEW
    private String winnerParty;      // ✅ NEW
    private int winnerVotes;
    private double votePercentage;   // ✅ NEW - e.g. 67.2
    private int winningMargin;       // ✅ NEW - votes ahead of runner-up
}