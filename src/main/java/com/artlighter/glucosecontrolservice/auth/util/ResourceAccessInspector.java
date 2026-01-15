package com.artlighter.glucosecontrolservice.auth.util;

import com.artlighter.glucosecontrolservice.auth.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ResourceAccessInspector {
    private DoctorProfileService doctorProfileService;

    public ResourceAccessInspector(DoctorProfileService doctorProfileService) {
        this.doctorProfileService = doctorProfileService;
    }

    public boolean hasPermissionForResource(String authorityToAccessAll, String authorityToAccessAttached,
                                            String authorityToAccessOnlyOwn, int resourceOwnerId,
                                            Authentication actualCurrentUserAuthentication) {
        UserDetails currentUserDetails = (UserDetails) actualCurrentUserAuthentication.getPrincipal();
        if (currentUserDetails == null) return false;

        if (hasAuthority(currentUserDetails, authorityToAccessAll))
            return true;

        if (hasAuthority(currentUserDetails, authorityToAccessOnlyOwn))
            return checkIfUserIsResourceOwner(resourceOwnerId, currentUserDetails);

        if (hasAuthority(currentUserDetails, authorityToAccessAttached))
            return checkIfResourceOwnerIsAttachedToCurrentUser(resourceOwnerId, currentUserDetails);

        return false;
    }

    private boolean checkIfUserIsResourceOwner(int resourceOwnerId, UserDetails currentUserDetails) {
       // UserDetails userDetails = (UserDetails) actualCurrentUserAuth.getPrincipal();
        if (currentUserDetails instanceof ServiceUserDetails serviceUserDetails) {
            return resourceOwnerId == serviceUserDetails.getId();
        }
        return false;
    }

    private boolean checkIfResourceOwnerIsAttachedToCurrentUser(int resourceOwnerId, UserDetails currentUserDetails) {
        if (currentUserDetails instanceof ServiceUserDetails serviceUserDetails) {
            return doctorProfileService.isPatientAttached(serviceUserDetails.getId(), resourceOwnerId);
        }
        return false;
    }

    private boolean hasAuthority(UserDetails userDetails, String authority) {
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }
}
