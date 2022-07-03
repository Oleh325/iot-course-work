package ua.lviv.iot.novaPoshtaAPI.model;

public abstract class Item {
    public abstract String getHeaders();

    public abstract String toCSV();

    public Long getCourierId() {
        return 0L;
    }

    public Long getDepartmentId() {
        return 0L;
    }

    public Long getParcelId() {
        return 0L;
    }

}
