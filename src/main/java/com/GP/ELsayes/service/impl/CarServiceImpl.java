package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.AddCarRequest;
import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
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


    private void throwExceptionIfCarInsideBranch(String carPlateNumber){
        Optional<Car> car = carRepo.findByCarPlateNumber(carPlateNumber);
        Optional<VisitationsOfBranches> VisitationsOfBranches =customerService.getVisitationsOfBranchByCustomerId(car.get().getCustomer().getId());
        if(VisitationsOfBranches.isEmpty()){
            return;
        }
        throw new RuntimeException("Car with number = " +carPlateNumber+ " still in branch ,can't delete");
    }

    @Override
    public Car add(Car car){
        throwExceptionIfCarIsAlreadyExist(car.getCarPlateNumber());
        return this.carRepo.save(car);
    }

    @SneakyThrows
    public Car update(Car updatedCar){
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
    public CarResponse addCarToCustomer(AddCarRequest addCarRequest){
        Optional<Car> car = carRepo.findByCarPlateNumber(addCarRequest.getCarPlateNumber());
        if(car.isEmpty()){
            car = Optional.of(new Car());
            car.get().setCarPlateNumber(addCarRequest.getCarPlateNumber());
            car = Optional.of(carRepo.save(car.get()));
        }
        Customer customer = customerService.getById(addCarRequest.getCustomerId());

        throwExceptionIfCustomerAlreadyHasCar(customer.getId());
        car.get().setCustomer(customer);
        car.get().setCarType(addCarRequest.getCarType());
        car.get().setModel(addCarRequest.getModel());

        car = Optional.ofNullable(update(car.get()));
        return carMapper.toResponse(car.get());
    }






    @Override
    public void delete(Long carId) {

        Car car = this.getById(carId);
        throwExceptionIfCarInsideBranch(car.getCarPlateNumber());
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
    public Optional<Car> getObjectById(Long carId) {
        return carRepo.findById(carId);
    }

    @Override
    public Car getById(Long carId) {
        return getObjectById(carId).orElseThrow(
                () -> new NoSuchElementException("There is no car with id = " + carId)
        );
    }

    @Override
    public CarResponse getResponseById(Long carId) {
        return carMapper.toResponse(getById(carId));
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
    public Optional<Car> getEntityByCustomerId(Long customerId) {
        return carRepo.findByCustomerId(customerId);
    }

    @Override
    public Car getByCustomerId(Long customerId) {
        return getEntityByCustomerId(customerId).orElseThrow(
                () -> new NoSuchElementException("There is no car with customer id = " + customerId)
        );
    }

    @Override
    public CarResponse getResponseByCustomerId(Long customerId) {
        return carMapper.toResponse(getByCustomerId(customerId));
    }


}
