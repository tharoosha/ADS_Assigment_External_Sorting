def read_chunk_from_file(file, buffer_size):
    """
    Reads a chunk of data from a file, returns as a list of integers.
    """
    chunk = file.read(buffer_size)
    if not chunk:
        return []
    
    # Convert binary data to list of integers
    return [int.from_bytes(chunk[i:i + 4], byteorder='little') for i in range(0, len(chunk), 4)]

def write_chunk_to_file(file, chunk):
    """
    Writes a sorted chunk of data to a file.
    """
    for num in chunk:
        file.write(num.to_bytes(4, byteorder='little'))
