package ru.yandex.practicum.delivery;

public enum ParcelCategory {
    STANDARD(1),
    PERISHABLE(2),
    FRAGILE(3);

    private int code;

    ParcelCategory(int code) {
        this.code = code;
    }

    public static ParcelCategory fromCode(int code) {
        for (ParcelCategory type : values()) {
            if (type.code == code)
                return type; }
        throw new IllegalArgumentException("Неизвестный тип посылки: " + code);
    }
}
