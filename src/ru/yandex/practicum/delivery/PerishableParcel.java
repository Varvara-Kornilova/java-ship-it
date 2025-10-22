package ru.yandex.practicum.delivery;

import java.util.Objects;

public class PerishableParcel extends Parcel {

    private final ParcelCategory type = ParcelCategory.PERISHABLE;
    private static final int BASE_COST = 3;
    private final int timeToLive;

    public PerishableParcel(String description, int weight, String deliveryAddress, int sendDay, int timeToLive) {
        super(description, weight, deliveryAddress, sendDay);
        this.timeToLive = timeToLive;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    private boolean isExpired(int currentDay) {
        return getSendDay() + getTimeToLive() < currentDay;
    }

    @Override
    public int getBaseCost() {
        return BASE_COST;
    }

    @Override
    public ParcelCategory getType() {
        return ParcelCategory.PERISHABLE;
    }

    @Override
    public String toString() {
        return  "*" + getStatus() +"* /"
                + getType() + "/ <<"
                + getDescription()
                + ">>, вес: " + getWeight()
                + "кг, адрес доставки <<" + getDeliveryAddress()
                + ">>, день отправления: " + getSendDay()
                + ", допустимое количество дней для доставки: " + getTimeToLive();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PerishableParcel that = (PerishableParcel) o;
        return timeToLive == that.timeToLive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeToLive);
    }
}
