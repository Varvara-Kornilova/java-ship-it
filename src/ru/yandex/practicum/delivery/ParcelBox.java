package ru.yandex.practicum.delivery;

import java.util.ArrayList;
import java.util.List;

public class ParcelBox<T extends Parcel> {

    private final int maxWeight;
    private boolean isSent = false;
    private final ParcelCategory boxType;
    private final boolean isTrackable;

    public ParcelBox(int maxWeight, ParcelCategory boxType, boolean isTrackable) {
        this.maxWeight = maxWeight;
        this.boxType = boxType;
        this.isTrackable = isTrackable;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isSent() {
        return isSent;
    }

    private int currentWeight = 0;
    private final List<T> parcelsInBox = new ArrayList<>();

    public ParcelCategory getBoxType() {
        return boxType;
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

    public boolean removeParcel(T parcel) {
        parcelsInBox.remove(parcel);
        return true;
    }

    public List<T> getAllParcels() {
        return parcelsInBox;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public boolean isTrackable() {
        return isTrackable;
    }
}
