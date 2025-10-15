package ru.yandex.practicum.delivery;

public class PerishableParcel extends Parcel {

    private final String type = "Скоропортящаяся посылка";
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
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Посылка <<" + getDescription()
                + ">>, вес: " + getWeight()
                + "кг, адрес доставки <<" + getDeliveryAddress()
                + ">>, день отправления: " + getSendDay()
                + ", допустимое количеств дней для доставки: " + getTimeToLive();
    }
}
