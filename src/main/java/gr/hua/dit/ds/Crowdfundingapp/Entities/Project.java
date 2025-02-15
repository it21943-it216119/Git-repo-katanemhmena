package gr.hua.dit.ds.Crowdfundingapp.Entities;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    @Size(min = 20)
    private String description;

    @Column
    @NotNull
    private Double fundingGoal;

    @Column
    private Double currentFunding;


    @Enumerated(EnumType.STRING)
    @Column
    private ProjectStatus status = ProjectStatus.PENDING;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="founder_id")
    private User founder;

    @OneToMany(mappedBy = "fundedProject",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Funding> fundings = new ArrayList<>();

    @OneToMany(mappedBy = "relatedProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @PostLoad
    @PrePersist
    @PreUpdate
    private void updateCurrentFunding() {
        if (fundings == null || fundings.isEmpty()) {
            this.currentFunding = 0.0;
        } else {
            this.currentFunding = fundings.stream()
                    .mapToDouble(Funding::getAmount)
                    .sum();
        }
    }

    public Project(Integer id, String name, String description, Double fundingGoal, Double currentFunding, ProjectStatus status, User founder, List<Funding> fundings, List<Notification> notifications) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fundingGoal = fundingGoal;
        this.currentFunding = currentFunding;
        this.status = status;
        this.founder = founder;
        this.fundings = fundings;
        this.notifications = notifications;
    }

    public Project(String name, String description, Double fundingGoal) {
        this.name = name;
        this.description = description;
        this.fundingGoal = fundingGoal;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Project() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFundingGoal() {
        return fundingGoal;
    }

    public void setFundingGoal(Double fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    public Double getCurrentFunding() {
        return currentFunding;
    }

    public void setCurrentFunding(Double currentFunding) {
        this.currentFunding = currentFunding;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public User getFounder() {
        return founder;
    }

    public void setFounder(User founder) {
        this.founder = founder;
    }

    public List<Funding> getFundings() {
        return fundings;
    }

    public void setFundings(List<Funding> fundings) {
        this.fundings = fundings;
    }




    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fundingGoal=" + fundingGoal +
                ", currentFunding=" + currentFunding +
                ", status=" + status +
                '}';
    }
}