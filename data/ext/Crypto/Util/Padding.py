"""Padding utilities"""

def pad(data, block_size):
    """Pad data to block_size"""
    padding_len = block_size - (len(data) % block_size)
    return data + bytes([padding_len] * padding_len)
