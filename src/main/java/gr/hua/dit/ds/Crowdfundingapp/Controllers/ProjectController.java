package gr.hua.dit.ds.Crowdfundingapp.Controllers;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.ProjectStatus;
import gr.hua.dit.ds.Crowdfundingapp.service.ProjectService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("projects")
public class ProjectController {


    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("")
   public String showProjects(Model model) {
        List<Project> projects = projectService.getProjects();
        model.addAttribute("projects", projects);
        return "projects/projects";
   }

    @GetMapping("/{id}")
    public String showProject(@PathVariable Integer id, Model model){
        Project project = projectService.getProject(id);
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
    public String saveProject(@ModelAttribute("project") Project project, Model model) {
        projectService.saveProject(project);
        model.addAttribute("projects", projectService.getProjects());
        return "projects/projects";
    }

    @Secured("ROLE_SUPPORTER")
    @GetMapping("/donate/{id}")
    public String donateProject(@PathVariable Integer id, Model model){
        Project project = projectService.getProject(id);
        model.addAttribute("project", project);
        return "projects/donate";
    }

    @Secured("ROLE_SUPPORTER")
    @PostMapping("/donate/{id}")
    public String processDonation(@PathVariable Integer id, @RequestParam("amount") Double amount, Model model) {
        Project project = projectService.getProject(id);
        if (project != null) {
            Double updatedFunding = project.getCurrentFunding() + amount;
            project.setCurrentFunding(updatedFunding);
        }

        model.addAttribute("projects", project);

        return "projects/projects";
    }

    }


