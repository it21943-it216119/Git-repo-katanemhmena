package gr.hua.dit.ds.Crowdfundingapp.service;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Funding;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Notification;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    @Autowired
    private FundingService fundingService;

    public NotificationService(NotificationRepository notificationRepository, FundingService fundingService) {
        this.notificationRepository = notificationRepository;
        this.fundingService = fundingService;
    }

    @Transactional
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public List<Notification> getNotificationsByProject(Project project) {
        return notificationRepository.findByRelatedProject(project);
    }

    public List<Notification> getNotificationsForUser(User user) {

        List<Funding> fundings = fundingService.findFundingsBySupporter(user);


        Set<Project> donatedProjects = fundings.stream()
                .map(Funding::getFundedProject)
                .collect(Collectors.toSet());

        // Fetch notifications for these projects
        return notificationRepository.findByRelatedProjectIn(new ArrayList<>(donatedProjects));
    }
}