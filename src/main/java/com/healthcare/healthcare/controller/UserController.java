package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.UsersDto;
import com.healthcare.healthcare.entitiy.Users;
import com.healthcare.healthcare.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/v1/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signIn")
    public ResponseEntity<Long> signIn(@Valid @RequestBody UsersDto userInfoDto) {
        Long userId = userService.signIn(userInfoDto);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<Long> changePassword(@RequestParam(required = false) String username,
                                               @RequestParam(required = false) String email,
                                               @RequestBody String password) {
        Long userId = userService.changePassword(username, email, password);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PostMapping("/register/user")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody UsersDto usersDto,
                                              @RequestParam(required = false) String userRole) {
        Users users = userService.registerUser(usersDto, userRole);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

//    @PostMapping("/register/patient")
//    public ResponseEntity<Users> registerPatient(@Valid @RequestBody UsersDto usersDto) {
//        Users users = userService.registerPatient(usersDto);
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    @PostMapping("/register/doctor")
//    public ResponseEntity<Users> registerDoctor(@Valid @RequestBody UsersDto usersDto) {
//        Users users = userService.registerDoctor(usersDto);
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    @PostMapping("/register/staff")
//    public ResponseEntity<Users> registerStaff(@Valid @RequestBody UsersDto usersDto) {
//        Users users = userService.registerStaff(usersDto);
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    @PostMapping("/register/admin")
//    public ResponseEntity<Users> registerAdmin(@Valid @RequestBody UsersDto usersDto) {
//        Users users = userService.registerAdmin(usersDto);
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
}
