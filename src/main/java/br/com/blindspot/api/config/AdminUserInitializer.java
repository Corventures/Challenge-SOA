package br.com.blindspot.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.blindspot.api.domain.AppUser;
import br.com.blindspot.api.repository.AppUserRepository;

@Component
@Profile("dev")
public class AdminUserInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (appUserRepository.findByUsername("admin").isEmpty()) {
            appUserRepository.save(new AppUser("admin", passwordEncoder.encode("Admin@123"), "ADMIN"));
        }
    }
}