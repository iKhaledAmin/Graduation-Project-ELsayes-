package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.entity.Offer;

public interface OfferService extends CrudService<OfferRequest, Offer, OfferResponse,Long> {
}
