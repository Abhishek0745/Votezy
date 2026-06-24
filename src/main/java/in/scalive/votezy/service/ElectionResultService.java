package in.scalive.votezy.service;

import in.scalive.votezy.dto.ElectionResultResponseDTO;
import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.ElectionResult;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.ElectionResultRepository;
import in.scalive.votezy.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// ✅ MODIFIED FILE: Now calculates votePercentage and winningMargin
@Service
@AllArgsConstructor
public class ElectionResultService {

    private final ElectionResultRepository electionResultRepository;
    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    public ElectionResultResponseDTO declareElectionResult(String electionName) {
        // If already declared, return existing result
        Optional<ElectionResult> existingResult = electionResultRepository.findByElectionName(electionName);
        if (existingResult.isPresent()) {
            return buildResponseDTO(existingResult.get());
        }

        if (voteRepository.count() == 0) {
            throw new IllegalStateException("Cannot declare result - no votes have been cast yet");
        }

        // Get candidates sorted by vote count (highest first)
        List<Candidate> allCandidates = candidateRepository.findAllByOrderByVoteCountDesc();
        if (allCandidates.isEmpty()) {
            throw new ResourceNotFoundException("No candidates found");
        }

        Candidate winner = allCandidates.get(0);
        int totalVotes = allCandidates.stream().mapToInt(Candidate::getVoteCount).sum();

        ElectionResult result = new ElectionResult();
        result.setElectionName(electionName);
        result.setWinner(winner);
        result.setTotalVotes(totalVotes);

        ElectionResult saved = electionResultRepository.save(result);
        return buildResponseDTO(saved);
    }

    public List<ElectionResult> getAllResults() {
        return electionResultRepository.findAll();
    }

    // ✅ NEW: Builds rich response with analytics
    private ElectionResultResponseDTO buildResponseDTO(ElectionResult result) {
        Candidate winner = result.getWinner();
        int totalVotes = result.getTotalVotes();

        // Get all candidates to find runner-up for margin calculation
        List<Candidate> allCandidates = candidateRepository.findAllByOrderByVoteCountDesc();

        double percentage = totalVotes > 0
            ? Math.round((winner.getVoteCount() * 100.0 / totalVotes) * 10.0) / 10.0
            : 0.0;

        int margin = allCandidates.size() > 1
            ? winner.getVoteCount() - allCandidates.get(1).getVoteCount()
            : winner.getVoteCount();

        ElectionResultResponseDTO dto = new ElectionResultResponseDTO();
        dto.setElectionName(result.getElectionName());
        dto.setTotalVotes(totalVotes);
        dto.setWinnerId(winner.getId());
        dto.setWinnerName(winner.getName());         // ✅ NEW
        dto.setWinnerParty(winner.getParty());       // ✅ NEW
        dto.setWinnerVotes(winner.getVoteCount());
        dto.setVotePercentage(percentage);           // ✅ NEW
        dto.setWinningMargin(margin);                // ✅ NEW

        return dto;
    }
}