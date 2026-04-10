package example.save2.xml.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GpxPointDto {

    private LocalDateTime datetime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal altitude;
    private Integer heartRate;

    public GpxPointDto() {}

    private GpxPointDto(GpxPointDtoBuilder builder) {

        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.altitude = builder.altitude;
        this.datetime = builder.datetime;
        this.heartRate = builder.heartRate;
    }

    public LocalDateTime getDateTime() {
        return datetime;
    }

    public void setDateTime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public void setAltitude(BigDecimal altitude) {
        this.altitude = altitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public static class GpxPointDtoBuilder {

        private LocalDateTime datetime;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private BigDecimal altitude;
        private Integer heartRate;

        public GpxPointDtoBuilder setDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public GpxPointDtoBuilder setLatitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public GpxPointDtoBuilder setLongitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public GpxPointDtoBuilder setAltitude(BigDecimal altitude) {
            this.altitude = altitude;
            return this;
        }

        public GpxPointDtoBuilder setHeartRate(Integer heartRate) {
            this.heartRate = heartRate;
            return this;
        }

        public GpxPointDto build() {
            return new GpxPointDto(this);
        }

    }

}
