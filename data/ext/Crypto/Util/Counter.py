"""Counter mode for AES-CTR"""

class Counter:
    """Counter mode wrapper"""
    @staticmethod
    def new(nbits, initial_value=0, little_endian=False):
        """Create a counter for CTR mode"""
        # Convert initial_value to 16-byte nonce
        if little_endian:
            return initial_value.to_bytes(16, byteorder='little')
        else:
            return initial_value.to_bytes(16, byteorder='big')
