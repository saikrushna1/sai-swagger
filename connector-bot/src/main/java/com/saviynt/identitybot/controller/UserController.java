package com.saviynt.identitybot.controller;

import com.saviynt.identitybot.constants.Message;
import com.saviynt.identitybot.model.User;
import com.saviynt.identitybot.payload.UserRegistrationDto;
import com.saviynt.identitybot.payload.UserUpdateDto;
import com.saviynt.identitybot.response.CreateSuccessResppnse;
import com.saviynt.identitybot.Service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

//TODO: HANDLE EXCEPTIONS
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "resource", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<User> getLoggedInUser( Principal principal)
    {
       User user=userService.findByEmail(principal.getName());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }



    @PutMapping(value = "update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody  UserUpdateDto userRegistrationDto)
    {
        User user=userService.upate(userRegistrationDto,id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }


}
