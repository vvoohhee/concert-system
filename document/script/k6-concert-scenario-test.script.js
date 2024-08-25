import http from 'k6/http';
import {check, sleep} from 'k6';
import {randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export let options = {
    scenarios: {
        scenario1: {
            vus: 100,
            exec: 'concert_scenario',
            executor: 'per-vu-iterations',
            iterations: 2
        },
    }
};

export function concertSearch() {
    let response = http.get(`http://localhost:8080/api/concert/queue?at=2024-08-23T10:00:00`);
    check(response, {
        'is status 200': (r) => r.status === 200
    });
}

export function concertOptionSearch() {
    let response = http.get(`http://localhost:8080/api/concert/queue/options?concert=1`);
    check(response, {
        'is status 200': (r) => r.status === 200
    });
}

function concertSeatSearch() {
    let response = http.get(`http://localhost:8080/api/concert/queue/1/seats`)

    check(response, {
        'is status 200': (r) => r.status === 200
    });
}


export function reservation() {
    const seatIdList = [randomIntBetween(1, 50), randomIntBetween(1, 50)];
    const request = {
        seatIdList: seatIdList
    }
    let response = http.post(
        `http://localhost:8080/api/concert/queue/reserve`,
        JSON.stringify(request),
        {
            headers: {'Content-Type': 'application/json'}
        });

    check(response, {
        'is status 200': (r) => r.status === 200
    });
}

export function concert_scenario() {
    concertSearch();
    sleep(5);
    concertOptionSearch();
    sleep(5);
    concertSeatSearch();
    sleep(5);
    reservation();
    sleep(10);
    reservation();
}
