package gr.hua.dit.ds.Crowdfundingapp.repository;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>{
}
