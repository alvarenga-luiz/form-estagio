package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.model.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    
    // Ignoramos o ID e as datas de criação para não serem apagados na hora de atualizar a empresa
    @Mapping(target = "id", ignore = true)    
    @Mapping(target = "createdAt", ignore = true) 
    @Mapping(target = "updatedAt", ignore = true) 
    void atualizar(Empresa origem, @MappingTarget Empresa destino);
}