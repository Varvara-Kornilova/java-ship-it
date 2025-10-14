package ru.yandex.practicum.delivery;

public abstract class Parcel {
    //добавьте реализацию и другие необходимые классы
    private String description;
    private int weight;
    private String deliveryAddress;
    private int sendDay;


    public Parcel(String description, int weight, String deliveryAddress, int sendDay) {
        this.description = description;
        this.weight = weight;
        this.deliveryAddress = deliveryAddress;
        this.sendDay = sendDay;
    }

    public void setWeight(int weigh) {
            if (weight < 0) {
                throw new IllegalArgumentException("Вес не может быть отрицательным.");
            }
            this.weight = weight;
    }

    public void setSendDay(int sendDay) {
            if (sendDay < 1 || sendDay > 30) {
                System.out.println("День отправления должен находиться в пределах от 1 до 30.");
            }
            this.sendDay = sendDay;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "description='" + description + '\'' +
                ", weight=" + weight +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", sendDay=" + sendDay +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getSendDay() {
        return sendDay;
    }
}
