package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.delivery.PerishableParcel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryIsExpiredTest {

    @Test
    public void checkIsExpiredIfSendDayIs_3_AndTimeToLiveIs_5_WhenTodayIs_6() {
        PerishableParcel perishableParcel = new PerishableParcel("Колбаса", 5, "г. Дмитров",
                3, 5);
        int currentDay = 6;
        int timeToLive = perishableParcel.getTimeToLive();
        int sendDay = perishableParcel.getSendDay();

        assertFalse(sendDay + timeToLive < currentDay);
    }

    @Test
    public void checkIsExpiredIfSendDayIs_25_AndTimeToLiveIs_2_WhenTodayIs_30() {
        PerishableParcel perishableParcel = new PerishableParcel("Колбаса", 5, "г. Дмитров",
                25, 2);
        int currentDay = 30;
        int timeToLive = perishableParcel.getTimeToLive();
        int sendDay = perishableParcel.getSendDay();

        assertTrue(sendDay + timeToLive < currentDay);
    }

    @Test
    public void checkIsExpiredIfSendDayIs_7_AndTimeToLiveIs_8_WhenTodayIs_15() {
        PerishableParcel perishableParcel = new PerishableParcel("Колбаса", 5, "г. Дмитров",
                7, 8);
        int currentDay = 15;
        int timeToLive = perishableParcel.getTimeToLive();
        int sendDay = perishableParcel.getSendDay();

        assertFalse(sendDay + timeToLive < currentDay);
    }

}
