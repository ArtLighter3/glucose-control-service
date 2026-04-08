//package com.artlighter.glucosecontrolservice.user.util.exception;
//
//import com.artlighter.glucosecontrolservice.user.entity.Authority;
//import com.artlighter.glucosecontrolservice.user.entity.Role;
//
///**
// * Общее исключение, выбрасываемое при ошибках в действиях со связкой Роли и Права
// */
//
//public class AuthoritiesException extends RuntimeException {
//    private Authority authority;
//    private Role role;
//
//    /**
//     * Конструктор
//     * @param role роль, с которой связано исключение
//     * @param authority право, с которым связано исключение
//     * @param message сообщение исключения
//     */
//    public AuthoritiesException(Role role, Authority authority, String message) {
//      super(message);
//      this.authority = authority;
//      this.role = role;
//    }
//
//    public Authority getAuthority() {
//      return authority;
//    }
//
//    public Role getRole() {
//      return role;
//    }
//
//}
