package vn.ute.smartphoneshop.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RatingDTO {
    @Column(nullable = false, length = 65535)
    private String content;

    @Column(nullable = false)
    private int star;

    private int userId;

    private int productId;
}
