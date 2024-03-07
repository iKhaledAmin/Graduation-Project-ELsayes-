package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfOffersResponse;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import com.GP.ELsayes.model.entity.relations.ServicesOfOffers;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.relations.ServicesOfBranchesMapper;
import com.GP.ELsayes.model.mapper.relations.ServicesOfOffersMapper;
import com.GP.ELsayes.repository.relations.ServicesOfBranchesRepo;
import com.GP.ELsayes.repository.relations.ServicesOfOffersRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.OfferService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ServicesOfOffersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServicesOfOffersServiceImpl implements ServicesOfOffersService {

    private final ServicesOfOffersRepo servicesOfOffersRepo;
    private final ServicesOfOffersMapper servicesOfOffersMapper;
    private final ServiceService serviceService;
    private final OfferService offerService;


    public ServicesOfOffersServiceImpl(ServicesOfOffersRepo servicesOfOffersRepo, ServicesOfOffersMapper servicesOfOffersMapper,@Lazy ServiceService serviceService,@Lazy OfferService offerService) {
        this.servicesOfOffersRepo = servicesOfOffersRepo;
        this.servicesOfOffersMapper = servicesOfOffersMapper;
        this.serviceService = serviceService;
        this.offerService = offerService;
    }

    private ServicesOfOffers getByServiceIdAndBranchId(Long serviceId , Long offerId) {
        return servicesOfOffersRepo.findByServiceIdAndOfferId(serviceId,offerId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId + " in this offer")
        );
    }


    private void throwExceptionIfServiceHasAlreadyExistedInOffer(Long serviceId , Long offerId){
        Optional<ServicesOfOffers> servicesOfOffer = servicesOfOffersRepo.findByServiceIdAndOfferId(serviceId,offerId);
        if(servicesOfOffer.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ servicesOfOffer.get().getService().getId() +" already existed in this offer");
    }

    @Override
    public ServicesOfOffersResponse addServiceToOffer(Long serviceId, Long offerId) {
        throwExceptionIfServiceHasAlreadyExistedInOffer(serviceId,offerId);

        ServicesOfOffers servicesOfOffer = new ServicesOfOffers();

        ServiceEntity service = serviceService.getById(serviceId);
        Offer offer = offerService.getById(offerId);

        List<ServiceEntity> offerServicesList = serviceService.getAllByOfferId(offerId);

        //double newActualTotalRequiredTime = (offerService.calculateActualTotalRequiredTime(offerServicesList)) + service.getRequiredTime();


       // offerService.updateActualTotalRequiredTime(actualTotalRequiredTime + service.getRequiredTime());

        servicesOfOffer.setService(service);
        servicesOfOffer.setOffer(offer);
        servicesOfOffer.setAddingDate(new Date());

        servicesOfOffersRepo.save(servicesOfOffer);

        return servicesOfOffersMapper.toResponse(servicesOfOffer);
    }

    @SneakyThrows
    private ServicesOfOffers update(ServicesOfOffers servicesOfOffer){

        ServicesOfOffers updatedServicesOfOffer = servicesOfOffer;
        ServicesOfOffers existedServicesOfOffer = this.getByServiceIdAndBranchId(
                servicesOfOffer.getService().getId(),
                servicesOfOffer.getOffer().getId()
        );


        updatedServicesOfOffer.setId(existedServicesOfOffer.getId());
        BeanUtils.copyProperties(updatedServicesOfOffer,existedServicesOfOffer);

        return servicesOfOffersRepo.save(updatedServicesOfOffer);
    }

}
