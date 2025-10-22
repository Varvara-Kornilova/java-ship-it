package ru.yandex.practicum.delivery;

import java.util.Objects;

public abstract class Parcel {

    private String type;
    private final String description;
    private final int weight;
    private final String deliveryAddress;
    private String currentDeliveryLocation = "Посылка отправлена из почтового отделения";
    private final int sendDay;

    private Status status;

    public Parcel(String description, int weight, String deliveryAddress, int sendDay) {
        this.description = description;
        this.weight = weight;
        this.deliveryAddress = deliveryAddress;
        this.sendDay = sendDay;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
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
        return getType() + " <<"
                + getDescription() +
                ">>, вес: " + getWeight() +
                "кг, адрес доставки <<" + getDeliveryAddress() +
                ">>, день отправления: " + getSendDay() +
                ", статус: *" + getStatus() +"*";
    }

    protected void setCurrentDeliveryLocation(String currentDeliveryLocation) {
        this.currentDeliveryLocation = currentDeliveryLocation;
    }

    protected String getCurrentDeliveryLocation() {
        return currentDeliveryLocation;
    }

    protected abstract String getType();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return weight == parcel.weight
                && sendDay == parcel.sendDay
                && Objects.equals(type, parcel.type)
                && Objects.equals(description, parcel.description)
                && Objects.equals(deliveryAddress, parcel.deliveryAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, description, weight, deliveryAddress, sendDay);
    }
}
