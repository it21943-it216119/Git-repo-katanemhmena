package gr.hua.dit.ds.Crowdfundingapp.repository;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
