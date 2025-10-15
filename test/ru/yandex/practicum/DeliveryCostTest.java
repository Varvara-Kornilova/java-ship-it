package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.delivery.FragileParcel;
import ru.yandex.practicum.delivery.PerishableParcel;
import ru.yandex.practicum.delivery.StandardParcel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DeliveryCostTest {

    StandardParcel standardParcel = new StandardParcel("Плед", 2, "г. Москва", 1);
    PerishableParcel perishableParcel = new PerishableParcel("Колбаса", 5, "г. Дмитров",
            3, 5);
    FragileParcel fragileParcel = new FragileParcel("Аквариум", 1, "г. Солнечногорск",
            28);

    @Test
    public void shouldCalculateDeliveryCostForStandardParcel() {
        int resultCost = standardParcel.calculateDeliveryCost();
        assertEquals(4, resultCost);
    }

    @Test
    public void shouldCalculateDeliveryCostForPerishableParcel() {
        int resultCost = perishableParcel.calculateDeliveryCost();
        assertEquals(15, resultCost);
    }

    @Test
    public void shouldCalculateDeliveryCostForFragileParcel() {
        int resultCost = fragileParcel.calculateDeliveryCost();
        assertEquals(4, resultCost);
    }
}
