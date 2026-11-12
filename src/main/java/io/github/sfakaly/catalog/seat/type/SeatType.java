package io.github.sfakaly.catalog.seat.type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seat_type", uniqueConstraints = {
        @UniqueConstraint(name = "uq_seat_type_name", columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatType {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @NotNull
    @PositiveOrZero
    @Column(name = "surcharge", nullable = false, precision = 12, scale = 2)
    private BigDecimal surcharge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatType seatType)) return false;
        return id != null && id.equals(seatType.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
