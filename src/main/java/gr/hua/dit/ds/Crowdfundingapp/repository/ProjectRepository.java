package gr.hua.dit.ds.Crowdfundingapp.repository;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>{
    List<Project> findByFounder(User founder);

}
