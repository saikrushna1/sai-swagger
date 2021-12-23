package com.saviynt.identitybot.Service;

import com.saviynt.identitybot.payload.ApiResponse;
import com.saviynt.identitybot.payload.CompletePasswordResetRequest;
import com.saviynt.identitybot.payload.UserUpdateDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.saviynt.identitybot.model.User;
import com.saviynt.identitybot.payload.UserRegistrationDto;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);

    ApiResponse getAllUsers();


    User upate(UserUpdateDto userRegistrationDto, Long id);

    ApiResponse initiateResetPassword(String usernameOrEmail);

    ApiResponse completeResetPassword(CompletePasswordResetRequest completePasswordResetRequest);
}
