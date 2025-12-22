package com.artlighter.glucosecontrolservice.auth.controller;

import com.artlighter.glucosecontrolservice.auth.service.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdminController.class)
public class AdminControllerTests {
    @MockitoBean
    private AuthorityService authorityService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private MockMvc mockMvc;


}
