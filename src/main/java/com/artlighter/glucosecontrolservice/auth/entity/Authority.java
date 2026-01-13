package com.artlighter.glucosecontrolservice.auth.entity;

/**
 * Перечисление доступных прав пользователей
 */

public enum Authority {
    GLUCOSE_SHOW_OWN,
    GLUCOSE_SHOW_ATTACHED,
    GLUCOSE_SHOW_ALL,
    GLUCOSE_ADD_OWN,
    GLUCOSE_ADD_ATTACHED,
    GLUCOSE_ADD_ALL,
    GLUCOSE_UPDATE_OWN,
    GLUCOSE_UPDATE_ATTACHED,
    GLUCOSE_UPDATE_ALL,
    GLUCOSE_DELETE_OWN,
    GLUCOSE_DELETE_ATTACHED,
    GLUCOSE_DELETE_ALL;
}
