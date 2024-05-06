package project.back.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Pharmacy {
    @Id
    private String id;

    private String placeName;
    private String address;
    private String roadAddress;
    private String phone;
    private String distance;

    @Builder
    public Pharmacy( String id, String placeName, String address, String roadAddress, String phone, String distance) {
        this.placeName = placeName;
        this.id=id;
        this.address = address;
        this.roadAddress = roadAddress;
        this.phone = phone;
        this.distance = distance;
    }
}
