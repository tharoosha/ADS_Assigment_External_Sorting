import heapq
from .interval_heap import IntervalHeap
from .utils import read_chunk_from_file, write_chunk_to_file

def quick_sort_external(input_file, output_file, buffer_size=16 * 1024 * 1024):
    """
    External Quick Sort using Interval Heap.
    """
    with open(input_file, 'rb') as infile, open(output_file, 'wb') as outfile:
        # Read chunks from input file within buffer size limit
        while True:
            chunk = read_chunk_from_file(infile, buffer_size)
            if not chunk:
                break

            # Sort the chunk using internal quick sort with interval heap
            sorted_chunk = quick_sort(chunk)
            
            # Write the sorted chunk back to the output file
            write_chunk_to_file(outfile, sorted_chunk)

def quick_sort(chunk):
    if len(chunk) <= 1:
        return chunk
    interval_heap = IntervalHeap(chunk)
    sorted_chunk = []
    
    while not interval_heap.is_empty():
        min_val, max_val = interval_heap.extract_min_max()
        sorted_chunk.extend([min_val, max_val])
        
    return sorted_chunk
