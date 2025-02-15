package gr.hua.dit.ds.Crowdfundingapp.Controllers;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Funding;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.service.FundingService;
import gr.hua.dit.ds.Crowdfundingapp.service.ProjectService;
import gr.hua.dit.ds.Crowdfundingapp.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("supportedprojects")
public class SupportedProjectsController {

    private ProjectService projectService;
    private UserService userService;
    private FundingService fundingService;

    public SupportedProjectsController(ProjectService projectService, UserService userService, FundingService fundingService) {
        this.projectService = projectService;
        this.userService = userService;
        this.fundingService = fundingService;
    }


    @Secured("ROLE_SUPPORTER")
    @GetMapping("")
    public String showSupportedProjects(Model model, Principal principal) {
        String username = principal.getName();
        User supporter = userService.findUserByUsername(username);
        List <Funding> fundingsbyuser = fundingService.findFundingsBySupporter(supporter);
        Set<Project> supportedProjects = fundingsbyuser.stream()
                .map(Funding::getFundedProject)
                .collect(Collectors.toSet());
        model.addAttribute("projects", supportedProjects);
        return "projects/supportedprojects";
    }
}

