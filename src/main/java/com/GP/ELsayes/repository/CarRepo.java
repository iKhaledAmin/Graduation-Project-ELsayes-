package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepo extends JpaRepository<Car,Long> {

    Optional<Car> findByCarPlateNumber(String carPlateNumber);

    Optional<Car> findByCustomerId(Long customerId);

}
