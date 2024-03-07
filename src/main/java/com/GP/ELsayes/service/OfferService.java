package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.entity.Offer;

import java.util.List;

public interface OfferService extends CrudService<OfferRequest, Offer, OfferResponse,Long> {

    public List<OfferResponse> getResponseAllOffersIncludeService(Long serviceId);
    public List<Offer> getAllOffersIncludeService(Long serviceId);


    public String calculateOriginalTotalRequiredTime(Long offerId);


    public String calculateOriginalTotalPrice(Long offerId);




    public double calculateAmountOfDiscount(Long offerId, String percentageOfDiscount);
    public String calculateActualOfferPrice(Long offerId , String percentageOfDiscount);
}