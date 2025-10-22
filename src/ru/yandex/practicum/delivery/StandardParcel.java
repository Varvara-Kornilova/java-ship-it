package ru.yandex.practicum.delivery;

public class StandardParcel extends Parcel {

    private final ParcelCategory type = ParcelCategory.STANDARD;
    private static final int BASE_COST = 2;

    public StandardParcel(String description, int weight, String deliveryAddress, int sendDay) {
        super(description, weight, deliveryAddress, sendDay);
    }

    @Override
    public int getBaseCost() {
        return BASE_COST;
    }

    @Override
    public ParcelCategory getType() {
        return ParcelCategory.STANDARD;
    }
}
