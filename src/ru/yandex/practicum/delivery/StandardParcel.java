package ru.yandex.practicum.delivery;

public class StandardParcel extends Parcel {

    private final String type = "Обычная посылка";
    private static final int BASE_COST = 2;

    public StandardParcel(String description, int weight, String deliveryAddress, int sendDay) {
        super(description, weight, deliveryAddress, sendDay);
    }

    @Override
    public int getBaseCost() {
        return BASE_COST;
    }

    @Override
    public String getType() {
        return type;
    }
}
