package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.entity.Car;
import org.springframework.stereotype.Service;

@Service
public interface CarService extends CrudService<CarRequest, Car, CarResponse,Long> {
}
