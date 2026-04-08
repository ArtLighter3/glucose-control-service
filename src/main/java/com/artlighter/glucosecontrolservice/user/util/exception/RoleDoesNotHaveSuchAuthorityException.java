//package com.artlighter.glucosecontrolservice.user.util.exception;
//
//import com.artlighter.glucosecontrolservice.user.entity.Authority;
//import com.artlighter.glucosecontrolservice.user.entity.Role;
//
///**
// * Исключение, выбрасываемое, если при удалении права у роли оказывается, что этого права уже нет у роли
// */
//public class RoleDoesNotHaveSuchAuthorityException extends AuthoritiesException {
//
//    /**
//     * Конструктор
//     * @param role роль, с которой связано исключение
//     * @param authority право, с которым связано исключение (то есть уже отсутствующее у роли)
//     * @param message сообщение исключения
//     */
//    public RoleDoesNotHaveSuchAuthorityException(Role role, Authority authority, String message) {
//        super(role, authority, message);
//    }
//
//    /**
//     * Конструктор, автоматически инициализирующий сообщение исключения
//     * @param role роль, с которой связано исключение
//     * @param authority право, с которым связано исключение (то есть уже отсутствующее у роли)
//     */
//    public RoleDoesNotHaveSuchAuthorityException(Role role, Authority authority) {
//      this(role, authority, String.format("Role '%s' already doesn't have authority '%s'",
//              role.name(), authority.name()));
//    }
//
//}
