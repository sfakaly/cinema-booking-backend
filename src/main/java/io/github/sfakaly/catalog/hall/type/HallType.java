package io.github.sfakaly.catalog.hall.type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "hall_type", uniqueConstraints = {
        @UniqueConstraint(name = "uq_hall_type_name", columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallType {

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
    @Column(name = "minimum_interval_seconds", nullable = false)
    private Integer minimumIntervalSeconds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HallType hallType)) return false;
        return id != null && id.equals(hallType.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
