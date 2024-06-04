package com.example.IgolAuthService.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerSignupRequestDto {

    private String email;

    private String password;

    private String phoneNumber;

    private String name;

}
