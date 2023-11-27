package com.api.termo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class PalavraService {
    private List<String> palavras;
    private String palavraAtual;

    public PalavraService() {
        try {
            URI uri = new URI("https://www.ime.usp.br/~pf/dicios/br-utf8.txt");
            Path path = Path.of("br-utf8.txt");
            Files.copy(uri.toURL().openStream(), path, StandardCopyOption.REPLACE_EXISTING);
            palavras = Files.readAllLines(path);
            escolherPalavraAleatoria();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // Escolher uma palavra aleatória da lista
    private void escolherPalavraAleatoria() {
        Random random = new Random();
        // pegar palavra com 5 letras
        palavraAtual = palavras.get(random.nextInt(palavras.size())).toLowerCase();
        while (palavraAtual.length() != 5) {
            palavraAtual = palavras.get(random.nextInt(palavras.size())).toLowerCase();
        }
    }

    // Scheduler para mudar a palavra diariamente
    @Scheduled(cron = "0 0 0 * * ?")
    private void mudarPalavraDiariamente() {
        escolherPalavraAleatoria();
    }

    // Verificar se a palavra digitada é a palavra atual
    public boolean verificarPalavra(String palavraDigitada) {
        return palavraDigitada.equals(palavraAtual);
    }

    // Getter para obter a palavra atual
    public String getPalavraAtual() {
        return palavraAtual;
    }
}
