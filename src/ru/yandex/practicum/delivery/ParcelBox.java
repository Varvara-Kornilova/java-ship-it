package ru.yandex.practicum.delivery;

import java.util.ArrayList;
import java.util.List;

public class ParcelBox<T extends Parcel> {

    private final int maxWeight;
    private int currentWeight = 0;
    List<T> parcelsInBox = new ArrayList<>();

    public ParcelBox(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public boolean addParcel(T parcel) {
        if (parcel == null) {
            return false;
        }

        if (parcel.getWeight() + currentWeight <= maxWeight) {
            parcelsInBox.add(parcel);
            currentWeight += parcel.getWeight();
            return true;
        } else {
            System.out.println("Добавление данной посылки невозможно, так как превышен весовой порог.");
            return false;
        }
    }

    public List<T> getAllParcels() {
        return parcelsInBox;
    }
}
