//package com.artlighter.glucosecontrolservice.auth.controller;
//
//import com.artlighter.glucosecontrolservice.auth.UserLoginDTO;
//import com.artlighter.glucosecontrolservice.user.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//    private UserService userService;
//
//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/process-login")
//    public HttpStatus processLogin(UserLoginDTO userLoginDTO) {
//        return HttpStatus.CREATED;
//    }
//}
