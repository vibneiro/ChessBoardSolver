package chess

class Utils {

    // Choose all C(n,k) combinations
    def static choose(def itemset, int choose) {
        def results = []

        //Initialize indices
        int[] indices = new int[choose]
        for (i in 0..<choose) {
            indices[i] = i
        }

        boolean hasMore = true;
        while (hasMore) {
            def combo = []
            for (i in 0..<indices.size()) {
                combo << itemset[indices[i]]
            }
            results << combo
            hasMore = { /* Closure to move the right-most index */
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
                    return true
                }
                // reached the end, no more combinations
                return false
            }() // execute closure
        }
        return results
    }

}
