package io.github.sfakaly.catalog.hall.type;

import io.github.sfakaly.catalog.hall.type.dto.HallTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallTypeService {

    private final HallTypeRepository hallTypeRepository;
    private final HallTypeMapper hallTypeMapper;

    @Transactional(readOnly = true)
    public List<HallTypeResponse> findAll() {
        return hallTypeRepository.findAll()
                .stream()
                .map(hallTypeMapper::toResponse)
                .toList();
    }
}
