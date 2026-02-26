import CryptoJS from "crypto-js";

// 🔒 Mesma chave usada no Java (precisa ser idêntica)
const SECRET_KEY = "foxinlineTechnologiesCerubEdshow@!";

// ========================================
// 🔐 AES ENCRYPT / DECRYPT (compatível com Java AES-256/ECB/PKCS5Padding)
// ========================================
export function encrypt(text: string): string {
  // Cria a chave fixa de 32 bytes (AES-256)
  const key = CryptoJS.enc.Utf8.parse(
    SECRET_KEY.padEnd(32, "\0").slice(0, 32)
  );

  // Criptografa o texto
  const encrypted = CryptoJS.AES.encrypt(text, key, {
    mode: CryptoJS.mode.ECB, // mesmo modo do Java
    padding: CryptoJS.pad.Pkcs7 // PKCS5 em Java ≡ PKCS7 em CryptoJS
  });

  // Retorna o HEX (igual bytesToHex do Java)
  return encrypted.ciphertext.toString(CryptoJS.enc.Hex);
}

export function decrypt(hexCipher: string): string {
  // Cria a chave fixa de 32 bytes
  const key = CryptoJS.enc.Utf8.parse(
    SECRET_KEY.padEnd(32, "\0").slice(0, 32)
  );

  // Monta os bytes a partir do HEX
  const cipherParams = CryptoJS.lib.CipherParams.create({
    ciphertext: CryptoJS.enc.Hex.parse(hexCipher)
  });

  // Descriptografa
  const decrypted = CryptoJS.AES.decrypt(cipherParams, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });

  // Retorna o texto original (UTF-8)
  return decrypted.toString(CryptoJS.enc.Utf8);
}