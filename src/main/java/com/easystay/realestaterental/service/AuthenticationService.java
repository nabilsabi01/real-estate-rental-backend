package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.AuthResponseDTO;
import com.easystay.realestaterental.dto.LoginRequestDTO;
import com.easystay.realestaterental.dto.RegistrationRequestDTO;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Host;
import com.easystay.realestaterental.entity.User;
import com.easystay.realestaterental.enums.UserRole;
import com.easystay.realestaterental.exception.BadRequestException;
import com.easystay.realestaterental.exception.DuplicateResourceException;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.HostRepository;
import com.easystay.realestaterental.repository.UserRepository;
import com.easystay.realestaterental.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(RegistrationRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Cet email existe déjà");
        }

        User user;
        if ("host".equalsIgnoreCase(request.getRole())) {
            user = new Host();
            user.setRole(UserRole.HOST);
        } else if ("guest".equalsIgnoreCase(request.getRole())) {
            user = new Guest();
            user.setRole(UserRole.GUEST);
        } else {
            throw new BadRequestException("Rôle invalide");
        }

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        if (user instanceof Host) {
            hostRepository.save((Host) user);
        } else {
            guestRepository.save((Guest) user);
        }

        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().toString());
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            return new AuthResponseDTO(token, user.getRole().toString());
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password");
        }
    }
}