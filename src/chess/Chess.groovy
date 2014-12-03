package chess

import groovy.time.TimeCategory
import groovy.time.TimeDuration

class Chess {

    def counter = 0

    /*
       The main solving wrapper
       @param sizeX  Horizontal size of the board
       @param sizeY  Vertical size of the board
       @param pieces All pieces to be placed on the board
       @return       A collection of all layouts containing pairs of [coordinates, piece]
       @see          findLayouts method
     */
    def static solve(sizeX, sizeY, pieces) {

        if(sizeX*sizeY < pieces.size()) {
            throw new IllegalArgumentException("This number of pieces won''t fit that size of chess-board")
        }

        if (sizeX*sizeY == pieces.size && pieces.size == 1) {
            return 1
        }

        Chess chess = new Chess()
        def freeSquares = []

        for (i in 1..sizeX) {
            for(j in 1..sizeY) {
                freeSquares += [x: i, y: j]
            }
        }

        def timeStart = new Date()
        chess.findLayouts([:], freeSquares, pieces, sizeX, sizeY)
        def timeStop = new Date()
        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        println "Total Time of running: " + duration
        return chess.counter
    }

    /*
       Backtracking solver
       @param nonFreeSquares Already placed pieces on the board
       @param freeSquares    Keeps coordinates of where on the board we can be put so that not to threaten others.
       @param pieces         Pieces at our disposal left to place on the board
       @param sizeX          Horizontal size of the board
       @param sizeY          Vertical size of the board
       @return               A collection of all layouts containing pairs of [coordinates, piece]
     */
    def findLayouts(nonFreeSquares, freeSquares, pieces, sizeX, sizeY) {

        if(pieces.isEmpty()) {
            printLayout(nonFreeSquares, sizeX, sizeY)
            return
        }

        def positions = getFreeSquaresForNextPiece(nonFreeSquares, freeSquares, getNextPiece(pieces), sizeX, sizeY)

        if (positions.isEmpty()) {
            return // No solution for this layout, backtrack
        }

        for(e in positions) {
            findLayouts(e['nonFree'], e['free'], pieces - e['piece']['name'], sizeX, sizeY)
        }
    }

    def getFreeSquaresForNextPiece(nonFreeSquares, freeSquares, piece, sizeX, sizeY) {

        if (freeSquares == []) {
            return []
        }

        def positions = []

        // In-house intersection closure as Groovy cannot intersect (x,y) pairs properly
        def isThreat = { nonFreeSquaresThis, threatenedSquaresThis ->
            def nonFreeSet = nonFreeSquaresThis.keySet()
            for (e in threatenedSquaresThis) {
                if(nonFreeSet.contains(e)) return true
            }
            return false
        }

        def combinations = new Combinations(freeSquares, piece['count']) // Doing it lazily via Iterator

        for(comb in combinations) {
            def nonFreeSquaresNew = nonFreeSquares + []
            def threatenedSquares = []

              for (freeSquare in comb) {
                  threatenedSquares += getThreats(piece['name'], freeSquare['x'], freeSquare['y'], sizeX, sizeY)
                  nonFreeSquaresNew[freeSquare] = piece['name']
              }

            // Some squares are threatened, prune this configuration
            if(isThreat(nonFreeSquaresNew, threatenedSquares)) {
                continue;
            }

            def freeSquaresNew = updateFreeSquares(freeSquares, nonFreeSquaresNew, threatenedSquares)

            positions.add(['piece':piece, 'nonFree':nonFreeSquaresNew, 'free': freeSquaresNew])
        }

        return positions
    }

    def getThreats(String piece, int x, int y, int sizeX, int sizeY) {
        if (!(x in 1..sizeX) || !(y in 1..sizeY)) {
            throw new IllegalArgumentException("Incorrect location")
        }

        def listOfThreats = []
        def addToList = { newX, newY, nX, nY ->
            if(newX in 1..sizeX && newY in 1..sizeY && !(nX == 0 && nY == 0)) { listOfThreats << [x: newX, y: newY] }}

        def addRookPos = {
            for(nX in 0..<sizeX) {
                addToList(x+nX, y, nX, 0)
                addToList(x-nX, y, nX, 0)
            }

            for(nY in 0..<sizeY) {
                addToList(x, y+nY, 0, nY)
                addToList(x, y-nY, 0, nY)
            }
        }

        def addBishopPos = {
            def signX = [-1, -1,  1, 1]
            def signY = [-1,  1, -1, 1]

            for(i in 0..<signX.size()) {
                def step = 1
                def curX = x + signX[i]*step
                def curY = y + signY[i]*step
                while((curX = x + signX[i]*step) in 1..sizeX && (curY = y + signY[i]*step) in 1..sizeY) {
                    addToList(curX, curY, -1, -1)
                    step++
                }
            }
        }

        if(piece == "K") {
            for(nX in [-1, 0, 1]) {
                for(nY in [-1, 0, 1]) {
                    addToList(x+nX, y+nY, nX, nY)
                }
            }
        } else if(piece == "N") {
            def nX = [1, 1, 2, 2, -1, -1, -2, -2]
            def nY = [2, -2, 1, -1, 2, -2, 1, -1]

            for (i in 0..<nX.size()) {
                addToList(x + nX[i], y + nY[i], nX[i], nY[i])
            }
        }
        else if(piece == "R") {
            addRookPos()
        } else if(piece == "B") {
            addBishopPos()
        } else if(piece == "Q") {
            addRookPos()
            addBishopPos()
        } else {
            throw new IllegalArgumentException("Unknown Piece Type: " + piece)
        }
        return listOfThreats
    }

    def updateFreeSquares(freeSquares, nonFreeSquares, threatenedSquares) {
        return (freeSquares.toSet() - (nonFreeSquares.keySet() + threatenedSquares).toSet())
    }

    def getNextPiece(pieces) {
         def entry = pieces.countBy { it[0] }.min { it.value }
         return [name:entry.key, count:entry.value]
    }

    def printLayout(layout, sizeX, sizeY) {
        println 'Layout No. ' + ++counter + ':'

        for(i in 1..sizeX) {
           for(j in 1..sizeY) {
              def k = [x:i, y:j];
              if(layout.containsKey(k)) {
                  def v = layout.get(k)
                  print v
              } else {
                  print '.'
              }
           }
           println ''
       }
        println ''
    }

}