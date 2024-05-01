package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.AddCarRequest;
import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.entity.Car;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public interface CarService extends CrudService<CarRequest, Car, CarResponse,Long> {
    public Car add(Car car);
    public Car update(Car updatedCar);
    public Car getByCarPlateNumber(String carPlateNumber);
    public Optional<Car> getIfExistByCarPlateNumber(String carPlateNumber);
    public CarResponse addCarToCustomer(AddCarRequest addCarRequest);


    public Optional<Car> getEntityByCustomerId(Long customerId);
    public Car getByCustomerId(Long customerId);
    public CarResponse getResponseByCustomerId(Long customerId);



}
