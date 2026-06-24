package in.scalive.votezy.service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.exception.VoteNotAllowedException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.VoteRepository;
import in.scalive.votezy.repository.VoterRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// ✅ MODIFIED FILE: castVote now takes voterId from JWT (authenticated user)
// No longer accepts voterId from request body - security fix
@Service
@AllArgsConstructor
public class VotingService {

    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public Vote castVote(Long voterId, Long candidateId) {
        // VoterId comes from authenticated JWT, not from request body
        Voter voter = voterRepository.findById(voterId)
            .orElseThrow(() -> new ResourceNotFoundException("Voter not found with ID: " + voterId));

        if (voter.isHasVoted()) {
            throw new VoteNotAllowedException("You have already cast your vote");
        }

        Candidate candidate = candidateRepository.findById(candidateId)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + candidateId));

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        voteRepository.save(vote);

        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateRepository.save(candidate);

        voter.setVote(vote);
        voter.setHasVoted(true);
        voterRepository.save(voter);

        return vote;
    }

    // ✅ FIXED: Renamed from getAllVoters to getAllVotes (was wrong name in original)
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }
}