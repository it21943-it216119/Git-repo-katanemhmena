package gr.hua.dit.ds.Crowdfundingapp.Entities;


import jakarta.persistence.*;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String message;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="related_project_id")
    private Project relatedProject;

    public Notification(String message) {
        this.message = message;
    }

    public Notification() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public Project getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(Project relatedProject) {
        this.relatedProject = relatedProject;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "message='" + message + '\'' +
                '}';
    }
}
