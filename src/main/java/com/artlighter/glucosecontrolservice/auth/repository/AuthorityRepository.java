package com.artlighter.glucosecontrolservice.auth.repository;

import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.user.entity.Role;

import java.util.Map;

/**
 * Общий интерфейс для методов доступа к репозиториям данных о правах ролей
 */
public interface AuthorityRepository {
    /**
     * Добавляет право к определенной роли в репозиторий
     * @param role определенная роль (из энума), к которой нужно добавить право
     * @param authority определенное право (из энума), добавляемое к роли
     * @param isDeletable логическое значение, определяющее, можно ли администраторам
     *                    отзывать это право у этой роли в настройках системы
     * @return экземпляр добавленного права из энума в случае успешного добавления; null, если право уже
     * существует у роли
     * @throws IllegalArgumentException в случае, если переданные роль и/или право являются null
     */
    Authority addAuthority(Role role, Authority authority, boolean isDeletable);

    /**
     * Удаляет право у роли из репозитория
     * @param role определенная роль (из энума), у которой нужно удалить право
     * @param authority определенное право (из энума), удаляемое из роли
     * @return экземпляр удаленного права в случае успешного удаления из роли;
     * null, если этого права и так у роли нет
     * @throws IllegalArgumentException в случае, если переданные роль и/или право являются null
     */
    Authority removeAuthority(Role role, Authority authority);

    /**
     * Находит и составляет коллекцию всех прав переданной роли согласно данным репозитория
     * @param role определенная роль, список прав которой нужно получить
     * @return Словарь прав роли, где ключ - право, а
     * значение - логическое значение, определяющее, можно ли администраторам
     * отзывать это право у переданной роли в настройках системы;
     * пустой словарь в случае ошибок
     */
    Map<Authority, Boolean> getRoleAuthorities(Role role);
}
