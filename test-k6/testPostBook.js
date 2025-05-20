import http from 'k6/http';
import encoding from 'k6/encoding';
import { check, sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '20s',
};

export default function () {
  const url = 'http://localhost:8086/bookstar/admin/books';

  const payload = JSON.stringify({
    title: `Book ${__VU}-${__ITER}`,
    author: "Test Author",
    editorial: "Editorial Test",
    isbn: `9780000${__VU}${__ITER}`.padEnd(13, "0"),
    publicationDate: "2024-01-01"
  });

  const credentials = 'anusky:Liqui2023*-';
  const encoded = encoding.b64encode(credentials);

  const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Basic ${encoded}`
  };

  const res = http.post(url, payload, { headers });

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}
