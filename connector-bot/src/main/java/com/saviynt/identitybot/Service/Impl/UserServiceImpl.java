package com.saviynt.identitybot.Service.Impl;

import java.util.*;
import java.util.stream.Collectors;

import com.saviynt.identitybot.Service.EmailService;
import com.saviynt.identitybot.Service.UserService;
import com.saviynt.identitybot.constants.EmailConstants;
import com.saviynt.identitybot.exceptions.RecordNotFoundException;
import com.saviynt.identitybot.payload.ApiResponse;
import com.saviynt.identitybot.payload.CompletePasswordResetRequest;
import com.saviynt.identitybot.payload.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saviynt.identitybot.model.Role;
import com.saviynt.identitybot.model.User;
import com.saviynt.identitybot.payload.UserRegistrationDto;
import com.saviynt.identitybot.repository.UserRepository;

import javax.transaction.Transactional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailConstants emailConstants;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public ApiResponse getAllUsers() {
//        log.info("=== started listing all the users ===");
        List<User> users = this.userRepository.findAll();
        if (!users.isEmpty()) {
            return new ApiResponse(users, true, "");
        }
//        log.info("=== successfully fetched the sonimGroup list ===");
        return null;
    }

    @Override
    public User upate(UserUpdateDto userRegistrationDto, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user=userOptional.get();
            if(userRegistrationDto.getFirstName()!=null) user.setFirstName(userRegistrationDto.getFirstName());
            if(userRegistrationDto.getLastName()!=null)user.setLastName(userRegistrationDto.getLastName());
            if(userRegistrationDto.getPassword()!=null)user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

            return userRepository.save(user);
        }
        throw new  RecordNotFoundException("User with id "+id+" does not exists in system");
    }

    @Override
    @Transactional
    public ApiResponse initiateResetPassword(String email) {
        ApiResponse apiResponse = new ApiResponse(true, "Password reset email sent successfully.");
        Optional<User> user = userRepository.findOneByEmail(email);
        if (user.isPresent()) {
            String token = String.valueOf(UUID.randomUUID());
            String emailContent = emailConstants.getPasswordResetEmailContent().
                    replace("PASSWORD_RESET_URL", emailConstants.getPasswordResetUrl() + token);

            // send out password reset usernameOrEmail
            emailService.sendText(user.get().getEmail(), emailConstants.getPasswordResetEmailSubject(), emailContent);
            user.get().setToken(token);
            userRepository.save(user.get());

        } else {
            apiResponse = new ApiResponse(false, "Could not process the password reset request.");
        }
        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse completeResetPassword(CompletePasswordResetRequest completePasswordResetRequest) {
        ApiResponse apiResponse = new ApiResponse(true, "Password reset completed successfully.");
        Optional<User> userOptional = userRepository.findByToken(completePasswordResetRequest.getToken());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(completePasswordResetRequest.getPassword()));
            user.setToken(null);
            userRepository.save(user);

            emailService.sendText(user.getEmail(), emailConstants.getPasswordResetSuccessEmailSubject(),
                    emailConstants.getPasswordResetSuccessEmailContent());

        } else {
            apiResponse = new ApiResponse(false, "Could not process the password reset request.");
        }
        return apiResponse;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}