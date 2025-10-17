package ru.yandex.practicum.delivery;

public abstract class Parcel {

    private final String description;
    private final int weight;
    private final String deliveryAddress;
    private String currentDeliveryLocation;
    private final int sendDay;

    public Parcel(String description, int weight, String deliveryAddress, int sendDay) {
        this.description = description;
        this.weight = weight;
        this.deliveryAddress = deliveryAddress;
        this.sendDay = sendDay;
    }

    protected void packageItem() {
        System.out.println("Посылка <<" + getDescription() + ">> упакована.");
    }

    protected void deliver() {
        System.out.println("Посылка <<" + getDescription()
                + ">> отправлена по адресу <<" + getDeliveryAddress() + ">>.");
    }

    protected abstract int getBaseCost();

    public int calculateDeliveryCost() {
        return getWeight() * getBaseCost();
    }

    protected String getDescription() {
        return description;
    }

    protected int getWeight() {
        return weight;
    }

    protected String getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getSendDay() {
        return sendDay;
    }

    @Override
    public String toString() {
        return "Посылка <<" + getDescription() +
                ">>, вес: " + getWeight() +
                "кг, адрес доставки <<" + getDeliveryAddress() +
                ">>, день отправления: " + getSendDay();
    }

    protected void setCurrentDeliveryLocation(String currentDeliveryLocation) {
        this.currentDeliveryLocation = currentDeliveryLocation;
    }

    protected String getCurrentDeliveryLocation() {
        return currentDeliveryLocation;
    }

    protected abstract String getType();
}
