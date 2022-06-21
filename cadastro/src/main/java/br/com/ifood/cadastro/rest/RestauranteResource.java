package br.com.ifood.cadastro.rest;

import br.com.ifood.cadastro.domain.entity.Prato;
import br.com.ifood.cadastro.domain.entity.Restaurante;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
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

    @GET
    public Response buscar() {
        List<Restaurante> restaurantes = Restaurante.listAll();
        return Response.status(Response.Status.OK).entity(restaurantes).build();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante dto) {
        dto.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}")
    @Transactional
    public Response atualizar(@PathParam("idRestaurante") Long idRestaurante, Restaurante dto) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Restaurante restaurante = restauranteBanco.get();
        restaurante.nome = dto.nome;
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

        List<Restaurante> restaurantes = Prato.list("restaurante", restauranteBanco.get());

        return Response.status(Response.Status.OK).entity(restaurantes).build();
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "Prato")
    public Response adicionarPratos(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }
        Prato prato = new Prato();
        prato.restaurante = restauranteBanco.get();
        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.preco = dto.preco;
        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{idPrato}")
    @Transactional
    @Tag(name = "Prato")
    public Response atualizarPratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("idPrato") Long idPrato, Prato dto) {
        Optional<Restaurante> restauranteBanco = Restaurante.findByIdOptional(idRestaurante);

        if(restauranteBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Optional<Prato> pratoBanco = Prato.findByIdOptional(idPrato);

        if(pratoBanco.isEmpty()) {
            throw new NotFoundException();
        }

        Prato prato = pratoBanco.get();
        prato.preco = dto.preco;

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