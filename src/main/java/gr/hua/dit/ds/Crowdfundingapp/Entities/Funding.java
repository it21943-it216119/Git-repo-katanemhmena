package gr.hua.dit.ds.Crowdfundingapp.Entities;


import jakarta.persistence.*;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;

@Entity
@Table(name = "funding")
public class  Funding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private Double amount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="project_id")
    private Project fundedProject;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
   private User fundedBy;

    public Funding(Double amount) {
        this.amount = amount;
    }

    public Funding() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Project getFundedProject() {
        return fundedProject;
    }

    public void setFundedProject(Project fundedProject) {
        this.fundedProject = fundedProject;
    }

    public User getFundedBy() {
        return fundedBy;
    }

    public void setFundedBy(User fundedBy) {
        this.fundedBy = fundedBy;
    }

    @Override
    public String toString() {
        return "Funding{" +
                "amount=" + amount +
                '}';
    }
}
