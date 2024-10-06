from quick_sort.quick_sort import quick_sort_external
from merge_sort.merge_sort import merge_sort_external
import time

def run_experiment():
    # Define file paths
    input_file = 'data/unsorted_file_1.bin'
    quicksort_output = 'data/sorted_output/quicksort_sorted.bin'
    mergesort_output = 'data/sorted_output/mergesort_sorted.bin'

    # Run Quick Sort External
    start_time = time.time()
    quick_sort_external(input_file, quicksort_output)
    quicksort_time = time.time() - start_time
    print(f"Quick Sort External completed in {quicksort_time:.2f} seconds")

    # Run Merge Sort External
    start_time = time.time()
    merge_sort_external(input_file, mergesort_output)
    mergesort_time = time.time() - start_time
    print(f"Merge Sort External completed in {mergesort_time:.2f} seconds")

    # Compare results
    with open('results/performance_logs.txt', 'w') as log_file:
        log_file.write(f"Quick Sort External: {quicksort_time:.2f} seconds\n")
        log_file.write(f"Merge Sort External: {mergesort_time:.2f} seconds\n")

if __name__ == "__main__":
    run_experiment()
