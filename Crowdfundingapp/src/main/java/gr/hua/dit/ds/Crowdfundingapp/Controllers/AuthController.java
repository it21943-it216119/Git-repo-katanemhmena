package gr.hua.dit.ds.Crowdfundingapp.Controllers;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Role;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.repository.RoleRepository;
import gr.hua.dit.ds.Crowdfundingapp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {

    private UserRepository userRepository;
    RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void setup() {
        Role role_supporter = new Role("ROLE_SUPPORTER");
        Role role_founder = new Role("ROLE_FOUNDER");
        Role role_admin = new Role("ROLE_ADMIN");


        roleRepository.updateOrInsert(role_supporter);
        roleRepository.updateOrInsert(role_founder);
        roleRepository.updateOrInsert(role_admin);

        User admin = new User("admin", "kiriako16@gmail.com", "boss123");
        String passwd= admin.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        admin.setPassword(encodedPassword);


        Set<Role> roles = new HashSet<>();
        Role founderRole = roleRepository.findByName("ROLE_FOUNDER")
                .orElseThrow(() -> new RuntimeException("Error: Role FOUNDER not found."));
        Role supporterRole = roleRepository.findByName("ROLE_SUPPORTER")
                .orElseThrow(() -> new RuntimeException("Error: Role SUPPORTER not found."));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));
        roles.add(founderRole);
        roles.add(supporterRole);
        roles.add(adminRole);

        admin.setRoles(roles);

        if (userRepository.existsByUsername(admin.getUsername()) == false) {
            userRepository.save(admin);
        }

    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
