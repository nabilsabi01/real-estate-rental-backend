package com.easystay.realestaterental.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.easystay.realestaterental.dto.AuthResponseDTO;
import com.easystay.realestaterental.dto.LoginRequestDTO;
import com.easystay.realestaterental.dto.RegistrationRequestDTO;
import com.easystay.realestaterental.entity.User;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Host;
import com.easystay.realestaterental.enums.UserRole;
import com.easystay.realestaterental.exception.BadRequestException;
import com.easystay.realestaterental.exception.DuplicateResourceException;
import com.easystay.realestaterental.repository.UserRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.HostRepository;
import com.easystay.realestaterental.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private HostRepository hostRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegistrationRequestDTO guestRegistrationRequest;
    private RegistrationRequestDTO hostRegistrationRequest;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        guestRegistrationRequest = new RegistrationRequestDTO();
        guestRegistrationRequest.setEmail("guest@example.com");
        guestRegistrationRequest.setPassword("password");
        guestRegistrationRequest.setRole(UserRole.GUEST);
        guestRegistrationRequest.setFirstName("John");
        guestRegistrationRequest.setLastName("Doe");

        hostRegistrationRequest = new RegistrationRequestDTO();
        hostRegistrationRequest.setEmail("host@example.com");
        hostRegistrationRequest.setPassword("password");
        hostRegistrationRequest.setRole(UserRole.HOST);
        hostRegistrationRequest.setFirstName("Jane");
        hostRegistrationRequest.setLastName("Smith");

        loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");
    }

    @Test
    void testRegisterGuest_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(guestRepository.save(any(Guest.class))).thenReturn(new Guest());

        AuthResponseDTO response = authenticationService.register(guestRegistrationRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("GUEST", response.getUserRole());
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void testRegisterHost_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(hostRepository.save(any(Host.class))).thenReturn(new Host());

        AuthResponseDTO response = authenticationService.register(hostRegistrationRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("HOST", response.getUserRole());
        verify(hostRepository).save(any(Host.class));
    }

    @Test
    void testRegister_DuplicateEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authenticationService.register(guestRegistrationRequest));
    }

    @Test
    void testRegister_InvalidRole() {
        RegistrationRequestDTO invalidRequest = new RegistrationRequestDTO();
        invalidRequest.setEmail("invalid@example.com");
        invalidRequest.setPassword("password");
        invalidRequest.setRole(null);

        assertThrows(BadRequestException.class, () -> authenticationService.register(invalidRequest));
    }

    @Test
    void testLogin_Success() {
        Authentication authentication = mock(Authentication.class);
        User user = new Guest();
        user.setEmail("user@example.com");
        user.setRole(UserRole.GUEST);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthResponseDTO response = authenticationService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("GUEST", response.getUserRole());
    }

    @Test
    void testLogin_Failure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadRequestException("Invalid email or password"));

        assertThrows(BadRequestException.class, () -> authenticationService.login(loginRequest));
    }
}