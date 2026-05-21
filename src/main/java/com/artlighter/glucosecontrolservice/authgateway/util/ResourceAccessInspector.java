package com.artlighter.glucosecontrolservice.authgateway.util;

import com.artlighter.glucosecontrolservice.authgateway.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.user.UserService;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Класс, инкапсулирующий методы для проверки доступа текущего пользователя к
 * запрашиваемому ресурсу (возможно, другого) пользователя. Например, пользователи
 * с ролью больного по умолчанию могут запрашивать только свои записи дневника,
 * а у пользователей-врачей есть доступ к записям дневника прикрепленных к ним больных.
 */
@Component
public class ResourceAccessInspector {
    private UserService userService;

    /**
     *
     * @param userService сервис для доступа к пользователям
     */
    public ResourceAccessInspector(UserService userService) {
        this.userService = userService;
    }

//    /**
//     * Функция проверяет, есть ли у текущего пользователя из Authentication доступ к ресурсам пользователя
//     * с ID, переданным как resourceOwnerId. Функция использует строковые представления прав для
//     * определения.
//     * <p>
//     * В случае если текущий пользователь имеет право authorityToAccessAll
//     * (доступ к ресурсам определенного типа всех пользователей), то сразу вернет true.
//     * <p>
//     * Если нет права authorityToAccessAll, но есть право authorityToAccessOnlyOwn
//     * (доступ только к своим ресурсам определенного типа), то вернет true, если текущий пользователь и есть
//     * владелец ресурса (по ID).
//     * <p>
//     * Если нет и права authorityToAccessOnlyOwn или ресурс не принадлежит текущему пользователю,
//     * но есть право authorityToAccessAttached (доступ к ресурсу определенного типа прикрепленного пользователя),
//     * то доступ к ресурсу будет разрешен, если к текущему пользователю прикреплен владелец ресурса
//     * (к врачу прикреплен больной).
//     * <p>
//     * В случае отсутствия положительных результатов всех проверок на доступ вернет false. Также вернет false, если
//     * не был найден текущий пользователь в Authentication, либо Authentication - null.
//     * <p>
//     * Можно пропустить проверку
//     * на определенное право, если передать вместо строки null (допустим, если для ресурса определенного типа
//     * предусмотрено только право на личный доступ, ни врачи не смогут получить доступ к этому типу ресурса у
//     * прикрепленного пациента, ни админы к этому же типу ресурса у любого пользователя).
//     * @param authorityToAccessAll право на доступ (модификацию) к ресурсам типа всех пользователей; может быть null,
//     *                             для пропуска проверки на это право;
//     * @param authorityToAccessAttached право на доступ (модификацию) к ресурсам типа прикрепленных пользователей; может быть null,
//     *                                  для пропуска проверки на это право;
//     * @param authorityToAccessOnlyOwn право на доступ (модификацию) к собственному ресурсу типа; может быть null,
//     *                                 для пропуска проверки на это право;
//     * @param resourceOwnerId идентификационный номер владельца ресурса;
//     * @param actualCurrentUserAuthentication объект с текущим пользователем, претендующем на ресурс;
//     * @return true, если в результате вышеперечисленных проверок доступ разрешается; иначе false;
//     */
//    public boolean hasPermissionForResourceByAuthorities(@Nullable String authorityToAccessAll,
//                                                         @Nullable String authorityToAccessAttached,
//                                                         @Nullable String authorityToAccessOnlyOwn,
//                                                         int resourceOwnerId,
//                                                         Authentication actualCurrentUserAuthentication) {
//        if (actualCurrentUserAuthentication == null) return false;
//
//        if (authorityToAccessAll != null && hasAuthority(actualCurrentUserAuthentication, authorityToAccessAll))
//            return true;
//
//        Object detailsObject = actualCurrentUserAuthentication.getPrincipal();
//        if (!(detailsObject instanceof UserDetails currentUserDetails)) return false;
//
//        if (authorityToAccessOnlyOwn != null &&
//                hasAuthority(actualCurrentUserAuthentication, authorityToAccessOnlyOwn)) {
//            boolean check = checkIfUserIsResourceOwner(resourceOwnerId, currentUserDetails);
//            //не сразу возвращаем, если пользователь - не владелец ресурса, ведь у него может быть доступ
//            //к прикрепленным пользователям
//            if (check) return true;
//        }
//
//        if (authorityToAccessAttached != null &&
//                hasAuthority(actualCurrentUserAuthentication, authorityToAccessAttached))
//            return checkIfResourceOwnerIsAttachedToCurrentUser(resourceOwnerId, currentUserDetails);
//
//        return false;
//    }

