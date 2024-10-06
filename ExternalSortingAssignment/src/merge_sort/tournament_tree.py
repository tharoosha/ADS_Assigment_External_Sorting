import heapq

class TournamentTree:
    def __init__(self, runs):
        self.heap = []
        self.runs = [iter(run) for run in runs]
        
        for i, it in enumerate(self.runs):
            first_item = next(it, None)
            if first_item is not None:
                heapq.heappush(self.heap, (first_item, i))

    def merge(self):
        merged_run = []
        while self.heap:
            min_val, i = heapq.heappop(self.heap)
            merged_run.append(min_val)
            next_item = next(self.runs[i], None)
            if next_item is not None:
                heapq.heappush(self.heap, (next_item, i))
        return merged_run
