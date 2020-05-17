package chesspath



case class Board(startRow:Int,endRow:Int,startCol:Int,endCol:Int){

  val size=(endRow-startRow+1)* (endCol-startCol+1)

  def isValidTile(tile:Tile)=
    tile.row<=endRow && tile.col<=endCol && tile.row>=startRow && tile.col>=startCol
}


// Smallest box in chess board
case class Tile(row:Int,col:Int,board: Board,isTraversed:Boolean=false){

  override def toString={
    row.toString+"-"+col.toString
  }
  override def equals(obj: scala.Any): Boolean = {
    obj match{
      case tile:Tile=>row==tile.row && col ==tile.col
      case _=>false
    }
  }
  def isValid:Boolean= (row<=10 && col<=10 && row>=1 && col>=1)

  def getValidPaths= {
    val northPath = Tile(row + 3, col,board)
    val southPath = Tile(row - 3, col,board)
    val eastPath = Tile(row, col + 3,board)
    val westPath = Tile(row, col - 3,board)
    val northWestPath =  Tile(row + 2, col - 2,board)
    val northEastPath = Tile(row + 2, col + 2,board)
    val southWestPath =  Tile(row - 2, col - 2,board)
    val southEastPath = Tile(row - 2, col + 2,board)
    List(northPath,southPath,eastPath,westPath,northWestPath,northEastPath,
      southWestPath,southEastPath).filter(board.isValidTile(_))
  }

  def getPaths={
    val northPath = Tile(row + 3, col,board)
    val southPath = Tile(row - 3, col,board)
    val eastPath = Tile(row, col + 3,board)
    val westPath = Tile(row, col - 3,board)
    val northWestPath =  Tile(row + 2, col - 2,board)
    val northEastPath = Tile(row + 2, col + 2,board)
    val southWestPath =  Tile(row - 2, col - 2,board)
    val southEastPath = Tile(row - 2, col + 2,board)
    List(northPath,southPath,eastPath,westPath,northWestPath,northEastPath,
      southWestPath,southEastPath)
  }
  /* def getValidPaths= {
     val northPath = PathFromTile(List(Tile(row + 3, col)//, Tile(row + 2, col), Tile(row + 1, col)
     ),Tile(row + 3, col))
     val southPath = PathFromTile(List(Tile(row - 3, col)//, Tile(row - 2, col), Tile(row - 1, col)
     ),Tile(row - 3, col))
     val eastPath = PathFromTile(List(Tile(row, col + 3)//, Tile(row, col + 2), Tile(row, col + 1)
     ), Tile(row, col + 3))
     val westPath = PathFromTile(List(Tile(row, col - 3)//, Tile(row, col - 2), Tile(row, col - 1)
     ),Tile(row, col - 3))
     val northWestPath = PathFromTile(List(Tile(row + 2, col - 2)//, Tile(row + 1, col - 1)
     ), Tile(row + 2, col - 2))
     val northEastPath = PathFromTile(List(Tile(row + 2, col + 2)//, Tile(row + 1, col + 1)
     ),Tile(row + 2, col + 2))
     val southWestPath = PathFromTile(List(Tile(row - 2, col - 2)//, Tile(row - 1, col - 1)
     ), Tile(row - 2, col - 2))
     val southEastPath = PathFromTile(List(Tile(row - 2, col + 2)//, Tile(row - 1, col + 1)
     ),Tile(row - 2, col + 2))*/
  /*val northPath = PathFromTile(List(Tile(row + 1, col)//, Tile(row + 3, col)
  ),Tile(row + 1, col))
  val southPath = PathFromTile(List(Tile(row - 1, col)//, Tile(row - 3, col)
  ),Tile(row - 1, col))
  val eastPath = PathFromTile(List(Tile(row, col + 1)//, Tile(row, col + 3)
  ),Tile(row, col + 1))
  val westPath = PathFromTile(List(Tile(row, col - 1)//, Tile(row, col - 3)
  ),Tile(row, col - 1))*/

}

case class PathFromTile(tiles:List[Tile],endTile:Tile ,board:Board) {



  def isValidPath=tiles.forall(board.isValidTile(_))

  //&& tiles.distinct.size == tiles.size


  def getEndTile=tiles.head

  def setTileTraversed=PathFromTile(tiles.map(tile=>tile.copy(isTraversed = true)),endTile.copy(isTraversed = true),board)

  def allTilesCovered=tiles.size==board.size

}

case class CompletePath(list:List[PathFromTile]){
  def validCompletePaths= list.filter(path=>path.tiles.distinct.size==25)
  def filterDuplicatePaths=CompletePath(list.filter(l=>l.tiles.distinct.size == l.tiles.size))
}


