from .tournament_tree import TournamentTree
from .utils import read_chunk_from_file, write_chunk_to_file

def merge_sort_external(input_file, output_file, k=10, buffer_size=16 * 1024 * 1024):
    """
    External Merge Sort using Tournament Trees and K-Way merge.
    """
    runs = []
    
    # Phase 1: Create sorted runs
    with open(input_file, 'rb') as infile:
        while True:
            chunk = read_chunk_from_file(infile, buffer_size)
            if not chunk:
                break
            sorted_chunk = sorted(chunk)  # Sort the chunk internally
            runs.append(sorted_chunk)
    
    # Phase 2: Merge runs using Tournament Tree
    with open(output_file, 'wb') as outfile:
        while len(runs) > 1:
            runs = k_way_merge(runs, k)
        
        # Write final sorted output
        write_chunk_to_file(outfile, runs[0])

def k_way_merge(runs, k):
    merged_runs = []
    for i in range(0, len(runs), k):
        group = runs[i:i + k]
        tournament_tree = TournamentTree(group)
        merged_run = tournament_tree.merge()
        merged_runs.append(merged_run)
    return merged_runs
