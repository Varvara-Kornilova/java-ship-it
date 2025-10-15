package ru.yandex.practicum.delivery;

public class FragileParcel extends Parcel implements Trackable {

    private static final int BASE_COST = 4;
    private final String type = "Хрупкая посылка";

    public FragileParcel(String description, int weight, String deliveryAddress, int sendDay) {
        super(description, weight, deliveryAddress, sendDay);
    }

    @Override
    public void reportStatus(String newLocation) {
        System.out.println("Хрупкая посылка <<" + getDescription() + ">> изменила местоположение на " + newLocation);

    }

    @Override
    public void packageItem() {
        System.out.println("Посылка <<" + getDescription() + ">> обёрнута в защитную пленку.");
        super.packageItem();
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
