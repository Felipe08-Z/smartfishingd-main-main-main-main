package com.itb.inf2dm.smartfishingd.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itb.inf2dm.smartfishingd.model.entity.Favorito;
import com.itb.inf2dm.smartfishingd.model.entity.Pesqueiro;
import com.itb.inf2dm.smartfishingd.services.FavoritoService;

@RestController
@RequestMapping("/api/v1/favorito")
public class ControllerFavorito {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public ResponseEntity<Object> favoritar(@RequestBody Favorito favorito) {
        try {
            Favorito novoFavorito = favoritoService.favoritar(favorito.getUsuarioId(), favorito.getPesqueiroId());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoFavorito);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(
                    Map.of(
                            "status", 409,
                            "error", "Conflict",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @DeleteMapping("/usuario/{usuarioId}/pesqueiro/{pesqueiroId}")
    public ResponseEntity<Object> desfavoritar(@PathVariable String usuarioId, @PathVariable String pesqueiroId) {
        try {
            favoritoService.desfavoritar(Long.parseLong(usuarioId), Long.parseLong(pesqueiroId));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "Favorito removido com sucesso!"
                    )
            );
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O usuarioId ou pesqueiroId informado não é válido"
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Object> listarFavoritosPorUsuario(@PathVariable String usuarioId) {
        try {
            List<Pesqueiro> favoritos = favoritoService.listarFavoritosPorUsuario(Long.parseLong(usuarioId));
            return ResponseEntity.ok(favoritos);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O usuarioId informado não é válido: " + usuarioId
                    )
            );
        }
    }
}
