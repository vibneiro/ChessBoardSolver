package test

import chess.Chess
import org.junit.Test
import static org.junit.Assert.assertTrue

class ChessTest {

   @Test
    void squaresThreatenedByKingTest() {
       Chess chess = new Chess()
       assertTrue chess.getThreats("K", 1, 1, 3, 3) == [[x:1, y:2], [x:2, y:1], [x:2, y:2]]
       assertTrue chess.getThreats("K", 2, 2, 3, 3) == [[x:1, y:1], [x:1, y:2], [x:1, y:3], [x:2, y:1], [x:2, y:3], [x:3, y:1], [x:3, y:2], [x:3, y:3]]
       assertTrue chess.getThreats("K", 3, 3, 3, 3) == [[x:2, y:2], [x:2, y:3], [x:3, y:2]]
       assertTrue chess.getThreats("K", 3, 1, 3, 3) == [[x:2, y:1], [x:2, y:2], [x:3, y:2]]
       assertTrue chess.getThreats("K", 1, 3, 3, 3) == [[x:1, y:2], [x:2, y:2], [x:2, y:3]]
       assertTrue chess.getThreats("K", 1, 1, 1, 2) == [[x:1, y:2]]
       assertTrue chess.getThreats("K", 1, 1, 2, 1) == [[x:2, y:1]]
       assertTrue chess.getThreats("K", 1, 1, 1, 1) == []
    }

    @Test
    void squaresThreatenedByRookTest() {
        Chess chess = new Chess()
        assertTrue chess.getThreats("R", 1, 1, 3, 3) == [[x:2, y:1], [x:3, y:1], [x:1, y:2], [x:1, y:3]]
        assertTrue chess.getThreats("R", 2, 2, 3, 3) == [[x:3, y:2], [x:1, y:2], [x:2, y:3], [x:2, y:1]]
        assertTrue chess.getThreats("R", 3, 3, 3, 3) == [[x:2, y:3], [x:1, y:3], [x:3, y:2], [x:3, y:1]]
        assertTrue chess.getThreats("R", 3, 1, 3, 3) == [[x:2, y:1], [x:1, y:1], [x:3, y:2], [x:3, y:3]]
        assertTrue chess.getThreats("R", 1, 3, 3, 3) == [[x:2, y:3], [x:3, y:3], [x:1, y:2], [x:1, y:1]]
        assertTrue chess.getThreats("R", 1, 1, 1, 5) == [[x:1, y:2], [x:1, y:3], [x:1, y:4], [x:1, y:5]]
        assertTrue chess.getThreats("R", 1, 1, 5, 1) == [[x:2, y:1], [x:3, y:1], [x:4, y:1], [x:5, y:1]]
        assertTrue chess.getThreats("R", 1, 1, 1, 1) == []
    }

    @Test
    void squaresThreatenedByKnightTest() {
        Chess chess = new Chess()
        assertTrue chess.getThreats("N", 1, 1, 3, 3) == [[x:2, y:3], [x:3, y:2]]
        assertTrue chess.getThreats("N", 2, 2, 3, 3) == []
        assertTrue chess.getThreats("N", 3, 3, 3, 3) == [[x:2, y:1], [x:1, y:2]]
        assertTrue chess.getThreats("N", 3, 1, 3, 3) == [[x:2, y:3], [x:1, y:2]]
        assertTrue chess.getThreats("N", 1, 3, 3, 3) == [[x:2, y:1], [x:3, y:2]]
        assertTrue chess.getThreats("N", 3, 3, 5, 5) == [[x:4, y:5], [x:4, y:1], [x:5, y:4], [x:5, y:2], [x:2, y:5], [x:2, y:1], [x:1, y:4], [x:1, y:2]]
        assertTrue chess.getThreats("N", 1, 1, 1, 5) == []
        assertTrue chess.getThreats("N", 1, 1, 5, 1) == []
        assertTrue chess.getThreats("N", 1, 1, 1, 1) == []
    }

