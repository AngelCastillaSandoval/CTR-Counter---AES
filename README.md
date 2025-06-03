# 🔐 AES CTR Demo - Spring Boot WebFlux

Este proyecto demuestra el uso de **cifrado simétrico** con **AES en modo CTR (Counter Mode)**, implementado con Java y Spring Boot WebFlux.

---

## 📌 ¿Qué es AES en modo CTR?

AES-CTR convierte el cifrado por bloques (AES) en un **cifrado por flujo** mediante el uso de un contador (CTR).  
Cada bloque se cifra con:
- Una clave de 16 bytes (128 bits)
- Un IV (vector de inicialización) de 16 bytes que actúa como el `CTR_0`.

✅ No requiere padding: se puede cifrar texto de cualquier longitud.

---

## 🛠️ Tecnologías usadas
- Java 17
- Spring Boot 3.5.0
- WebFlux (reactivo)
- Cipher (Javax Crypto)
- Postman (para probar endpoints)

---

## 📡 Endpoints

### 🔒 `/api/crypto/encrypt`
> Cifra texto plano

```json
POST /api/crypto/encrypt
{
  "plainText": "Hola mundo CTR",
  "key": "miclaveaes123456",
  "iv": "vectorinicio1234"
}
