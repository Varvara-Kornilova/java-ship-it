package ru.yandex.practicum.delivery;

public class PerishableParcel extends Parcel {

    private int timeToLive;

    public PerishableParcel(String description, int weight, String deliveryAddress, int sendDay, int timeToLive) {
        super(description, weight, deliveryAddress, sendDay);
        this.timeToLive = timeToLive;
    }

    @Override
    public String toString() {
        return "PerishableParcel{" +
                "description='" + getDescription() + '\'' +
                ", weight=" + getWeight() +
                ", deliveryAddress='" + getDeliveryAddress() + '\'' +
                ", sendDay=" + getSendDay() +
                "timeToLive=" + getTimeToLive() +
                '}';
    }

    public int getTimeToLive() {
        return timeToLive;
    }
}
