package in.scalive.votezy.controller;

import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.service.VoterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ✅ MODIFIED FILE:
// - Registration moved to AuthController (/api/auth/register)
// - Admin-only operations protected with @PreAuthorize
@RestController
@RequestMapping("/api/voters")
@CrossOrigin
@AllArgsConstructor
public class VoterController {

    private final VoterService voterService;

    // ADMIN only: Get all voters
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Voter>> getAllVoters() {
        return ResponseEntity.ok(voterService.getAllVoters());
    }

    // ADMIN only: Get voter by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Voter> getVoterById(@PathVariable Long id) {
        return ResponseEntity.ok(voterService.getVoterById(id));
    }

    // ADMIN only: Update voter
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Voter> updateVoter(@PathVariable Long id, @RequestBody Voter voter) {
        return ResponseEntity.ok(voterService.updateVoter(id, voter));
    }

    // ADMIN only: Delete voter
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVoter(@PathVariable Long id) {
        voterService.deleteVoter(id);
        return ResponseEntity.ok("Voter with id: " + id + " deleted successfully");
    }
}