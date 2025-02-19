package gr.hua.dit.ds.Crowdfundingapp.Controllers;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Funding;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.ProjectStatus;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.service.FundingService;
import gr.hua.dit.ds.Crowdfundingapp.service.ProjectService;
import gr.hua.dit.ds.Crowdfundingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import org.springframework.ui.Model;

@Controller
@RequestMapping("projects")
public class ProjectController {


    private final FundingService fundingService;
    private ProjectService projectService;
    private UserService userService;

    public ProjectController(ProjectService projectService, UserService userService, FundingService fundingService) {
        this.projectService = projectService;
        this.userService = userService;
        this.fundingService = fundingService;
    }

    @GetMapping("")
   public String showProjects(Model model) {
        List<Project> projects = projectService.getApprovedProjects();
        model.addAttribute("projects", projects);
        return "projects/projects";
   }

    @GetMapping("/{id}")
    public String showProject(@PathVariable Integer id, Model model){
        Project project = projectService.getProject(id);
        if (project.getStatus() != ProjectStatus.APPROVED) {
            throw new RuntimeException("Access denied: Project is not approved.");
        }
        model.addAttribute("projects", project);
        return "projects/projects";
    }

    @Secured("ROLE_FOUNDER")
    @GetMapping("/new")
    public String addProject(Model model){
        Project project= new Project();
        model.addAttribute("project", project);
        return "projects/project";
    }

    @Secured("ROLE_FOUNDER")
    @PostMapping("/new")
    public String saveProject(@Valid @ModelAttribute("project") Project project, Model model, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("error");
            return "projects/project";
        }else {
            String username = principal.getName();
            System.out.println(username);
            User founder = userService.findUserByUsername(username);
            project.setFounder(founder);
            projectService.saveProject(project);
            founder.getProjects().add(project);
            model.addAttribute("projects", projectService.getProjects());
            return "projects/myprojects";
        }
    }

    @Secured("ROLE_SUPPORTER")
    @GetMapping("/donate/{id}")
    public String donateProject(@PathVariable Integer id, Model model){
        Project project = projectService.getProject(id);
        if (project.getStatus() != ProjectStatus.APPROVED) {
            throw new RuntimeException("Access denied: Project is not approved.");
        }
        model.addAttribute("project", project);
        return "projects/donate";
    }

    @Secured("ROLE_SUPPORTER")
    @PostMapping("/donate/{id}")
    public String donateProject(@PathVariable Integer id, @RequestParam("amount") Double amount, Model model, Principal principal) {
        Project project = projectService.getProject(id);
        String username = principal.getName();
        User supporter = userService.findUserByUsername(username);
        Funding funding = new Funding(amount);
        funding.setFundedProject(project);
        funding.setFundedBy(supporter);
        fundingService.saveFunding(funding);
        project.getFundings().add(funding);
        projectService.saveProject(project);
        Project updatedProject = projectService.getProject(id);
        model.addAttribute("projects", updatedProject);
        return "redirect:/projects/" + id;
    }

    }


