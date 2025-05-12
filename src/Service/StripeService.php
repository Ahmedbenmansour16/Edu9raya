<?php

namespace App\Service;

use Stripe\Stripe;
use Stripe\PaymentIntent;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;

class StripeService
{
    private $params;

    public function __construct(ParameterBagInterface $params)
    {
        $this->params = $params;
        Stripe::setApiKey($this->params->get('stripe_secret_key'));
    }

    public function createPaymentIntent(float $amount): PaymentIntent
    {
        try {
            // Convert TND to USD (approximate conversion rate: 1 TND = 0.32 USD)
            $amountInUSD = $amount * 0.32;
            
            return PaymentIntent::create([
                'amount' => (int)($amountInUSD * 100), // Convert to cents and ensure it's an integer
                'currency' => 'usd',
                'payment_method_types' => ['card'],
                'metadata' => [
                    'integration_check' => 'accept_a_payment',
                    'original_amount_tnd' => $amount,
                ],
            ]);
        } catch (\Exception $e) {
            throw new \Exception('Error creating payment intent: ' . $e->getMessage());
        }
    }

    public function getPublicKey(): string
    {
        try {
            $publicKey = $this->params->get('stripe_public_key');
            if (empty($publicKey)) {
                throw new \Exception('Stripe public key is not configured');
            }
            return $publicKey;
        } catch (\Exception $e) {
            throw new \Exception('Error getting Stripe public key: ' . $e->getMessage());
        }
    }
}
