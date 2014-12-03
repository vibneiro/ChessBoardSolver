package chess

import sun.reflect.generics.reflectiveObjects.NotImplementedException

// Lazy Iterator for choosing all C(n, k) combinations
// Doesn't require preliminary calculation or additional memory
// Uses only indices array of length k
class Combinations implements Iterator {

    int[] indices
    def itemset
    def choose
    boolean hasNext = true

    Combinations(def itemset, int choose) {
        this.choose = choose
        this.itemset = itemset

        //Initialize indices
        indices = new int[choose]
        for (i in 0..<choose) {
            indices[i] = i
        }
    }

    private def prepareNext() {
        int rightMostIndex = { /* Closure to find the right-most index */
            for (i in choose-1..0){
                int bounds = itemset.size() - choose + i
                if (indices[i] < bounds) return i
            }
            return -1
        }() // execute closure

        // increment all indices
        if (rightMostIndex >= 0) {
            indices[rightMostIndex]++
            for (i in rightMostIndex+1..<choose) {
                indices[i] = indices[i-1] + 1;
            }
            // there are still more combinations
            hasNext = true
            return
        }
        // reached the end, no more combinations
        hasNext = false
    }

    @Override
    boolean hasNext() {
        return hasNext
    }

    @Override
    def next() {
        if (!hasNext)
            throw new NoSuchElementException();

        def combination = []

        for (i in 0..<indices.size()) {
            combination << itemset[indices[i]]
        }
        prepareNext()
        return combination
    }

    @Override
    public void remove() {
        throw new NotImplementedException()
    }
}