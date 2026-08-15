package com.itb.inf2dm.smartfishingd.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itb.inf2dm.smartfishingd.model.entity.Historico;
import com.itb.inf2dm.smartfishingd.model.entity.Pesqueiro;
import com.itb.inf2dm.smartfishingd.services.HistoricoService;

@RestController
@RequestMapping("/api/v1/historico")
public class ControllerHistorico {

    @Autowired
    private HistoricoService historicoService;

    @PostMapping
    public ResponseEntity<Historico> registrarAcesso(@RequestBody Historico historico) {
        Historico novoHistorico = historicoService.registrarAcesso(historico.getUsuarioId(), historico.getPesqueiroId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoHistorico);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Object> listarHistoricoPorUsuario(@PathVariable String usuarioId) {
        try {
            List<Pesqueiro> historico = historicoService.listarHistoricoPorUsuario(Long.parseLong(usuarioId));
            return ResponseEntity.ok(historico);
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
