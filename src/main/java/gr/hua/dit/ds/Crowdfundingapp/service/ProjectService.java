package gr.hua.dit.ds.Crowdfundingapp.service;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.ProjectStatus;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


import java.util.List;
import java.util.Optional;

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

    @Transactional
    public List<Project> findProjectsByFounder(User founder) {
        return projectRepository.findByFounder(founder);
    }

    @Transactional
    public void deleteProject(Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void updateProject(Integer projectId, String newDescription, Double newFundingGoal) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " not found"));


        if (newDescription != null && !newDescription.trim().isEmpty()) {
            project.setDescription(newDescription);
        }
        if (newFundingGoal != null && newFundingGoal < project.getFundingGoal()) {
            throw new IllegalArgumentException("New funding goal must be greater or equal to the current funding goal");
        }
        if (newFundingGoal != null) {
            project.setFundingGoal(newFundingGoal);
        }


        projectRepository.save(project);
    }

    @Transactional
    public void updateProjectStatus(Integer projectId, ProjectStatus status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(status);
        projectRepository.save(project);
    }

    @Transactional
    public List<Project> getApprovedProjects() {
        return projectRepository.findAll()
                .stream()
                .filter(project -> project.getStatus() == ProjectStatus.APPROVED)
                .toList();
    }
}


