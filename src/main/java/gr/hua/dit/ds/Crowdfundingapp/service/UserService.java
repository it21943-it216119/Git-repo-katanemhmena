package gr.hua.dit.ds.Crowdfundingapp.service;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Role;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.repository.RoleRepository;
import gr.hua.dit.ds.Crowdfundingapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Integer saveUser(User user, String role) {
        String passwd= user.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        user.setPassword(encodedPassword);


        Set<Role> roles = new HashSet<>();

        if ("FOUNDER".equalsIgnoreCase(role)) {
            Role founderRole = roleRepository.findByName("ROLE_FOUNDER")
                    .orElseThrow(() -> new RuntimeException("Error: Role FOUNDER not found."));
            Role supporterRole = roleRepository.findByName("ROLE_SUPPORTER")
                    .orElseThrow(() -> new RuntimeException("Error: Role SUPPORTER not found."));
            roles.add(founderRole);
            roles.add(supporterRole);
        } else if ("SUPPORTER".equalsIgnoreCase(role)) {
            Role supporterRole = roleRepository.findByName("ROLE_SUPPORTER")
                    .orElseThrow(() -> new RuntimeException("Error: Role SUPPORTER not found."));
            roles.add(supporterRole);
        } else {
            throw new RuntimeException("Error: Invalid role selected.");
        }

        user.setRoles(roles);

        user = userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Integer updateUser(User user) {
        user = userRepository.save(user);
        return user.getId();
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty())
            throw new UsernameNotFoundException("User with username: " +username +" not found !");
        else {
            User user = opt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role-> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet())
            );
        }
    }

    @Transactional
    public Object getUsers() {
        return userRepository.findAll();
    }

    public Object getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public void updateOrInsertRole(Role role) {
        roleRepository.updateOrInsert(role);
    }
}