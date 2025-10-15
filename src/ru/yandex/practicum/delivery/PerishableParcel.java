package ru.yandex.practicum.delivery;

public class PerishableParcel extends Parcel {

    private static final int BASE_COST = 3;
    private int timeToLive;

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
    public String toString() {
        return "PerishableParcel{" +
                "description='" + getDescription() + '\'' +
                ", weight=" + getWeight() +
                ", deliveryAddress='" + getDeliveryAddress() + '\'' +
                ", sendDay=" + getSendDay() +
                ", timeToLive=" + getTimeToLive() +
                '}';
    }
}
