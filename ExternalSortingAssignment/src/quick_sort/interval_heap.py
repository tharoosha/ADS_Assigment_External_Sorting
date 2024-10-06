class IntervalHeap:
    def __init__(self, array):
        self.heap = []
        for num in array:
            heapq.heappush(self.heap, num)

    def extract_min_max(self):
        if len(self.heap) == 1:
            return heapq.heappop(self.heap), None
        min_val = heapq.heappop(self.heap)
        max_val = heapq.nlargest(1, self.heap)[0]
        self.heap.remove(max_val)
        return min_val, max_val

    def is_empty(self):
        return len(self.heap) == 0
