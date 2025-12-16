package com.artlighter.glucosecontrolservice.auth.util.exception;

/**
 * Общее исключение, выбрасывающееся в ситуации, когда запрос на модификацию, требующий указания значения, имеющего
 * определенный набор значений (энум), содержит значение, отсутствующее в наборе значений (нет такого экземпляра энума)
 */
public class NoSuchEnumerableConstantException extends RuntimeException {
    private String nonExistentEnumValue;
    private String enumName;

    /**
     * Конструктор
     * @param nonExistentEnumValue строковое значение энума, появившееся в запросе, но отсутствующее в самом энуме
     * @param enumName строковое имя энума, значение которого пытались передать
     * @param message сообщение исключения
     */
    public NoSuchEnumerableConstantException(String nonExistentEnumValue, String enumName, String message) {
        super(message);
        this.nonExistentEnumValue = nonExistentEnumValue;
        this.enumName = enumName;
    }

    /**
     * Конструктор, автоматически инициализирующий сообщение
     * @param nonExistentEnumValue строковое значение энума, появившееся в запросе, но отсутствующее в самом энуме
     * @param enumName строковое имя энума, значение которого пытались передать
     */
    public NoSuchEnumerableConstantException(String nonExistentEnumValue, String enumName) {
        this(nonExistentEnumValue, enumName, String.format("%s \'%s\' does not exist", enumName, nonExistentEnumValue));
    }

    public String getNonExistentEnumValue() {
        return nonExistentEnumValue;
    }

    public String getEnum() {
        return enumName;
    }
}
