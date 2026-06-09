package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.UsersDto;
import com.healthcare.healthcare.entitiy.Role;
import com.healthcare.healthcare.entitiy.Users;
import com.healthcare.healthcare.enums.RoleName;
import com.healthcare.healthcare.exception.NoSuchUserFoundException;
import com.healthcare.healthcare.exception.PasswordConfirmationException;
import com.healthcare.healthcare.exception.UsernameAlreadyTakenException;
import com.healthcare.healthcare.repository.RoleRepository;
import com.healthcare.healthcare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository/*, PasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    public Long signIn(UsersDto userInfoDto) {
        Optional<Users> optionalUser = userRepository.findById(userInfoDto.getId());

        if (optionalUser.isPresent()) {
            try {
                throw new UsernameAlreadyTakenException("Username already taken");
            } catch (UsernameAlreadyTakenException e) {
                throw new RuntimeException(e);
            }
        }
        Users user = new Users();
//        user.setUsername(userInfoDto.getUsername());
        user.setEmail(userInfoDto.getEmail());
//        user.setPassword(userInfoDto.getPassword());
        userRepository.save(user);
        return user.getId();
    }

    public Long changePassword(String username, String email, String password) {
        Optional<Users> optionalUser = userRepository.findByUsernameOrEmail(username, email);
        if (optionalUser.isEmpty()) {
            try {
                throw new NoSuchUserFoundException("no such user found!");
            } catch (NoSuchUserFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Users user = optionalUser.get();
        if (username != null) {
            user.setId(user.getId());
            user.setUsername(username);
//            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return user.getId();
        } else {
            user.setId(user.getId());
            user.setEmail(email);
//            user.setPassword(passwordEncoder.encode(password));
            return user.getId();
        }
    }

    public Users registerUser(UsersDto usersDto, String role) {

        if(!usersDto.getPassword().equals(usersDto.getConfirmPassword())) {
            try {
                throw new PasswordConfirmationException("Passwords do not match");
            } catch (PasswordConfirmationException e) {
                throw new RuntimeException(e);
            }
        }
        if(userRepository.existsByUsername(usersDto.getUsername())) {
            try {
                throw new UsernameAlreadyTakenException("Username already taken");
            } catch (UsernameAlreadyTakenException e) {
                throw new RuntimeException(e);
            }
        }

        Users users = new Users();
        users.setUsername(usersDto.getUsername());
//        users.setPassword(passwordEncoder.encode(usersDto.getPassword()));
        users.setEmail(usersDto.getEmail());

        Role patientRole = roleRepository.findByName(role);
        users.addRole(patientRole);
        userRepository.save(users);
        return users;
    }

//    public Users registerPatient(UsersDto usersDto) {
//        String role = String.valueOf(RoleName.ROLE_PATIENT);
//        Users users = registerUser(usersDto, role);
//        return userRepository.save(users);
//
//    }
//
//    public Users registerDoctor(UsersDto usersDto) {
//        String role = String.valueOf(RoleName.ROLE_DOCTOR);
//        Users users = registerUser(usersDto, role);
//        return userRepository.save(users);
//    }
//
//    public Users registerStaff(UsersDto usersDto) {
//        String role = String.valueOf(RoleName.ROLE_STAFF);
//        Users users = registerUser(usersDto, role);
//        return userRepository.save(users);
//    }
//
//    public Users registerAdmin(UsersDto usersDto) {
//        String role = String.valueOf(RoleName.ROLE_ADMIN);
//        Users users = registerUser(usersDto, role);
//        return userRepository.save(users);
//    }

}
