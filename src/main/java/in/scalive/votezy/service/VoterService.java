package in.scalive.votezy.service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.VoterRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// ✅ MODIFIED FILE: Registration now handled by AuthService
// VoterService keeps admin operations (list, delete, update voters)
@Service
@AllArgsConstructor
public class VoterService {

    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;

    public List<Voter> getAllVoters() {
        return voterRepository.findAll();
    }

    public Voter getVoterById(Long id) {
        return voterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Voter with id: " + id + " not found"));
    }

    public Voter updateVoter(Long id, Voter updateVoter) {
        Voter voter = getVoterById(id);

        if (updateVoter.getName() != null) {
            voter.setName(updateVoter.getName());
        }
        if (updateVoter.getEmail() != null) {
            voter.setEmail(updateVoter.getEmail());
        }

        return voterRepository.save(voter);
    }

    @Transactional
    public void deleteVoter(Long id) {
        Voter voter = getVoterById(id);
        Vote vote = voter.getVote();

        if (vote != null) {
            Candidate candidate = vote.getCandidate();
            if (candidate != null) {
                candidate.setVoteCount(candidate.getVoteCount() - 1);
                candidateRepository.save(candidate);
            }
        }

        voterRepository.delete(voter);
    }
}