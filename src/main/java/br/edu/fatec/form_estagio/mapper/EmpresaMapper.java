package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoEmpresa;
import br.edu.fatec.form_estagio.dto.DadosCadastroEmpresa;
import br.edu.fatec.form_estagio.model.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Empresa toEntity(DadosCadastroEmpresa dados);

    // Converte Entity para DTO (para preencher formulário de edição)
    DadosAtualizacaoEmpresa toAtualizacao(Empresa empresa);

    // Ignoramos o ID e as datas de criação para não serem apagados na hora de atualizar a empresa
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void atualizar(DadosAtualizacaoEmpresa dados, @MappingTarget Empresa empresa);
}
