package com.minsait.api.controller;

import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class UsuarioController {
        @Autowired
        private UsuarioRepository usuarioRepository;

        @PreAuthorize("hasAuthority('LEITURA_USUARIO')")
        @GetMapping("/usuario")
        public ResponseEntity<Page<UsuarioResponse>> findAll(
                        @RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "5") int pageSize) {
                Pageable pageable = PageRequest.of(page, pageSize);
                final Page<UsuarioEntity> usuarioEntityPage = usuarioRepository.findAll(pageable);
                final Page<UsuarioResponse> usuarioResponsePage = ObjectMapperUtil.mapAll(usuarioEntityPage,
                                UsuarioResponse.class);
                return ResponseEntity.ok(usuarioResponsePage);
        }

        @PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
        @PostMapping("/usuario")
        public ResponseEntity<UsuarioResponse> insert(@RequestBody UsuarioRequest usuarioRequest) {
                final var usuarioEntity = ObjectMapperUtil.map(usuarioRequest, UsuarioEntity.class);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                usuarioEntity.setSenha(encoder.encode(usuarioEntity.getSenha()));

                final var usuarioEntitySaved = usuarioRepository.save(usuarioEntity);
                final var usuarioResponseSaved = ObjectMapperUtil.map(usuarioEntitySaved,
                                UsuarioResponse.class);
                return new ResponseEntity<>(usuarioResponseSaved, HttpStatus.CREATED);
        }

        @PreAuthorize("hasAuthority('LEITURA_USUARIO')")
        @GetMapping("/usuario/{id}")
        public ResponseEntity<UsuarioResponse> findById(
                        @PathVariable Long id) {
                final var usuarioEntity = usuarioRepository.findById(id);
                UsuarioResponse usuarioResponse = new UsuarioResponse();
                if (usuarioEntity.isPresent()) {
                        usuarioResponse = ObjectMapperUtil.map(usuarioEntity.get(),
                                        UsuarioResponse.class);
                        return ResponseEntity.ok(usuarioResponse);
                }
                return new ResponseEntity<>(usuarioResponse, HttpStatus.NOT_FOUND);
        }

        @PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
        @PutMapping("/usuario")
        public ResponseEntity<UsuarioResponse> update(@RequestBody UsuarioRequest usuarioRequest) {
                final var usuarioEntityFound = usuarioRepository.findById(usuarioRequest.getId());
                UsuarioResponse usuarioResponse = new UsuarioResponse();
                if (usuarioEntityFound.isEmpty()) {
                        return new ResponseEntity<>(usuarioResponse, HttpStatus.NOT_FOUND);
                }
                UsuarioEntity usuarioEntity = ObjectMapperUtil.map(usuarioRequest, UsuarioEntity.class);
                if (usuarioEntity.getSenha() != null) {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        usuarioEntity.setSenha(encoder.encode(usuarioEntity.getSenha()));
                } else {
                        usuarioEntity.setSenha(usuarioEntityFound.get().getSenha());
                }
                UsuarioEntity updatedUsuario = usuarioRepository.save(usuarioEntity);
                usuarioResponse = ObjectMapperUtil.map(updatedUsuario, UsuarioResponse.class);
                return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
        @DeleteMapping("/usuario/{id}")
        public ResponseEntity delete(@PathVariable Long id) {
                final var usuarioEntity = usuarioRepository.findById(id);
                if (usuarioEntity.isPresent()) {
                        usuarioRepository.delete(usuarioEntity.get());
                        return ResponseEntity.ok().build();
                }
                return ResponseEntity.notFound().build();
        }

}
