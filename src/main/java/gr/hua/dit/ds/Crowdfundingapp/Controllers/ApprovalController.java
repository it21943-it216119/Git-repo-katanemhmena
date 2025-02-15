package gr.hua.dit.ds.Crowdfundingapp.Controllers;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import gr.hua.dit.ds.Crowdfundingapp.service.ProjectService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("approval")
public class ApprovalController {

    @Autowired
    private ProjectService projectService;


    public ApprovalController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("")
    public String showProjectsForApproval(Model model) {

        List<Project> projects = projectService.getProjects();


        model.addAttribute("projects", projects);

        return "admin/projects";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/update-status/{id}")
    public String updateProjectStatus(@PathVariable("id") Integer projectId, @RequestParam ProjectStatus status, Model model, RedirectAttributes redirectAttributes) {

        projectService.updateProjectStatus(projectId, status);


        redirectAttributes.addFlashAttribute("success", "Project status updated successfully!");
        return "redirect:/approval";
    }

}
