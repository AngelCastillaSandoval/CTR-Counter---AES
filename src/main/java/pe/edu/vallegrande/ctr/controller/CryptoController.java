package pe.edu.vallegrande.ctr.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ctr.service.CryptoService;
import reactor.core.publisher.Mono;

import java.util.Base64;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/encrypt")
    public Mono<CryptoResponse> encrypt(@RequestBody CryptoRequest request) throws Exception {
        byte[] key = cryptoService.generateKey();
        byte[] iv = cryptoService.generateIV();
        String encrypted = cryptoService.encrypt(request.getPlainText(), key, iv);
        return Mono.just(new CryptoResponse(encrypted,
                Base64.getEncoder().encodeToString(key),
                Base64.getEncoder().encodeToString(iv)));
    }

    @PostMapping("/decrypt")
    public Mono<String> decrypt(@RequestBody CryptoResponse response) throws Exception {
        byte[] key = Base64.getDecoder().decode(response.getKey());
        byte[] iv = Base64.getDecoder().decode(response.getIv());
        return Mono.just(cryptoService.decrypt(response.getCipherText(), key, iv));
    }

    @Data
    public static class CryptoRequest {
        private String plainText;
    }

    @Data
    public static class CryptoResponse {
        private String cipherText;
        private String key;
        private String iv;

        public CryptoResponse(String cipherText, String key, String iv) {
            this.cipherText = cipherText;
            this.key = key;
            this.iv = iv;
        }

        public CryptoResponse() {}
    }
}
