package project.back.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class MartLocationDto {

    private double latitude;
    private double longitude;

    @Builder
    public MartLocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
