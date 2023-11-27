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
        char[] palavraDigitadaArray = palavraDigitada.toCharArray();
        String palavraAtual = palavraService.getPalavraAtual();
        int comprimentoMaximo = Math.max(palavraAtual.length(), palavraDigitadaArray.length);

        for (int i = 0; i < comprimentoMaximo; i++) {
            char letraAtual = (i < palavraDigitadaArray.length) ? palavraDigitadaArray[i] : ' ';
            char letraPalavraAtual = (i < palavraAtual.length()) ? palavraAtual.charAt(i) : ' ';

            if (letraAtual == letraPalavraAtual) {
                response.put(String.valueOf(letraAtual), LetterStatus.CORRECT.toString());
            } else if (palavraAtual.indexOf(letraAtual) != -1) {
                response.put(String.valueOf(letraAtual), LetterStatus.INCORRECT.toString());
            } else {
                response.put(String.valueOf(letraAtual), LetterStatus.MISSING.toString());
            }
        }

        return response;
    }

    @RequestMapping("/atual")
    public String obterPalavraAtual() {
        return "Palavra atual: " + palavraService.getPalavraAtual();
    }
}
