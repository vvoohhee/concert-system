import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    scenarios: {
        scenario1: {
            vus: 500,
            exec: 'concertSearch',
            executor: 'per-vu-iterations',
            iterations: 1
        },
    }
};

export function concertSearch() {
    let response = http.get(`http://localhost:8080/api/concert/queue?at=2024-08-23T10:00:00`);
    check(response, {
        'is status 200': (r) => r.status === 200
    });
}
