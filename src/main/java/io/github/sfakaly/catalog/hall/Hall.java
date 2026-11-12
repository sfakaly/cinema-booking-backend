package io.github.sfakaly.catalog.hall;

import io.github.sfakaly.catalog.cinema.Cinema;
import io.github.sfakaly.catalog.hall.type.HallType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "hall", uniqueConstraints = {
        @UniqueConstraint(name = "uq_hall_cinema_name", columnNames = {"cinema_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hall_type_id", nullable = false)
    private HallType hallType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @NotBlank
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hall hall)) return false;
        return id != null && id.equals(hall.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
