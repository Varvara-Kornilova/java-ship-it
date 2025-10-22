package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.delivery.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryBoxParcelAddTest {

    private final ParcelBox<StandardParcel> standardBox = new ParcelBox<>(50, ParcelCategory.STANDARD);
    private final ParcelBox<PerishableParcel> perishableBox = new ParcelBox<>(50, ParcelCategory.PERISHABLE);
    private final ParcelBox<FragileParcel> fragileBox = new ParcelBox<>(50, ParcelCategory.FRAGILE);

    @Test
    public void shouldNotExceedMaxWeightWhenAddingNewParcelWithWeight_5_AndCurrentWeight_10() {
        StandardParcel standardParcel = new StandardParcel("Гантели", 5,
                "г. Москва", 5);
        standardBox.setCurrentWeight(10);
        assertTrue(standardBox.addParcel(standardParcel));
    }

    @Test
    public void shouldExceedMaxWeight_50_WhenAddingNewParcelWithWeight_15_AndCurrentWeight_49() {
        PerishableParcel perishableParcel = new PerishableParcel("Поставка колбасы", 15,
                "г. Москва", 5, 3);
        perishableBox.setCurrentWeight(49);
        assertFalse(perishableBox.addParcel(perishableParcel));
    }

    @Test
    public void shouldNotExceedMaxWeight_50_WhenAddingNewParcelWithWeight_20_AndCurrentWeight_30() {
        FragileParcel fragileParcel = new FragileParcel("Хрустальная люстра", 20,
                "г. Москва", 5);
        fragileBox.setCurrentWeight(30);
        assertTrue(fragileBox.addParcel(fragileParcel));
    }
}
