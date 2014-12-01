package chess

class ChessRunner {

    static void main(String[] args) {
        def sizeX = 4
        def sizeY = 4
        def layouts = Chess.solve(sizeX, sizeY, ["N","R","N","R","N","N"])
        Chess.prettyPrintAllLayouts(layouts, sizeX, sizeY)
    }
}
