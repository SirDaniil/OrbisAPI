<?php

namespace App;

use GuzzleHttp\Client;

class Example
{
    public function exampleRequest()
    {
        $client = new Client();

        $apiClient = new OrbisAPI('my-app', 'my-access-token', 'my-cert-path');

        $jwe = $apiClient->generateJWE()->toBase64();

        $response = $client->get('orbis-api-url', [
            'headers' => [
                'Authorization' => 'Key ' . $jwe
            ],
        ]);

        echo $response->getBody()->getContents();
    }
}