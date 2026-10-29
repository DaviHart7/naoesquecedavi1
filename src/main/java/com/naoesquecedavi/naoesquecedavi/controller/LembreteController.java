package com.naoesquecedavi.naoesquecedavi.controller;

import com.naoesquecedavi.naoesquecedavi.dto.LembreteRequestDTO;
import com.naoesquecedavi.naoesquecedavi.dto.LembreteResponseDTO;
import com.naoesquecedavi.naoesquecedavi.exception.ResourceNotFoundException;
import com.naoesquecedavi.naoesquecedavi.model.Lembrete;
import com.naoesquecedavi.naoesquecedavi.model.Status;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/lembretes")
public class LembreteController {

    private final List<Lembrete> lembretes = new ArrayList<>();
    private final AtomicLong geradorId = new AtomicLong(1);


    @PutMapping("/{id}")
    public ResponseEntity<Lembrete> atualizar(@PathVariable Long id, @RequestBody Lembrete lembreteAtualizado) {
        Optional<Lembrete> lembreteOpt = buscarLembretePorId(id);

        if (lembreteOpt.isPresent()) {
            Lembrete lembreteExistente = lembreteOpt.get();
            lembreteExistente.setTitulo(lembreteAtualizado.getTitulo());
            lembreteExistente.setDescricao(lembreteAtualizado.getDescricao());
            lembreteExistente.setDataHora(lembreteAtualizado.getDataHora());
            lembreteExistente.setPrioridade(lembreteAtualizado.getPrioridade());

            if (lembreteAtualizado.getStatus() != null) {
                lembreteExistente.setStatus(lembreteAtualizado.getStatus());
            }

            lembreteExistente.setAtualizadoEm(LocalDateTime.now());
            return ResponseEntity.ok(lembreteExistente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Lembrete> concluir(@PathVariable Long id) {
        Optional<Lembrete> lembreteOpt = buscarLembretePorId(id);

        if (lembreteOpt.isPresent()) {
            Lembrete lembrete = lembreteOpt.get();
            lembrete.setStatus(Status.CONCLUIDO);
            lembrete.setAtualizadoEm(LocalDateTime.now());
            return ResponseEntity.ok(lembrete);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<Lembrete> lembreteOpt = buscarLembretePorId(id);

        if (lembreteOpt.isPresent()) {
            lembretes.remove(lembreteOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    private Optional<Lembrete> buscarLembretePorId(Long id) {
        for (Lembrete l : lembretes) {
            if (l.getId().equals(id)) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }

    @PostMapping
    public ResponseEntity<LembreteResponseDTO> cadastrar(
            @Valid @RequestBody LembreteRequestDTO request) {

        Lembrete lembrete = new Lembrete();
        lembrete.setId(geradorId.getAndIncrement());
        lembrete.setTitulo(request.getTitulo());
        lembrete.setDescricao(request.getDescricao());
        lembrete.setDataHora(request.getDataHora());
        lembrete.setPrioridade(request.getPrioridade());
        lembrete.setStatus(Status.PENDENTE);
        lembrete.setCriadoEm(LocalDateTime.now());
        lembrete.setAtualizadoEm(LocalDateTime.now());

        lembretes.add(lembrete);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LembreteResponseDTO.fromEntity(lembrete));
    }

    @GetMapping
    public ResponseEntity<List<LembreteResponseDTO>> listar() {
        List<LembreteResponseDTO> dtos = new ArrayList<>();
        for (Lembrete l : lembretes) {
            dtos.add(LembreteResponseDTO.fromEntity(l));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LembreteResponseDTO> buscarLembretePorIdPorId(@PathVariable Long id) {
        Optional<Lembrete> lembreteOpt = buscarLembretePorId(id);
        if (lembreteOpt.isPresent()) {
            return ResponseEntity.ok(
                    LembreteResponseDTO.fromEntity(lembreteOpt.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Lembrete obterLembreteOuLancarExcecao(Long id) {
        for (Lembrete l : lembretes) {
            if (l.getId().equals(id)) {
                return l;
            }
        }
        throw new ResourceNotFoundException("Lembrete não encontrado com ID: " + id);
    }

}