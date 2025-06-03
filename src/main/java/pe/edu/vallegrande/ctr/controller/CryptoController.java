package pe.edu.vallegrande.ctr.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.ctr.service.CryptoService;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
@RestController
@RequestMapping("/api/crypto")
// Controlador REST que expone los endpoints relacionados al cifrado y descifrado.
@RequiredArgsConstructor
public class CryptoController {

    // Inyecta automáticamente el servicio encargado del cifrado y descifrado.
    private final CryptoService cryptoService;

    // 🔐 Endpoint POST para cifrar texto plano
    @PostMapping("/encrypt")
    public Mono<ResponseEntity<?>> encrypt(@RequestBody EncryptRequest request) {
        try {
            // ✅ Validación: la clave y el IV deben tener exactamente 16 caracteres (128 bits)
            if (request.getKey().length() != 16 || request.getIv().length() != 16) {
                return Mono.just(ResponseEntity.badRequest().body("❌ Clave e IV deben tener exactamente 16 caracteres."));
            }

            // 🔄 Codificamos la clave e IV a Base64 para pasarlas como bytes al servicio
            String base64Key = Base64.getEncoder().encodeToString(request.getKey().getBytes(StandardCharsets.UTF_8));
            String base64IV = Base64.getEncoder().encodeToString(request.getIv().getBytes(StandardCharsets.UTF_8));

            // 🚀 Llamamos al servicio para cifrar el texto plano
            String cipherText = cryptoService.encrypt(request.getPlainText(), base64Key, base64IV);

            // ✅ Devolvemos el texto cifrado al cliente
            return Mono.just(ResponseEntity.ok(new EncryptResponse(cipherText)));
        } catch (Exception e) {
            // ❌ En caso de error, devolvemos el mensaje
            return Mono.just(ResponseEntity.badRequest().body("❌ Error en cifrado: " + e.getMessage()));
        }
    }

    // 🔓 Endpoint POST para descifrar un texto cifrado
    @PostMapping("/decrypt")
    public Mono<ResponseEntity<?>> decrypt(@RequestBody DecryptRequest request) {
        try {
            // ✅ Validación: la clave y el IV deben tener 16 caracteres
            if (request.getKey().length() != 16 || request.getIv().length() != 16) {
                return Mono.just(ResponseEntity.badRequest().body("❌ Clave e IV deben tener exactamente 16 caracteres."));
            }

            // 🔄 Codificamos clave e IV a Base64 igual que en el cifrado
            String base64Key = Base64.getEncoder().encodeToString(request.getKey().getBytes(StandardCharsets.UTF_8));
            String base64IV = Base64.getEncoder().encodeToString(request.getIv().getBytes(StandardCharsets.UTF_8));

            // 🔓 Desciframos el texto cifrado usando el servicio
            String plainText = cryptoService.decrypt(request.getCipherText(), base64Key, base64IV);

            // ✅ Devolvemos el texto plano resultante
            return Mono.just(ResponseEntity.ok(new DecryptResponse(plainText)));
        } catch (Exception e) {
            // ❌ Error en descifrado
            return Mono.just(ResponseEntity.badRequest().body("❌ Error en descifrado: " + e.getMessage()));
        }
    }

    // 📦 DTO para la solicitud de cifrado
    @Data
    public static class EncryptRequest {
        private String plainText; // Texto que se desea cifrar
        private String key;       // Clave simétrica (16 caracteres)
        private String iv;        // Vector de inicialización (también de 16 caracteres)
    }

    // 📦 DTO para la respuesta del cifrado
    @Data
    public static class EncryptResponse {
        private final String cipherText; // Resultado del cifrado (en Base64)
    }

    // 📦 DTO para la solicitud de descifrado
    @Data
    public static class DecryptRequest {
        private String cipherText; // Texto cifrado en Base64
        private String key;        // Clave simétrica (16 caracteres)
        private String iv;         // Vector de inicialización (16 caracteres)
    }

    // 📦 DTO para la respuesta del descifrado
    @Data
    public static class DecryptResponse {
        private final String plainText; // Resultado del descifrado
    }
}
