package gr.hua.dit.ds.Crowdfundingapp.repository;

import gr.hua.dit.ds.Crowdfundingapp.Entities.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FundingRepository extends JpaRepository<Funding, Integer> {
}
