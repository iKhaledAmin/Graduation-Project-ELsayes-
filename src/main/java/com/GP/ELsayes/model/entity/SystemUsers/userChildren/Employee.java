package com.GP.ELsayes.model.entity.SystemUsers.userChildren;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.enums.roles.EmployeeRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.Date;
import java.util.function.ToDoubleFunction;



@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")



@Entity
@Table(name = "employee")
@PrimaryKeyJoinColumn(name = "employee_id")
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Employee extends User {
    @Id
    private Long id;
    private String baseSalary;
    private String bonus;
    private double totalSalary;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfEmployment;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date DateOfResignation;



    public void setTotalSalary(ToDoubleFunction<Employee> salaryCalculator) {
        double calculatedSalary = salaryCalculator.applyAsDouble(this);
        this.totalSalary = calculatedSalary;
    }
}
