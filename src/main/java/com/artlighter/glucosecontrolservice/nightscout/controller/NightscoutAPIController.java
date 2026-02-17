package com.artlighter.glucosecontrolservice.nightscout.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nightscout/{username}/")
public class NightscoutAPIController {

    @PostMapping("/entries")
    @PreAuthorize("@nightscoutAuthUtils.hasAccessToNightscoutApi(#username, " +
            "httpServletRequest.getHeader('api-secret'))")
    public ResponseEntity postEntry(@PathVariable String username) {
        return null;
    }
}
