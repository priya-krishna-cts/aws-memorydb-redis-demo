package com.memorydb.redis.demo.model;

import com.memorydb.redis.demo.PMSSource;

import java.io.Serializable;
import java.time.LocalDate;

public class HotelMigrationStatus implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    private String hotelId;

    private PMSSource pmsSource;

    private LocalDate updatedOn;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public PMSSource getPmsSource() {
        return pmsSource;
    }

    public void setPmsSource(PMSSource pmsSource) {
        this.pmsSource = pmsSource;
    }

    public LocalDate getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return String.format("HotelMigrationStatus{hotelId='%s', pmsSource=%d, updatedOn=%d}", hotelId, pmsSource, updatedOn);
    }
}
