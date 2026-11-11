package io.github.sfakaly.catalog.cinema;

import io.github.sfakaly.catalog.cinema.dto.CinemaCreateRequest;
import io.github.sfakaly.catalog.cinema.dto.CinemaPatchRequest;
import io.github.sfakaly.catalog.cinema.dto.CinemaResponse;
import io.github.sfakaly.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    @Transactional(readOnly = true)
    public CinemaResponse findById(Integer id) {
        return cinemaMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<CinemaResponse> findAll(Pageable pageable) {
        Page<Cinema> cinemaPage = cinemaRepository.findAll(pageable);
        return cinemaPage.map(cinemaMapper::toResponse);
    }

    @Transactional
    public CinemaResponse create(CinemaCreateRequest dto) {
        Cinema cinema = cinemaMapper.toEntity(dto);
        Cinema savedCinema = cinemaRepository.save(cinema);
        return cinemaMapper.toResponse(savedCinema);
    }

    @Transactional
    public void delete(Integer id) {
        Cinema cinema = findOrThrow(id);
        cinemaRepository.delete(cinema);
    }

    @Transactional
    public CinemaResponse patch(Integer id, CinemaPatchRequest dto) {
        Cinema existingCinema = findOrThrow(id);

        cinemaMapper.patchEntityFromRequest(dto, existingCinema);
        return cinemaMapper.toResponse(existingCinema);
    }

    private Cinema findOrThrow(Integer id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema with %d id not found".formatted(id)));
    }
}
