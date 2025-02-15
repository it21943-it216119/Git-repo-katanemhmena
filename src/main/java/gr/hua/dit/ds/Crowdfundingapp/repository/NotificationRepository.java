package gr.hua.dit.ds.Crowdfundingapp.repository;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Notification;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByRelatedProject(Project project);
    List<Notification> findByRelatedProjectIn(List<Project> projects);

}
