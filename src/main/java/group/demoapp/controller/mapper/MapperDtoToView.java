package group.demoapp.controller.mapper;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MapperDtoToView {
    private final ModelMapper modelMapper;

    public <DtoType, ViewType> List<ViewType> mapDtoListToViews(Class<ViewType> type, List<DtoType> dtoList) {
        return dtoList.stream().map(dto ->
                modelMapper.map(dto, type)).collect(Collectors.toList());
    }

    public <DtoType, ViewType> ViewType mapDtoToView(Class<ViewType> type, DtoType dto) {
        return modelMapper.map(dto, type);
    }
}
