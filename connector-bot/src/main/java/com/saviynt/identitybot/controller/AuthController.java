package com.saviynt.identitybot.controller;

import com.saviynt.identitybot.Service.CustomUserDetailsService;
import com.saviynt.identitybot.Service.UserService;
import com.saviynt.identitybot.constants.Message;
import com.saviynt.identitybot.exceptions.UserAlreadyExistsException;
import com.saviynt.identitybot.model.User;
import com.saviynt.identitybot.payload.*;
import com.saviynt.identitybot.repository.UserRepository;
import com.saviynt.identitybot.response.CreateSuccessResppnse;
import com.saviynt.identitybot.security.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsernameOrEmail());
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
//                    required = true, dataType = "string", paramType = "header")
//    })
    public ResponseEntity<CreateSuccessResppnse> saveUser(@RequestBody UserRegistrationDto userRegistrationDto)
    {
        User existing = userService.findByEmail(userRegistrationDto.getEmail());
        if (existing != null) {
            throw new UserAlreadyExistsException("There is already an account registered with thIS email");
        }
        User user=userService.save(userRegistrationDto);
        return new ResponseEntity<CreateSuccessResppnse>
                (CreateSuccessResppnse.builder().id(user.getId()).message(Message.CREATE_USER_SUCCESS_MESSAGE).build(), HttpStatus.OK);
    }

    @PostMapping("/startPasswordReset")
    public ApiResponse startPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest) {
        return userService.initiateResetPassword(passwordResetRequest.getEmail());
    }


    @PostMapping("/completePasswordReset")
    public ApiResponse completePasswordReset(@RequestBody CompletePasswordResetRequest completePasswordResetRequest) {
        return userService.completeResetPassword(completePasswordResetRequest);
    }


}
