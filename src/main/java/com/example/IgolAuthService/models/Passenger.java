package com.example.IgolAuthService.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "bookings"})
public class Passenger  extends BaseModel {

    private String name;

    @OneToMany(mappedBy = "passenger", fetch = FetchType.LAZY)
    private List<Booking> bookings;

}