   @Test
   void squaresThreatenedByBishopTest() {
       Chess chess = new Chess()
       assertTrue chess.getThreats("B", 3, 3, 5, 5) == [[x:2, y:2], [x:1, y:1], [x:2, y:4], [x:1, y:5], [x:4, y:2], [x:5, y:1], [x:4, y:4], [x:5, y:5]]
       assertTrue chess.getThreats("B", 1, 1, 3, 3) == [[x:2, y:2], [x:3, y:3]]
       assertTrue chess.getThreats("B", 3, 3, 3, 3) == [[x:2, y:2], [x:1, y:1]]
       assertTrue chess.getThreats("B", 3, 1, 3, 3) == [[x:2, y:2], [x:1, y:3]]
       assertTrue chess.getThreats("B", 1, 3, 3, 3) == [[x:2, y:2], [x:3, y:1]]
       assertTrue chess.getThreats("B", 1, 1, 1, 1) == []
   }

   @Test
   void squaresThreatenedByQueenTest() {
       Chess chess = new Chess()
       assertTrue chess.getThreats("Q", 3, 3, 5, 5).size() == 16
       assertTrue chess.getThreats("Q", 1, 1, 3, 3) == [[x:2, y:1], [x:3, y:1], [x:1, y:2], [x:1, y:3], [x:2, y:2], [x:3, y:3]]
       assertTrue chess.getThreats("Q", 3, 3, 3, 3) == [[x:2, y:3], [x:1, y:3], [x:3, y:2], [x:3, y:1], [x:2, y:2], [x:1, y:1]]
       assertTrue chess.getThreats("Q", 3, 1, 3, 3) == [[x:2, y:1], [x:1, y:1], [x:3, y:2], [x:3, y:3], [x:2, y:2], [x:1, y:3]]
       assertTrue chess.getThreats("Q", 1, 3, 3, 3) == [[x:2, y:3], [x:3, y:3], [x:1, y:2], [x:1, y:1], [x:2, y:2], [x:3, y:1]]
       assertTrue chess.getThreats("Q", 1, 1, 1, 1) == []
   }

    @Test
    void update3x3FreeSquares2TimesTest() {

        Chess chess = new Chess()

        def free = [
                [x:1, y:1], [x:1, y:2], [x:1, y:3],
                [x:2, y:1], [x:2, y:2], [x:2, y:3],
                [x:3, y:1], [x:3, y:2], [x:3, y:3]
        ]
        def freeSquares = ['R' : free, 'N' : free]
        def nonFreeSquaresNew = [[x:1,y:1] : "R"]
        def threatenedSquaresNew = chess.getThreats("R", 1, 1, 3, 3)
        def freeSquaresNew = chess.updateFreeSquares(freeSquares, (nonFreeSquaresNew.keySet() + threatenedSquaresNew).flatten())

        assertTrue freeSquaresNew['R'].toSet() == [[x:2, y:3], [x:3, y:3], [x:3, y:2], [x:2, y:2]].toSet()
        assertTrue freeSquaresNew != freeSquares

        nonFreeSquaresNew += [[x:3,y:3] : "N"]
        threatenedSquaresNew = chess.getThreats("N", 3, 3, 3, 3)
        freeSquaresNew = chess.updateFreeSquares(freeSquaresNew, (nonFreeSquaresNew.keySet() + threatenedSquaresNew).flatten())

        assertTrue freeSquaresNew['R'].toSet() == [[x:2, y:3], [x:3, y:2], [x:2, y:2]].toSet()
        assertTrue freeSquaresNew['N'].toSet() == [[x:2, y:3], [x:3, y:2], [x:2, y:2]].toSet()
    }

