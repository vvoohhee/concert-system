import http from 'k6/http';
import {check} from 'k6';
import {randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export let options = {
    scenarios: {
        scenario1: {
            vus: 300,
            exec: 'token_scenario',
            executor: 'per-vu-iterations',
            iterations: 1
        },
        scenario2: {
            vus: 100,
            exec: 'token_scenario',
            executor: 'per-vu-iterations',
            iterations: 2
        },
        scenario3: {
            vus: 100,
            exec: 'token_scenario',
            executor: 'per-vu-iterations',
            iterations: 5,
        },
    }
};

export function token_scenario() {
    const body = {
        userId: randomIntBetween(1, 500)
    }
    issueToken(body);
}

function issueToken(body) {
    let headers = {'Content-Type': 'application/json'};
    let response = http.post(
        `http://localhost:8080/api/token/issue`,
        JSON.stringify(body),
        {headers: headers}
    )

    check(response, {
        'is status 200': (r) => r.status === 200
    });
}