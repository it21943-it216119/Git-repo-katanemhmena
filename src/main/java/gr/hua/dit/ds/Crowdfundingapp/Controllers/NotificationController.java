package gr.hua.dit.ds.Crowdfundingapp.Controllers;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Notification;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.service.FundingService;
import gr.hua.dit.ds.Crowdfundingapp.service.NotificationService;
import gr.hua.dit.ds.Crowdfundingapp.service.ProjectService;
import gr.hua.dit.ds.Crowdfundingapp.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("notifications")
public class NotificationController {

    private NotificationService notificationService;
    private UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Secured("ROLE_SUPPORTER")
    @GetMapping("")
    public String showMyNotifications(Model model, Principal principal) {
        // Get the logged-in user
        String username = principal.getName();
        User user = userService.findUserByUsername(username);

        // Get all notifications for projects the user has donated to
        List<Notification> notifications = notificationService.getNotificationsForUser(user);
        Collections.reverse(notifications);

        // Add the notifications to the model
        model.addAttribute("notifications", notifications);

        return "notifications/mynotifications";
    }

}
