package com.api.termo.api.controller;

import com.api.termo.service.PalavraService;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

enum LetterStatus {
    CORRECT, INCORRECT, MISSING
}

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/palavra")
public class PalavraController {
    private final PalavraService palavraService;

    @Autowired
    public PalavraController(PalavraService palavraService) {
        this.palavraService = palavraService;
    }

    @GetMapping("/verificar")
    public Map<String, String> verificarPalavra(@QueryParam("palavraDigitada") String palavraDigitada) {
        Map<String, String> response = new HashMap<>();
        String palavraAtual = palavraService.getPalavraAtual().toLowerCase();

        palavraAtual = palavraAtual.replaceAll("[áàãâä]", "a");
        palavraAtual = palavraAtual.replaceAll("[éèêë]", "e");
        palavraAtual = palavraAtual.replaceAll("[íìîï]", "i");
        palavraAtual = palavraAtual.replaceAll("[óòõôö]", "o");
        palavraAtual = palavraAtual.replaceAll("[úùûü]", "u");

        char[] palavraDigitadaArray = palavraDigitada.toLowerCase().toCharArray();
        boolean[] marcadoComoPresente = new boolean[palavraAtual.length()];

        // Primeiro, marcar as letras corretas
        for (int i = 0; i < palavraDigitadaArray.length; i++) {
            char letra = palavraDigitadaArray[i];
            if (i < palavraAtual.length() && letra == palavraAtual.charAt(i)) {
                response.put(String.valueOf(i), LetterStatus.CORRECT.toString());
                marcadoComoPresente[i] = true;
            } else {
                response.put(String.valueOf(i), LetterStatus.MISSING.toString());
            }
        }

        // Depois, marcar as letras presentes
        for (int i = 0; i < palavraDigitadaArray.length; i++) {
            if (!response.get(String.valueOf(i)).equals(LetterStatus.CORRECT.toString())) {
                char letra = palavraDigitadaArray[i];
                for (int j = 0; j < palavraAtual.length(); j++) {
                    if (letra == palavraAtual.charAt(j) && !marcadoComoPresente[j]) {
                        response.put(String.valueOf(i), LetterStatus.INCORRECT.toString());
                        marcadoComoPresente[j] = true;
                        break;
                    }
                }
            }
        }

        return response;
    }

    @RequestMapping("/atual")
    public String obterPalavraAtual() {
        return palavraService.getPalavraAtual();
    }
}
