package in.scalive.votezy.controller;

import in.scalive.votezy.dto.VoteRequestDTO;
import in.scalive.votezy.dto.VoteResponseDTO;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.service.VotingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ✅ MODIFIED FILE:
// - castVote: extracts voter from JWT (Authentication object), not from request body
// - VOTER role required to cast vote
// - ADMIN role required to view all votes
@RestController
@RequestMapping("/api/votes")
@CrossOrigin
@AllArgsConstructor
public class VotingController {

    private final VotingService votingService;

    // VOTER only: Cast a vote
    // Authentication object is auto-injected by Spring Security from JWT
    @PostMapping("/cast")
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<VoteResponseDTO> castVote(
            @RequestBody @Valid VoteRequestDTO voteRequest,
            Authentication authentication) {  // ✅ Voter identity comes from JWT

        // Get the logged-in voter from Authentication
        Voter loggedInVoter = (Voter) authentication.getPrincipal();

        Vote vote = votingService.castVote(loggedInVoter.getId(), voteRequest.getCandidateId());

        VoteResponseDTO response = new VoteResponseDTO(
            "Vote cast successfully",
            true,
            vote.getVoterId(),
            vote.getCandidateId()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ADMIN only: View all votes
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Vote>> getAllVotes() {
        return ResponseEntity.ok(votingService.getAllVotes());
    }
}