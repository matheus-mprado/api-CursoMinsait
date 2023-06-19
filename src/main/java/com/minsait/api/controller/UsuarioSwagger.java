package com.minsait.api.controller;

import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;
import com.minsait.api.controller.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "endpoint usuario práticas tecnológicas usuário")
public interface UsuarioSwagger {
    @Operation(summary = "Listar todos os usuários", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao listar todos os usuários"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro"),
    })
    ResponseEntity<Page<UsuarioResponse>> findAll(
            int page, int pageSize);

    @Operation(summary = "inserir um novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao inserir um usuário"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro"),
    })
    ResponseEntity<UsuarioResponse> insert(UsuarioRequest request);

    @Operation(summary = "atualizar um usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao atualizar um usuário"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro"),
    })
    ResponseEntity<UsuarioResponse> update(UsuarioRequest request);

    @Operation(summary = "deletar um usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao deletar um usuário"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro"),
    })
    ResponseEntity<MessageResponse> delete(Long id);

    @Operation(summary = "buscar um usuário pelo id", responses = {
            @ApiResponse(responseCode = "201", description = "Sucesso listar usuário"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro"),
    })
    ResponseEntity<UsuarioResponse> findById(Long id);
}
