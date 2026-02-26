export function decodeJwt<T = any>(token: string): T | null {
  try {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  } catch (e) {
    console.error('❌ Erro ao decodificar token:', e);
    return null;
  }
}
