import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 20, // usuarios virtuales
  duration: '30s', // tiempo de test
};

export default function () {
  const url = 'http://localhost:8086/bookstar/public/users';

  const payload = JSON.stringify({
    firstName: 'Test',
    lastName: 'User',
    alias: `testuser_${__VU}_${__ITER}`,
    email: `test_${__VU}_${__ITER}@bookstar.io`,
    phoneNumber: '123456789',
    password: 'Prueba123!',
    country: 'ES',
    birthDate: '1991-04-20',
    role: 'USER',
    avatarUrl: ''
  });

  const headers = { 'Content-Type': 'application/json' };

  const res = http.post(url, payload, { headers });

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1); // simula tiempo entre usuarios
}
