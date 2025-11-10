"""
Compatibility shim for Crypto.Cipher.AES using cryptography library.
This allows victron_ble to work on systems that have cryptography but not pycryptodome.
"""

from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend

MODE_CTR = 'CTR'

class AESCipher:
    """AES cipher wrapper"""
    
    def __init__(self, key, mode, counter):
        self.cipher = Cipher(
            algorithms.AES(key),
            modes.CTR(counter),
            backend=default_backend()
        )
        self.decryptor = self.cipher.decryptor()
    
    def decrypt(self, data):
        return self.decryptor.update(data)

def new(key, mode, counter):
    """Create new AES cipher"""
    return AESCipher(key, mode, counter)
