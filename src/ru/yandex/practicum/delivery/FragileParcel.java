package ru.yandex.practicum.delivery;

public class FragileParcel extends Parcel {

    private static final int BASE_COST = 4;

    public FragileParcel(String description, int weight, String deliveryAddress, int sendDay) {
        super(description, weight, deliveryAddress, sendDay);
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
}
