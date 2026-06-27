//package com.ecommerce.project.controller;
//
//import com.ecommerce.project.model.AppRole;
//import com.ecommerce.project.model.Role;
//import com.ecommerce.project.model.User;
//import com.ecommerce.project.repository.RoleRepository;
//import com.ecommerce.project.repository.UserRepository;
//import com.ecommerce.project.security.request.LoginRequest;
//import com.ecommerce.project.security.request.SignupRequest;
//import com.ecommerce.project.security.response.MessageResponse;
//import com.ecommerce.project.security.response.UserInfoResponse;
//import com.ecommerce.project.security.jwt.JwtUtils;
//import com.ecommerce.project.security.services.UserDetailsImpl;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//        Authentication authentication;
//        try {
//            authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//        } catch (AuthenticationException exception) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("message", "Bad credentials");
//            map.put("status", false);
//            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
//        }
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        //String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//
////        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
////                userDetails.getUsername(), roles, jwtToken);
//
//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
//                userDetails.getUsername(), roles, userDetails.getEmail(), jwtCookie.toString());
//
//        //return ResponseEntity.ok(response);
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
//                jwtCookie.toString())
//                .body(response);
//    }
//
//
//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
//        if(userRepository.existsByUserName(signupRequest.getUsername())){
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if(userRepository.existsByEmail(signupRequest.getEmail())){
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already taken!"));
//        }
//
//        User user = new User(
//                signupRequest.getUsername(),
//                signupRequest.getEmail(),
//                encoder.encode(signupRequest.getPassword())
//        );
//
//        Set<String> strRoles = signupRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if(strRoles == null){
//            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                    .orElseThrow(()-> new RuntimeException("Error: Role is not found!") );
//            roles.add(userRole);
//        }else{
//            strRoles.forEach(role->{
//                switch (role){
//                    case "admin":
//                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                                .orElseThrow(()-> new RuntimeException("Error: Role is not found!") );
//                        roles.add(adminRole);
//                        break;
//                    case "seller":
//                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
//                                .orElseThrow(()-> new RuntimeException("Error: Role is not found!") );
//                        roles.add(sellerRole);
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                                .orElseThrow(()-> new RuntimeException("Error: Role is not found!") );
//                        roles.add(userRole);
//                }
//            });
//        }
//        user.setRoles(roles);
//        userRepository.save(user);
//        return ResponseEntity.ok(new MessageResponse("User Registered successfully!"));
//    }
//
//    @GetMapping("/username")
//    public String currentUserName(Authentication authentication){
//        if(authentication != null){
//            return authentication.getName();
//        }else {
//            return "";
//        }
//    }
//
//    @GetMapping("/user")
//    public ResponseEntity<?> getUserDetails(Authentication authentication){
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//
//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
//                userDetails.getUsername(), roles);
//
//        return ResponseEntity.ok().body(response);
//
//    }
//
//    // sent clean cookie instead of usercookie as a response it itself explain signout logic
//    @PostMapping("/signout")
//    public ResponseEntity<?> signoutUser(){
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
//                        cookie.toString())
//                .body(new MessageResponse("You have been signed out!"));
//    }
//
//}



package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.AuthenticationResult;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        AuthenticationResult result = authService.login(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        result.getJwtCookie().toString())
                .body(result.getResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null)
            return authentication.getName();
        else
            return "";
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        return ResponseEntity.ok().body(authService.getCurrentUserDetails(authentication));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = authService.logoutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(pageNumber ,
                Integer.parseInt(AppConstants.PAGE_SIZE), sortByAndOrder);

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));
    }

}
