package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.delivery.FragileParcel;
import ru.yandex.practicum.delivery.PerishableParcel;
import ru.yandex.practicum.delivery.StandardParcel;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeliveryCostTest {

    private final StandardParcel standardParcel = new StandardParcel("Плед", 2, "г. Москва", 1);
    private final PerishableParcel perishableParcel = new PerishableParcel("Колбаса", 5, "г. Дмитров",
            3, 5);
    private final FragileParcel fragileParcel = new FragileParcel("Аквариум", 1, "г. Солнечногорск",
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
