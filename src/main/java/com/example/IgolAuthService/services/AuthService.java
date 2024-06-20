package com.example.IgolAuthService.services;

import com.example.IgolAuthService.dtos.PassengerDto;
import com.example.IgolAuthService.dtos.PassengerSignupRequestDto;
import com.example.IgolEntityService.models.Passenger;
import com.example.IgolAuthService.repositories.PassengerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PassengerRepository passengerRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(PassengerRepository passengerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PassengerDto signupPassenger(PassengerSignupRequestDto passengerSignupRequestDto) {
        Passenger passenger = Passenger.builder()
                                .email(passengerSignupRequestDto.getEmail())
                                .name(passengerSignupRequestDto.getName())
                                .password(bCryptPasswordEncoder.encode(passengerSignupRequestDto.getPassword()))
                                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                                .build();
        Passenger newPassenger = passengerRepository.save(passenger);
        return PassengerDto.from(newPassenger);
    }

}
