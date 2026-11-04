package io.github.sfakaly.catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "screening")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screening {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull
    @Column(name = "advertising_start_time", nullable = false)
    private OffsetDateTime advertisingStartTime;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "advertising_duration_seconds", nullable = false)
    private Integer advertisingDurationSeconds = 0;

    @NotNull
    @PositiveOrZero
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Screening screening)) return false;
        return id != null && id.equals(screening.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public OffsetDateTime getMovieStartTime() {
        return advertisingStartTime.plusSeconds(advertisingDurationSeconds);
    }

    public OffsetDateTime getMovieEndTime() {
        if (movie == null) return null;
        return getMovieStartTime().plusSeconds(movie.getDurationSeconds());
    }
}
