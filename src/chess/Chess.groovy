package chess

import groovy.time.TimeCategory
import groovy.time.TimeDuration

class Chess {

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

        Chess chess = new Chess()

        def freeCells = []

        for (i in 1..sizeX) {
            for(j in 1..sizeY) {
                freeCells += [x: i, y: j]
            }
        }
        def freeSquares = [:]

        for (p in pieces) {
            freeSquares[p] = freeCells
        }

        def timeStart = new Date()
        def layouts = chess.findLayouts([:], freeSquares, pieces, sizeX, sizeY)
        def timeStop = new Date()
        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        println "Total Time of running: " + duration
        return layouts
    }

    /*
       Backtracking solver
       @param nonFreeSquares Already placed pieces on the board
       @param freeSquares    Keeps coordinates of where on the board we can be put so that not to threaten others.
       @param pieces         Pieces at our disposal left to place on the board
       @return               A collection of all layouts containing pairs of [coordinates, piece]
     */
    def findLayouts(nonFreeSquares, freeSquares, pieces, sizeX, sizeY) {

        if(pieces == []) {
            return nonFreeSquares // return the configuration of layout
        }

        def positions = getFreeSquaresForNextPiece(nonFreeSquares, freeSquares, getNextPiece(pieces), sizeX, sizeY)

        // No solution for this layout, backtrack:
        if (positions == []) {
            return []
        }

        def layouts = []

        for(e in positions) {
            def piece = e['piece']
            def nonFreeSquaresNew = e['nonFree']
            def freeSquaresNew = e['free']

            def layout = findLayouts(nonFreeSquaresNew, freeSquaresNew, pieces - piece['name'], sizeX, sizeY)

            if (!layout.isEmpty()) {
                layouts.add(layout)
            }
        }

        return layouts.flatten()
    }

    def getFreeSquaresForNextPiece(nonFreeSquares, freeSquares, piece, sizeX, sizeY) {

        def frees = freeSquares[piece['name']]
        def count = piece['count']
        def positions = []

        // Non-optimized in-house intersection closure as groovy cannot properly process such (x,y) pairs
        def isThreat = { nonFreeSquaresThis, threatenedSquaresThis ->
            def nonFreeSet = nonFreeSquaresThis.keySet() + []
            for (e in threatenedSquaresThis) {
                if(nonFreeSet.contains(e)) return true
            }
            return false
        }

        if (count > 1) {
            // non-lazy combinations, slowers drastically work for large sets
            frees = Utils.choose(frees, count)
        }

        for(freeSquare in frees) {
            def nonFreeSquaresNew = nonFreeSquares + []
            def threatenedSquaresNew = []

            if(count > 1) {
              for (freeCell in freeSquare) {
                  threatenedSquaresNew += getThreats(piece['name'], freeCell['x'], freeCell['y'], sizeX, sizeY)
                  nonFreeSquaresNew[freeCell] = piece['name']
              }
            } else {
                threatenedSquaresNew = getThreats(piece['name'], freeSquare['x'], freeSquare['y'], sizeX, sizeY)
                nonFreeSquaresNew[freeSquare] = piece['name']
            }

            // Some squares are threatened, thus we avoid this configuration
            if(isThreat(nonFreeSquaresNew, threatenedSquaresNew)) {
                continue;
            }

            def updatedFreeSquares = updateFreeSquares(freeSquares, (nonFreeSquaresNew.keySet() + threatenedSquaresNew).flatten())

            // Out of free squares for some piece, thus we avoid this configuration
            if (updatedFreeSquares.any{ it.value == [] }) {
                continue;
            }

            positions.add(['piece':piece, 'nonFree':nonFreeSquaresNew, 'free': updatedFreeSquares])
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

    def updateFreeSquares(freeSquares, nonFreeSquares) {
        def freeSquaresNew = freeSquares + []
        for(e in freeSquaresNew) {
            def frees = e.value.toSet() - nonFreeSquares.toSet()
            freeSquaresNew[e.key] = frees
        }
       return freeSquaresNew
    }

    def getNextPiece(pieces) {
         def entry = pieces.countBy { it[0] }.min { it.value }
         return [name:entry.key, count:entry.value]
    }

    def static prettyPrintAllLayouts(layouts, sizeX, sizeY) {

       def format = { layout ->

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
       }

       println 'There are ' + layouts.size() + ' different layouts of chessboard:'

       int i = 1

       for (l in layouts) {
           println 'Layout No. ' + i++ + ':'
           format(l)
           println ''
       }

    }

}
