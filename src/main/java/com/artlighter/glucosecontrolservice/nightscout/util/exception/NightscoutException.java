package com.artlighter.glucosecontrolservice.nightscout.util.exception;

/**
 * Общее исключение, выбрасываемое при невозможности сохранить значения, которые передал загрузчик Nightscout
 */
public class NightscoutException extends RuntimeException {
    public NightscoutException(String message) {
    super(message);
  }
}