    /**
     * Функция проверяет доступ к ресурсу (или любому методу, содержащему в URL-пути ID владельца).
     * <p>
     * Если пользователь имеет только роль пациента, то вернет true только если
     * пользователь запрашивает свой ресурс (resourceOwnerId равен ID пользователя из текущей сессии).
     * <p>
     * Если выставлен флаг doctorAccess и пользователь имеет только роль врача, то доступ будет разрешен, если
     * владелец ресурса прикреплен к врачу.
     * <p>
     * Если выставлен флаг adminAccess и пользователь имеет роль админа, то доступ разрешается в любом случае.
     * <p>
     * Для суперпользователя доступ разрешен всегда.
     * <p>
     * Если actualCurrentUserAuthentication равен null, то доступ запрещается.
     * @param resourceOwnerId идентификационный номер владельца ресурса;
     * @param actualCurrentUserAuthentication объект с текущим пользователем, претендующим на ресурс; если null, то
     *                                        функция возвращает false;
     * @param doctorAccess должен ли быть предоставлен доступ пользователю-врачу,
     *                     если владелец ресурса прикреплен к нему;
     * @param adminAccess должен ли быть предоставлен доступ пользователю-админу;
     * @return true, если в результате всех проверок доступ разрешается; иначе false;
     */
    public boolean hasAccessToPatientResource(int resourceOwnerId, Authentication actualCurrentUserAuthentication,
                                              boolean doctorAccess, boolean adminAccess) {
        if (actualCurrentUserAuthentication == null) return false;

        Object detailsObject = actualCurrentUserAuthentication.getPrincipal();
        if (!(detailsObject instanceof UserDetails currentUserDetails)) return false;

        if (hasAuthority(actualCurrentUserAuthentication, "ROLE_SUPERUSER"))
            return true;
        if (adminAccess && hasAuthority(actualCurrentUserAuthentication, "ROLE_ADMIN"))
            return true;

        if (hasAuthority(actualCurrentUserAuthentication, "ROLE_PATIENT") &&
                checkIfUserIsResourceOwner(resourceOwnerId, currentUserDetails))
            return true;

        return doctorAccess &&
                hasAuthority(actualCurrentUserAuthentication, "ROLE_DOCTOR") &&
                checkIfResourceOwnerIsAttachedToCurrentUser(resourceOwnerId, currentUserDetails);
    }

    /**
     * Проверяет, является ли пользователь в текущей сессии владельцем ресурса по ID этого владельца
     * (независимо от роли).
     * @param resourceOwnerId ID владельца ресурса;
     * @param actualCurrentUserAuthentication настоящий пользователь из текущей сессии; если null, функция возвращает
     *                                        false;
     * @return true, если ID владельца совпадает с ID пользователя в текущей сессии; иначе false;
     */
    public boolean isOwnerOfResource(int resourceOwnerId, Authentication actualCurrentUserAuthentication) {
        if (actualCurrentUserAuthentication == null) return false;

        Object detailsObject = actualCurrentUserAuthentication.getPrincipal();
        if (!(detailsObject instanceof UserDetails currentUserDetails)) return false;

        return checkIfUserIsResourceOwner(resourceOwnerId, currentUserDetails);
    }

    /**
     * Проверяет, является ли пользователь с переданным ID администратором. Необходимо для проверок, где
     * результат может зависеть от наличия роли админа (например, один админ не может редактировать другого)
     * @param userId ID пользователя, у которого проверяется наличие роли администратора;
     * @return true, пользователь имеет роль администратора; иначе false;
     */
    public boolean isAdmin(int userId) {
        return userService.hasRole(userId, Role.ROLE_ADMIN);
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
            return userService.isPatientAttached(serviceUserDetails.getId(), resourceOwnerId);
        }
        return false;
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }
}
