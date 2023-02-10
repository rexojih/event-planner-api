package com.ojih.rex.eventplanner.model;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String streetAddress;
    @NonNull
    private String city;
    private String state;
    private String postalCode;
}
