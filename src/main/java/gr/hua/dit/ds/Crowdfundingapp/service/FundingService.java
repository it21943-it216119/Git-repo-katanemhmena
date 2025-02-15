package gr.hua.dit.ds.Crowdfundingapp.service;


import gr.hua.dit.ds.Crowdfundingapp.Entities.Funding;
import gr.hua.dit.ds.Crowdfundingapp.Entities.Project;
import gr.hua.dit.ds.Crowdfundingapp.Entities.User;
import gr.hua.dit.ds.Crowdfundingapp.repository.FundingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundingService {

    private FundingRepository fundingRepository;

    public FundingService(FundingRepository fundingRepository) {
        this.fundingRepository = fundingRepository;
    }

    @Transactional
    public List<Funding> getFundings() {
        return fundingRepository.findAll();
    }

    @Transactional
    public Funding getFunding(Integer funding_id){
        return fundingRepository.findById(funding_id).get();
    }

    @Transactional
    public void saveFunding(Funding funding) {
        fundingRepository.save(funding);
    }

    @Transactional
    public List<Funding> findFundingsBySupporter(User supporter) {
        return fundingRepository.findByFundedBy(supporter);
    }

    @Transactional
    public List<Funding> findFundingsByProject(Project project) {
        return fundingRepository.findByFundedProject(project);
    }



}
