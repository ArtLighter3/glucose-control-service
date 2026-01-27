package com.artlighter.glucosecontrolservice.calculations.util.calc;

/**
 * Интерфейс для стратегии получения текущего значения инсулина по его кривой вывода из организма. Могут быть
 * разные кривые в зависимости от некоторых параметров, а соответственно и значение оставшегося инсулина в
 * определенный момент времени с его введения будет разным.
 */
public interface InsulinDecayCurveStrategy {

    /**
     * Функция получает текущий активный инсулин с учетом изначального введенного количества, параметра
     * его длительности, и прошедших с введения минут.
     * @param initialInsulin изначальное количество инсулина в ЕД.
     * @param minutesPassedFromAdministration минуты, прошедшие с введения.
     * @param durationOfInsulinAction длительность инсулина.
     * @return вещественное значение, отражающее количество инсулина спустя minutesPassedFromAdministration минут.
     */
    double getCurrentActiveInsulin(float initialInsulin,
                                  int minutesPassedFromAdministration,
                                  int durationOfInsulinAction);
}
