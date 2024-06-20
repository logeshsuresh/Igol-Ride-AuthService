package com.example.IgolAuthService.controllers;

import com.example.IgolAuthService.dtos.AuthRequestDto;
import com.example.IgolAuthService.dtos.AuthResponseDto;
import com.example.IgolAuthService.dtos.PassengerDto;
import com.example.IgolAuthService.dtos.PassengerSignupRequestDto;
import com.example.IgolAuthService.services.AuthService;
import com.example.IgolAuthService.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${cookie.expiry}")
    private Integer COOKIE_EXPIRY;

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = this.authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signin(@RequestBody AuthRequestDto authRequestDto,
                                    HttpServletResponse httpServletResponse) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getEmail(), authRequestDto.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());
            ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)
                                    .httpOnly(false)
                                    .secure(false)
                                    .maxAge(7L * 24 * COOKIE_EXPIRY)
                                    .path("/")
                                    .build();
            httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new ResponseEntity<>(AuthResponseDto.builder().success(true).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Unsuccessfull Auth", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest httpServletRequest) {
        for (Cookie cookie : httpServletRequest.getCookies()) {
            System.out.println(cookie.getName() + " " + cookie.getValue());
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}