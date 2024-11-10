 import java.util.*; // For ArrayList

 public final class FibonacciHeap<T> {

     public static final class Entry<T> {
         private int     mDegree = 0;       // Number of children
         private boolean mIsMarked = false; // Whether this node is marked
 
         private Entry<T> mNext;   // Next and previous elements in the list
         private Entry<T> mPrev;
 
         private Entry<T> mParent; // Parent in the tree, if any.
 
         private Entry<T> mChild;  // Child node, if any.
 
         private T      mElem;     // Element being stored here
         private double mPriority; // Its priority
 
 
         public T getValue() {
             return mElem;
         }

         public void setValue(T value) {
             mElem = value;
         }

         public double getPriority() {
             return mPriority;
         }
 

         private Entry(T elem, double priority) {
             mNext = mPrev = this;
             mElem = elem;
             mPriority = priority;
         }
     }
 

     private Entry<T> mMin = null;
 
     private int mSize = 0;

     public Entry<T> enqueue(T value, double priority) {
         checkPriority(priority);

         Entry<T> result = new Entry<T>(value, priority);
 
         // Merge this singleton list with the tree list. 
         mMin = mergeLists(mMin, result);
 
         // Increase the size of the heap; we just added something. 
         ++mSize;
 
         // Return the reference to the new element. 
         return result;
     }
 
     public Entry<T> min() {
         if (isEmpty())
             throw new NoSuchElementException("Heap is empty.");
         return mMin;
     }

     public boolean isEmpty() {
         return mMin == null;
     }

     public int size() {
         return mSize;
     }
 

     public static <T> FibonacciHeap<T> merge(FibonacciHeap<T> one, FibonacciHeap<T> two) {
         // Create a new FibonacciHeap to hold the result. 
         FibonacciHeap<T> result = new FibonacciHeap<T>();
 

         result.mMin = mergeLists(one.mMin, two.mMin);
 
         // The size of the new heap is the sum of the sizes of the input heaps. 
         result.mSize = one.mSize + two.mSize;
 
         // Clear the old heaps. 
         one.mSize = two.mSize = 0;
         one.mMin  = null;
         two.mMin  = null;
 
         // Return the newly-merged heap. 
         return result;
     }
 

     public Entry<T> dequeueMin() {
         // Check for whether we're empty. 
         if (isEmpty())
             throw new NoSuchElementException("Heap is empty.");

         --mSize;
 
         // Grab the minimum element so we know what to return. 
         Entry<T> minElem = mMin;

         if (mMin.mNext == mMin) { // Case one
             mMin = null;
         }
         else { // Case two
             mMin.mPrev.mNext = mMin.mNext;
             mMin.mNext.mPrev = mMin.mPrev;
             mMin = mMin.mNext; // Arbitrary element of the root list.
         }
 

         if (minElem.mChild != null) {
             // Keep track of the first visited node. 
             Entry<?> curr = minElem.mChild;
             do {
                 curr.mParent = null;
 

                 curr = curr.mNext;
             } while (curr != minElem.mChild);
         }
 

         mMin = mergeLists(mMin, minElem.mChild);
 
         // If there are no entries left, we're done. 
         if (mMin == null) return minElem;
 

         List<Entry<T>> treeTable = new ArrayList<Entry<T>>();
 

         List<Entry<T>> toVisit = new ArrayList<Entry<T>>();
 

         for (Entry<T> curr = mMin; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.mNext)
             toVisit.add(curr);
 
         // Traverse this list and perform the appropriate unioning steps. 
         for (Entry<T> curr: toVisit) {
             // Keep merging until a match arises. 
             while (true) {

                 while (curr.mDegree >= treeTable.size())
                     treeTable.add(null);

                 if (treeTable.get(curr.mDegree) == null) {
                     treeTable.set(curr.mDegree, curr);
                     break;
                 }
 
                 // Otherwise, merge with what's there. 
                 Entry<T> other = treeTable.get(curr.mDegree);
                 treeTable.set(curr.mDegree, null); // Clear the slot
 
                 Entry<T> min = (other.mPriority < curr.mPriority)? other : curr;
                 Entry<T> max = (other.mPriority < curr.mPriority)? curr  : other;
 

                 max.mNext.mPrev = max.mPrev;
                 max.mPrev.mNext = max.mNext;
 
                 // Make it a singleton so that we can merge it. 
                 max.mNext = max.mPrev = max;
                 min.mChild = mergeLists(min.mChild, max);
                 
                 // Reparent max appropriately. 
                 max.mParent = min;
 
                 // Clear max's mark, since it can now lose another child. 
                 max.mIsMarked = false;
 
                 // Increase min's degree; it now has another child.
                 ++min.mDegree;
 
                 // Continue merging this tree. 
                 curr = min;
             }
 

             if (curr.mPriority <= mMin.mPriority) mMin = curr;
         }
         return minElem;
     }
 

     public void decreaseKey(Entry<T> entry, double newPriority) {
         checkPriority(newPriority);
         if (newPriority > entry.mPriority)
             throw new IllegalArgumentException("New priority exceeds old.");
 
         // Forward this to a helper function. 
         decreaseKeyUnchecked(entry, newPriority);
     }

     public void delete(Entry<T> entry) {

         decreaseKeyUnchecked(entry, Double.NEGATIVE_INFINITY);
 
         // Call dequeueMin to remove it. 
         dequeueMin();
     }

     private void checkPriority(double priority) {
         if (Double.isNaN(priority))
             throw new IllegalArgumentException(priority + " is invalid.");
     }
 

     private static <T> Entry<T> mergeLists(Entry<T> one, Entry<T> two) {

         if (one == null && two == null) { // Both null, resulting list is null.
             return null;
         }
         else if (one != null && two == null) { // Two is null, result is one.
             return one;
         }
         else if (one == null && two != null) { // One is null, result is two.
             return two;
         }
         else { 
             Entry<T> oneNext = one.mNext; // Cache this since we're about to overwrite it.
             one.mNext = two.mNext;
             one.mNext.mPrev = one;
             two.mNext = oneNext;
             two.mNext.mPrev = two;
 
             // Return a pointer to whichever's smaller. 
             return one.mPriority < two.mPriority? one : two;
         }
     }

     private void decreaseKeyUnchecked(Entry<T> entry, double priority) {
         // First, change the node's priority. 
         entry.mPriority = priority;
 

         if (entry.mParent != null && entry.mPriority <= entry.mParent.mPriority)
             cutNode(entry);

         if (entry.mPriority <= mMin.mPriority)
             mMin = entry;
     }
 
 
     private void cutNode(Entry<T> entry) {
         // Begin by clearing the node's mark, since we just cut it. 
         entry.mIsMarked = false;
 
         // Base case: If the node has no parent, we're done. 
         if (entry.mParent == null) return;
 
         // Rewire the node's siblings around it, if it has any siblings. 
         if (entry.mNext != entry) { // Has siblings
             entry.mNext.mPrev = entry.mPrev;
             entry.mPrev.mNext = entry.mNext;
         }

         if (entry.mParent.mChild == entry) {
             // If there are any other children, pick one of them arbitrarily. 
             if (entry.mNext != entry) {
                 entry.mParent.mChild = entry.mNext;
             }

             else {
                 entry.mParent.mChild = null;
             }
         }
 
         // Decrease the degree of the parent, since it just lost a child. 
         --entry.mParent.mDegree;
 

         entry.mPrev = entry.mNext = entry;
         mMin = mergeLists(mMin, entry);
 

         if (entry.mParent.mIsMarked)
             cutNode(entry.mParent);
         else
             entry.mParent.mIsMarked = true;
 
         // Clear the relocated node's parent; it's now a root. 
         entry.mParent = null;
     }
 }