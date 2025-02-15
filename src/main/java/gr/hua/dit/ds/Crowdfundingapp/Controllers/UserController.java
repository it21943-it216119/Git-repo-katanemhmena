package gr.hua.dit.ds.Crowdfundingapp.Controllers;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Role;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.repository.RoleRepository;
import gr.hua.dit.ds.Crowdfundingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private UserService userService;

    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute User user, @RequestParam("role") String role, Model model, BindingResult result) {
        if (result.hasErrors()) {
            // If there are validation errors, return to the registration page
            model.addAttribute("user", user);
            return "auth/register";
        }
        System.out.println("Selected Role: " + role);
        Integer id = userService.saveUser(user, role);
        String message = "Registration successful please login now!";
        model.addAttribute("msg", message);
        return "index";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Long user_id, Model model){
        User user = (User) userService.getUser(user_id);
        model.addAttribute("user", user);
        return "auth/user";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/user/{user_id}")
    public String saveStudent(@PathVariable Long user_id, @ModelAttribute("user") User user, Model model) {
        User the_user = (User) userService.getUser(user_id);
        the_user.setEmail(user.getEmail());
        the_user.setUsername(user.getUsername());
        userService.updateUser(the_user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public String deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRoletoUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }



}