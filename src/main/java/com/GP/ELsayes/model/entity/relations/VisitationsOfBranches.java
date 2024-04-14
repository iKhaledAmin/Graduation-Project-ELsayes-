package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "visitation_of_branches")
public class VisitationsOfBranches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateOfArriving;
    private Date dateOfLeaving;
    //@Convert(converter = DurationConverter.class) // Specify the custom converter
    private Duration period;

//    // Custom converter for Duration to Long
//    @Converter
//    public static class DurationConverter implements AttributeConverter<Duration, Long> {
//        @Override
//        public Long convertToDatabaseColumn(Duration duration) {
//            return duration.toHours(); // Convert to seconds (or any other suitable representation)
//        }
//
//        @Override
//        public Duration convertToEntityAttribute(Long seconds) {
//            return Duration.ofHours(seconds); // Convert back to Duration
//        }
//    }


    @JsonBackReference
    @JoinColumn(name = "customer_id")
    @ManyToOne
    private Customer customer;

    @JsonBackReference
    @JoinColumn(name = "car_id")
    @ManyToOne
    private Car car;

    @JsonBackReference
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    @JsonBackReference
    @JoinColumn(name = "order_id")
    @OneToOne
    private Order order;

}
