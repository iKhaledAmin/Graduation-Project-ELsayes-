package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.CarMapper;
import com.GP.ELsayes.repository.CarRepo;
import com.GP.ELsayes.service.CarService;
import com.GP.ELsayes.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

    private final CarRepo carRepo;
    private final CarMapper carMapper;
    private final CustomerService customerService;

    void throwExceptionIfCustomerAlreadyHasCar(Long customerId){
        Optional<Car> car = carRepo.findByCustomerId(customerId);
        if(car.isEmpty())
            return;
        throw new RuntimeException("This customer with id = "+ customerId +" already has a car");
    }

    void throwExceptionIfCarIsAlreadyExist(String carPlateNumber){
        Optional<Car> car = carRepo.findByCarPlateNumber(carPlateNumber);
        if(car.isEmpty()){
            return;
        }
        throw new RuntimeException("Invalid car plate number");
    }

    @Override
    public CarResponse add(CarRequest carRequest) {
        throwExceptionIfCarIsAlreadyExist(carRequest.getCarPlateNumber());

        Car car = this.carMapper.toEntity(carRequest);

        Customer customer = this.customerService.getById(carRequest.getCustomerId());

        throwExceptionIfCustomerAlreadyHasCar(customer.getId());
        car.setCustomer(customer);
        return this.carMapper.toResponse(this.carRepo.save(car));

    }

    @SneakyThrows
    @Override
    public CarResponse update(CarRequest carRequest, Long carId) {
        Car existedCar = this.getById(carId);
        Car updatedCar = this.carMapper.toEntity(carRequest);


        updatedCar.setId(carId);
        updatedCar.setCustomer(existedCar.getCustomer());
        BeanUtils.copyProperties(existedCar,updatedCar);

        return this.carMapper.toResponse(carRepo.save(updatedCar));
    }

    @Override
    public void delete(Long carId) {
        this.getById(carId);
        carRepo.deleteById(carId);
    }

    @Override
    public List<CarResponse> getAll() {
        return carRepo.findAll()
                .stream()
                .map(car ->  carMapper.toResponse(car))
                .toList();
    }

    @Override
    public Car getById(Long carId) {
        return carRepo.findById(carId).orElseThrow(
                () -> new NoSuchElementException("There is no car with id = " + carId)
        );
    }

    @Override
    public Car getByCarPlateNumber(String carPlateNumber) {
        return carRepo.findByCarPlateNumber(carPlateNumber).orElseThrow(
                () -> new NoSuchElementException("There is no car with car plate number = " + carPlateNumber)
        );
    }

    @Override
    public CarResponse getResponseById(Long carId) {
        return carMapper.toResponse(getById(carId));
    }
}
