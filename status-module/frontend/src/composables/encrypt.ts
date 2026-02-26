import CryptoJS from "crypto-js";

const SECRET_KEY = "foxinlineTechnologiesCerubEdshow@!";

export function encrypt(text: string): string {
  const key = CryptoJS.enc.Utf8.parse(
    SECRET_KEY.padEnd(32, "\0").slice(0, 32)
  );
  const encrypted = CryptoJS.AES.encrypt(text, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });
  return encrypted.ciphertext.toString(CryptoJS.enc.Hex);
}

export function decrypt(hexCipher: string): string {
  const key = CryptoJS.enc.Utf8.parse(
    SECRET_KEY.padEnd(32, "\0").slice(0, 32)
  );
  const cipherParams = CryptoJS.lib.CipherParams.create({
    ciphertext: CryptoJS.enc.Hex.parse(hexCipher)
  });
  const decrypted = CryptoJS.AES.decrypt(cipherParams, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });
  return decrypted.toString(CryptoJS.enc.Utf8);
}
