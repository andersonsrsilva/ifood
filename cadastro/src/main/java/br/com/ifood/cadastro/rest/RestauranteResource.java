package br.com.ifood.cadastro.rest;

import br.com.ifood.cadastro.domain.entity.Prato;
import br.com.ifood.cadastro.domain.entity.Restaurante;
import br.com.ifood.cadastro.rest.dto.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurante")
public class RestauranteResource {

    @Inject
    RestauranteMapper restauranteMapper;

    @GET
    public Response buscar() {
        List<Restaurante> restaurantes = Restaurante.listAll();
        List<RestauranteDTO> dtos = restauranteMapper.toListRestauranteDTO(restaurantes);
        return Response.status(Response.Status.OK).entity(dtos).build();
    }

    @POST
    @Transactional
    public Response adicionar(@Valid AdicionarRestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toEntity(dto);
        restaurante.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}")
    @Transactional
    public Response atualizar(@PathParam("idRestaurante") Long idRestaurante, AtualizarRestauranteDTO dto) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Restaurante restaurante = restauranteBanco.get();

        restauranteMapper.toEntity(dto, restaurante);
        restaurante.persist();

        return Response.noContent().build();
    }

    @DELETE
    @Path("{idRestaurante}")
    @Transactional
    public Response delete(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        restauranteBanco.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });

        return Response.noContent().build();
    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "Prato")
    public Response buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        List<Prato> pratos = Prato.list("restaurante", restauranteBanco.get());
        List<ListarPratoDTO> dtos = restauranteMapper.toListPratoDTO(pratos);

        return Response.status(Response.Status.OK).entity(dtos).build();
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "Prato")
    public Response adicionarPratos(@PathParam("idRestaurante") Long idRestaurante, AdicionarPratoDTO dto) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Prato prato = restauranteMapper.toEntity(dto);
        prato.restaurante = restauranteBanco.get();
        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{idPrato}")
    @Transactional
    @Tag(name = "Prato")
    public Response atualizarPratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("idPrato") Long idPrato, AtualizarPratoDTO dto) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Optional<Prato> pratoBanco = Prato.findByIdOptional(idPrato);

        if(pratoBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Prato prato = restauranteMapper.toEntity(dto);
        prato.restaurante = restauranteBanco.get();
        prato.persist();

        return Response.noContent().build();
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{idPrato}")
    @Transactional
    @Tag(name = "Prato")
    public Response deletePrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("idPrato") Long idPrato) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Optional<Prato> pratoBanco = Prato.findByIdOptional(idPrato);

        pratoBanco.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException();
        });

        return Response.noContent().build();
    }

}