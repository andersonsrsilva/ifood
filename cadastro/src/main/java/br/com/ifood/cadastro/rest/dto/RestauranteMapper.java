package br.com.ifood.cadastro.rest.dto;

import br.com.ifood.cadastro.domain.entity.Prato;
import br.com.ifood.cadastro.domain.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    Restaurante toEntity(AdicionarRestauranteDTO dto);
    void toEntity(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);

    Prato toEntity(AdicionarPratoDTO dto);
    Prato toEntity(AtualizarPratoDTO dto);

    List<ListarPratoDTO> toListPratoDTO(List<Prato> entity);
    List<RestauranteDTO> toListRestauranteDTO(List<Restaurante> entity);
}
