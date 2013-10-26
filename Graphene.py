import os
import random
PRIVATE_KEY_LOCATION = ".private.key"
PUBLIC_KEY_LOCATION = ".public.key"

def main():
    if not os.path.exists(PRIVATE_KEY_LOCATION):
        private_key, public_key = generate_RSA_keypair()
        with open(PRIVATE_KEY_LOCATION, 'w') as f:
            f.write(private_key)
        with open(PUBLIC_KEY_LOCATION, 'w') as f:
            f.write(public_key)


def generate_RSA_keypair():
    return None, None


def generate_constants(n):
    return tuple(random.random() for __ in range(n-1))


