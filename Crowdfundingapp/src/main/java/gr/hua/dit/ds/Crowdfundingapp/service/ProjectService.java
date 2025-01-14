package gr.hua.dit.ds.Crowdfundingapp.service;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Transactional
    public List<Project> getProjects(){
        return projectRepository.findAll();
    }

    @Transactional
    public Project getProject(Integer project_id){
        return projectRepository.findById(project_id).get();
    }

    @Transactional
    public void saveProject(Project project) {
        projectRepository.save(project);
    }
}
