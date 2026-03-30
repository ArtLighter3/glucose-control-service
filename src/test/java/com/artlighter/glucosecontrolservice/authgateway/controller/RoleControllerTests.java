package com.artlighter.glucosecontrolservice.authgateway.controller;

import com.artlighter.glucosecontrolservice.authgateway.controller.user.RoleController;
import com.artlighter.glucosecontrolservice.user.service.AuthorityService;
import com.artlighter.glucosecontrolservice.authgateway.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RoleController.class)
public class RoleControllerTests {
    @MockitoBean
    private AuthorityService authorityService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private MockMvc mockMvc;


}
