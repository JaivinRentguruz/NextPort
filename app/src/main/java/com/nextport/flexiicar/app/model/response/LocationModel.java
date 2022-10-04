package com.nextport.flexiicar.app.model.response;

public class LocationModel {
    public String locationname,streetno;
    public String locationAddress;
    public Double longitude, latitude;

    public LocationModel(String locationname, String streetno, String locationAddress, Double longitude, Double latitude) {
        this.locationname = locationname;
        this.streetno = streetno;
        this.locationAddress = locationAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LocationModel() {
    }
}