    @Test
    void update2x2NoMoreFreeSquaresTest() {
        Chess chess = new Chess()
        def free = [
                [x:1, y:1], [x:1, y:2],
                [x:2, y:1], [x:2, y:2]
        ]
        def freeSquaresMap = ['Q' : free, 'N' : free]
        def nonFreeSquaresMapNow = [[x:1,y:1] : "Q"]
        def threatenedSquaresNow = chess.getThreats("Q", 1, 1, 2, 2)
        def freeSquaresMapNow = chess.updateFreeSquares(freeSquaresMap, (nonFreeSquaresMapNow.keySet() + threatenedSquaresNow).flatten())

        assertTrue freeSquaresMapNow['Q'].toSet() == [].toSet()
        assertTrue freeSquaresMapNow['N'].toSet() == [].toSet()
    }

    @Test
    void getNextPieceTest() {
       Chess chess = new Chess()
       assertTrue chess.getNextPiece(["K","Q","K","R","R","K","R"]) == [name:'Q', count:1]
       assertTrue chess.getNextPiece(["Q","K","K"]) == [name:'Q', count:1]
       assertTrue chess.getNextPiece(["Q","Q","Q"]) == [name:'Q', count:3]
    }

    @Test
    void getFreeSquaresForNextPieceTest() {
       def pieces = ["K","Q"]

       def free = [
               [x:1, y:1], [x:1, y:2], [x:1, y:3],
               [x:2, y:1], [x:2, y:2], [x:2, y:3],
               [x:3, y:1], [x:3, y:2], [x:3, y:3]
       ]

       def nonFreeSquaresMap = [:]
       def freeSquaresMap = [:]
       freeSquaresMap['K'] = free
       freeSquaresMap['Q'] = free
       def chess = new Chess()
       def positions = new Chess().getFreeSquaresForNextPiece(nonFreeSquaresMap, freeSquaresMap, chess.getNextPiece(pieces), 3, 3)
       assertTrue positions.size() == 9
    }

    @Test
    void checkThatOnly1LayoutWith1PieceOn1x1BoardTest() {
        def layouts = Chess.solve(1, 1, ["N"])
        assertTrue layouts.size() == 1
    }

    @Test
    void checkThatNoLayoutsWithRBOn2x2BoardTest() {
        def layouts = Chess.solve(2, 2, ["R","B"])
        assertTrue layouts.size() == 0
    }

    @Test
    void findLayoutsWith1NOn1x3BoardTest() {
        def layouts = Chess.solve(1, 3, ["N"])
        assertTrue layouts.size() == 3
    }

    @Test
    void checkThatNoLayoutsWith3PiecesOn1x3BoardTest() {
        def layouts = Chess.solve(1, 3, ["Q","Q"])
        assertTrue layouts.size() == 0
    }

    @Test(expected = IllegalArgumentException)
    void checkThatNoLayoutsWith3PiecesOn1x2BoardTest() {
        Chess.solve(1, 2, ["Q","Q","Q"])
    }

    @Test
    void checkThatOnly1LayoutWith3PiecesOn1x3BoardTest() {
        def layouts = Chess.solve(1, 3, ["N","N","N"])
        assertTrue layouts.size() == 1
    }

    @Test
    void findLayoutsWith1PieceOn2x2BoardTest() {
        def layouts = Chess.solve(2, 2, ["R"])
        assertTrue layouts.size() == 4
    }

    @Test
    void findLayoutsWithBNOn2x2BoardTest() {
        def layouts = Chess.solve(2, 2, ["B","N"])
        assertTrue layouts.size() == 8
    }

    @Test
    void find3x3LayoutsWithKRTest() {
        def layouts = Chess.solve(3, 3, ["K","K","R"])
        assertTrue layouts.size() == 4
    }

    @Test
    void find4x4LayoutsWith2R4NTest() {
        def layouts = Chess.solve(4, 4, ["N","R","N","R","N","N"])
        assertTrue layouts.size() == 8
    }

    @Test
    void find6x9LayoutsTest() {
       Chess.solve(6, 9, ["K","K","Q","B","R","N"])
    }

}