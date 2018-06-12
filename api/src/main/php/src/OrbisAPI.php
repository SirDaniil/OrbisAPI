<?php

namespace App;

use Jose\Factory\JWEFactory;
use Jose\Factory\JWKFactory;
use Ramsey\Uuid\Uuid;

class OrbisAPI
{
    private
        /**
         * @var string
         */
        $appName,

        /**
         * @var string
         */
        $accessToken,

        /**
         * @var string
         */
        $certPath,

        /**
         * @var array
         */
        $payload,

        /**
         * @var \Jose\Object\JWK
         */
        $jwk,

        /**
         * @var array
         */
        $sharedHeaders,

        /**
         * @var string
         */
        $jwe;

    /**
     * OrbisAPI constructor.
     * @param string $appName
     * @param string $accessToken
     * @param string $certPath
     */
    public function __construct(string $appName, string $accessToken, string $certPath)
    {
        $this->appName = $appName;
        $this->accessToken = $accessToken;
        $this->certPath = $certPath;

        $this->setPayload();
        $this->setJWK();
        $this->setSharedHeaders();

        return $this;
    }

    /**
     * @return OrbisAPI
     */
    public function generateJWE(): OrbisAPI
    {
        $this->jwe = JWEFactory::createJWEToCompactJSON(
            $this->payload,
            $this->jwk,
            $this->sharedHeaders
        );

        return $this;
    }

    /**
     * @return string
     */
    public function toBase64()
    {
        return base64_encode($this->jwe);
    }

    /**
     * @return string
     */
    public function toString()
    {
        return $this->jwe;
    }

    private function setPayload()
    {
        $this->payload = [
            'iss' => $this->appName,                     // Usually domain name
            'iat' => now()->timestamp,                   // Issuer time
            'nbf' => now()->timestamp,                   // Not before time
            'exp' => now()->addSeconds(10)->timestamp,   // Expiration time
            'jti' => Uuid::uuid4()->toString(),          // unique uuid4 on every request
        ];
    }

    private function setJWK()
    {
        $this->jwk = JWKFactory::createFromKeyFile(
            base_path($this->certPath),
            null,
            [
                'kid' => $this->accessToken,
                'use' => 'enc',
                'alg' => 'RSA-OAEP-256',
            ]
        );
    }

    private function setSharedHeaders()
    {
        $this->sharedHeaders = [
            'kid' => $this->accessToken,
            'alg' => 'RSA-OAEP-256',
            'enc' => 'A128GCM',
        ];
    }
}