package gr.hua.dit.ds.Crowdfundingapp.Controllers;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Notification;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.service.NotificationService;
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

@Controller
@RequestMapping("myprojects")
public class MyProjectsController {
    private ProjectService projectService;
    private UserService userService;
    private NotificationService notificationService;

    public MyProjectsController(ProjectService projectService, UserService userService, NotificationService notificationService) {
        this.projectService = projectService;
        this.userService = userService;
        this.notificationService = notificationService;
    }


    @Secured("ROLE_FOUNDER")
    @GetMapping("")
    public String showMyProjects(Model model, Principal principal) {
        String username = principal.getName();
        User founder = userService.findUserByUsername(username);
        List<Project> projects = projectService.findProjectsByFounder(founder);
        model.addAttribute("projects", projects);
        return "projects/myprojects";
    }


    @Secured("ROLE_FOUNDER")
    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Integer id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {

            Project project = projectService.getProject(id);


            String username = principal.getName();
            User founder = userService.findUserByUsername(username);

            if (!project.getFounder().equals(founder)) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this project.");
                return "redirect:/myprojects";
            }


            model.addAttribute("project", project);
            return "projects/deleteproject";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/myprojects";
        }
    }

    @Secured("ROLE_FOUNDER")
    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        try {

            Project project = projectService.getProject(id);

            String username = principal.getName();
            User founder = userService.findUserByUsername(username);

            if (!project.getFounder().equals(founder)) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this project.");
                return "redirect:/myprojects";
            }

            // Delete the project
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute("success", "Project deleted successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/myprojects";
    }

    @Secured("ROLE_FOUNDER")
    @GetMapping("/edit/{id}")
    public String editProjectForm(@PathVariable("id") Integer projectId, Model model) {
        Project project = projectService.getProject(projectId);
        model.addAttribute("project", project);
        return "projects/editproject";
    }

    @Secured("ROLE_FOUNDER")
    @PostMapping("/edit/{id}")
    public String editProject(@PathVariable("id") Integer projectId,
                              @RequestParam("description") String newDescription,
                              @RequestParam("fundingGoal") Double newFundingGoal,
                              RedirectAttributes redirectAttributes) {
        projectService.updateProject(projectId, newDescription, newFundingGoal);
        redirectAttributes.addFlashAttribute("success", "Project updated successfully!");
        return "redirect:/myprojects";
    }

    @Secured("ROLE_FOUNDER")
    @GetMapping("/notify/{id}")
    public String showNotificationForm(@PathVariable Integer id, Model model) {

        Project project = projectService.getProject(id);
        Notification notification = new Notification();
        List<Notification> notifications = notificationService.getNotificationsByProject(project);

        model.addAttribute("project", project);
        model.addAttribute("notifications", notifications);
        model.addAttribute("notification", notification);
        return "projects/notify";
    }

    @Secured("ROLE_FOUNDER")
    @PostMapping("/notify/{id}")
    public String createNotification(@PathVariable Integer id, @ModelAttribute("notification") Notification notification, Model model) {

        Project project = projectService.getProject(id);


        notification.setRelatedProject(project);

        notificationService.saveNotification(notification);

        // Redirect to the notification form with a success message
        model.addAttribute("success", "Notification created successfully!");
        return "redirect:/myprojects/notify/" + id;
    }






}
