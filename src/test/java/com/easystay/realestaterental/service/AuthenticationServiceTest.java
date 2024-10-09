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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

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

    private RegistrationRequestDTO registrationRequest;
    private LoginRequestDTO loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequestDTO();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password");
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setRole(UserRole.GUEST);

        loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        user = new Guest();
        user.setEmail("test@example.com");
        user.setRole(UserRole.GUEST);
    }

    @Test
    void register_Guest_ShouldReturnAuthResponseDTO() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(guestRepository.save(any(Guest.class))).thenReturn((Guest) user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponseDTO result = authenticationService.register(registrationRequest);

        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(UserRole.GUEST.toString(), result.getUserRole());
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void register_Host_ShouldReturnAuthResponseDTO() {
        registrationRequest.setRole(UserRole.HOST);
        user = new Host();
        user.setRole(UserRole.HOST);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(hostRepository.save(any(Host.class))).thenReturn((Host) user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponseDTO result = authenticationService.register(registrationRequest);

        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(UserRole.HOST.toString(), result.getUserRole());
        verify(hostRepository).save(any(Host.class));
    }

    @Test
    void register_ExistingEmail_ShouldThrowDuplicateResourceException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authenticationService.register(registrationRequest));
    }

    @Test
    void login_ValidCredentials_ShouldReturnAuthResponseDTO() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponseDTO result = authenticationService.login(loginRequest);

        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(UserRole.GUEST.toString(), result.getUserRole());
    }

    @Test
    void login_InvalidCredentials_ShouldThrowBadRequestException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadRequestException("Invalid email or password."));

        assertThrows(BadRequestException.class, () -> authenticationService.login(loginRequest));
    }
}