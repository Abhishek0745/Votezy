package in.scalive.votezy.config;

import in.scalive.votezy.entity.Role;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final VoterRepository voterRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@votezy.com}")
    private String adminEmail;

    @Value("${admin.password:Admin@123}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        if (voterRepository.findByEmail(adminEmail).isEmpty()) {

            Voter admin = new Voter();
            admin.setName("System Administrator");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setHasVoted(false);

            voterRepository.save(admin);

            System.out.println("====================================");
            System.out.println(" Default Admin Created Successfully ");
            System.out.println(" Email : " + adminEmail);
            System.out.println("====================================");

        } else {
            System.out.println("Admin already exists.");
        }
    }
}