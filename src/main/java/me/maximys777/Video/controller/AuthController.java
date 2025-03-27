package me.maximys777.Video.controller;

import me.maximys777.Video.config.JWTGenerator;
import me.maximys777.Video.dto.request.LoginRequest;
import me.maximys777.Video.dto.response.AuthResponse;
import me.maximys777.Video.entity.RoleEntity;
import me.maximys777.Video.entity.UserEntity;
import me.maximys777.Video.repository.RoleRepository;
import me.maximys777.Video.repository.UserRepository;
import me.maximys777.Video.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@RestController
@RequestMapping("/SAO/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, CloudinaryService cloudinaryService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam("name") String name,
                                           @RequestParam("password") String password,
                                           @RequestParam(value = "profilePic", required = false) MultipartFile profilePicFile) {
        // Проверка на уникальность имени
        if(userRepository.existsByName(name)) {
            return new ResponseEntity<>("Имя пользователя занято.", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));

        if (profilePicFile != null && !profilePicFile.isEmpty()) {
            String profilePicUrl = cloudinaryService.uploadFile(profilePicFile, "user_profile_pics");
            user.setProfilePic(profilePicUrl);
        }

        // По умолчанию роль USER
        RoleEntity role = roleRepository.findByName("USER").orElseThrow(() ->
                new RuntimeException("Роль USER не найдена."));
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
        return new ResponseEntity<>("Пользователь успешно зарегистрирован.", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }
}
