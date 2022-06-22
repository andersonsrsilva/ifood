package br.com.ifood.cadastro.rest.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdicionarRestauranteDTO {

    @NotEmpty
    @NotNull
    public String proprietario;

    public String cnpj;

    @Size(min = 3, max = 30)
    public String nome;

    public LocalizacaoDTO localizacao;

}
