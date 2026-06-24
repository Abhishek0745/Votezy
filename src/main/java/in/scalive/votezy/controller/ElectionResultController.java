package in.scalive.votezy.controller;

import in.scalive.votezy.dto.ElectionResultRequestDTO;
import in.scalive.votezy.dto.ElectionResultResponseDTO;
import in.scalive.votezy.entity.ElectionResult;
import in.scalive.votezy.service.ElectionResultService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ✅ MODIFIED FILE:
// - Fixed filename: was "ELectionResultControllerr.java" (2 typos) → now "ElectionResultController.java"
// - declare result: ADMIN only
// - get all results: public
@RestController
@RequestMapping("/api/election-result")
@CrossOrigin
@AllArgsConstructor
public class ElectionResultController {

    private final ElectionResultService electionResultService;

    // ADMIN only: Declare election result
    @PostMapping("/declare")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ElectionResultResponseDTO> declareElectionResult(
            @RequestBody @Valid ElectionResultRequestDTO request) {
        ElectionResultResponseDTO result = electionResultService.declareElectionResult(request.getElectionName());
        return ResponseEntity.ok(result);
    }

    // PUBLIC: Get all results (voters can see results)
    @GetMapping
    public ResponseEntity<List<ElectionResult>> getAllResults() {
        return ResponseEntity.ok(electionResultService.getAllResults());
    }
}