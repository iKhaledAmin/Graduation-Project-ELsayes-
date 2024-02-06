package com.GP.ELsayes.model.entity.SystemUsers.userChildren;

import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.enums.roles.EmployeeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")



@Entity
@Table(name = "employee")
@PrimaryKeyJoinColumn(name = "employee-id")
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Employee extends User {
    @Id
    private Long id;
    private String baseSalary;
    private String bonus;
    private String totalSalary;

    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    //public abstract double setTotalSalary();
}
