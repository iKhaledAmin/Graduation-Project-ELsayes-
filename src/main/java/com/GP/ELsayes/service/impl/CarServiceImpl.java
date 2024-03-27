package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.AddCarToCustomerRequest;
import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
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

    private void throwExceptionIfCustomerAlreadyHasCar(Long customerId){
        Optional<Car> car = carRepo.findByCustomerId(customerId);
        if(car.isEmpty())
            return;
        throw new RuntimeException("This customer with id = "+ customerId +" already has a car");
    }

    private void throwExceptionIfCarIsAlreadyExist(String carPlateNumber){
        Optional<Car> car = carRepo.findByCarPlateNumber(carPlateNumber);
        if(car.isEmpty()){
            return;
        }
        throw new RuntimeException("Invalid car plate number");
    }

    @Override
    public Car add(Car car){
        throwExceptionIfCarIsAlreadyExist(car.getCarPlateNumber());
        return this.carRepo.save(car);
    }

    @SneakyThrows
    public Car update(Car updatedCar){
        System.out.println("The id   " + updatedCar.getId());
        Optional<Car> existedCar = carRepo.findById(updatedCar.getId());

        updatedCar.setId(updatedCar.getId());
        BeanUtils.copyProperties(existedCar,updatedCar);

        Customer customer = updatedCar.getCustomer();
        if (Optional.ofNullable(customer).isPresent()){
            updatedCar.setCustomer(customer);
        }


        return carRepo.save(updatedCar);
    }


    @Override
    public CarResponse add(CarRequest carRequest) {
        //throwExceptionIfCarIsAlreadyExist(carRequest.getCarPlateNumber());

        Car car = this.carMapper.toEntity(carRequest);

        return this.carMapper.toResponse(add(car));

    }


    @Override
    public CarResponse update(CarRequest carRequest, Long carId) {

        Car updatedCar = this.carMapper.toEntity(carRequest);
        updatedCar.setId(carId);

        Customer customer = customerService.getById(carRequest.getCustomerId());
        if (Optional.ofNullable(customer).isPresent()){
            updatedCar.setCustomer(customer);
        }

        updatedCar = update(updatedCar);

        return this.carMapper.toResponse(updatedCar);
    }

    @Override
    public CarResponse addCarToCustomer(AddCarToCustomerRequest addCarToCustomerRequest){
        Car car = getByCarPlateNumber(addCarToCustomerRequest.getCarPlateNumber());
        Customer customer = customerService.getById(addCarToCustomerRequest.getCustomerId());

        throwExceptionIfCustomerAlreadyHasCar(customer.getId());
        car.setCustomer(customer);
        car.setCarType(addCarToCustomerRequest.getCarType());
        car.setModel(addCarToCustomerRequest.getModel());

        car = update(car);
        return carMapper.toResponse(car);
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
    public Optional<Car> getIfExistByCarPlateNumber(String carPlateNumber) {
        return carRepo.findByCarPlateNumber(carPlateNumber);
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
