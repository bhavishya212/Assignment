package chesspath


import akka.actor.{ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout


import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import scala.collection.immutable.Stack
import scala.util.{Failure, Success}


object Main extends App {


  // implicit val board = Board(6,10,1,5)

  var pathAndBoard: (List[Tile], List[Board]) = (List(), List())

  def getCompletePath(board: Board, startingTile: Tile, list: PathFromTile, finalList: List[Tile]): List[Tile] = {
    implicit val ordered: Ordering[PathFromTile] = new Ordering[PathFromTile] {
      override def compare(x: PathFromTile, y: PathFromTile): Int = {
        if (x.endTile.getValidPaths.size > y.endTile.getValidPaths.size)
          -1
        else if (x.endTile.getValidPaths.size == y.endTile.getValidPaths.size)
          0
        else
          1
      }
    }


    implicit val orderedTile: Ordering[Tile] = new Ordering[Tile] {
      override def compare(x: Tile, y: Tile): Int = {
        if (x.getValidPaths.size > y.getValidPaths.size)
          -1
        else if (x.getValidPaths.size == y.getValidPaths.size)
          0
        else
          1
      }
    }

    if (finalList.size == board.size)
      finalList
    else {
      scala.util.Random.shuffle(startingTile.getValidPaths) match {
        case Nil => {
          finalList
        }
        case validList => {
          val updatedList = if (list.tiles.isEmpty)
            (finalList, PathFromTile(validList, validList.head, board))
          else {
            validList.filter(tile => !finalList.contains(tile)) match {
              case Nil =>
                dropHeads(finalList, list, board)
              case fiteredList =>
                (finalList, list.copy(tiles = (fiteredList ::: list.tiles)))
            }
          }
          getCompletePath(board, updatedList._2.tiles.head, PathFromTile(updatedList._2.tiles.head.copy(isTraversed = true) :: updatedList._2.tiles.drop(1), updatedList._2.tiles.head, board), List(updatedList._2.tiles.head.copy(isTraversed = true)) ::: updatedList._1)
        }


      }
    }
  }

  // }


  /*if (x.tiles.size > y.tiles.size)
        1
      else if (x.tiles.size == y.tiles.size)
        0
      else
        -1
    }*/

  /* }
  implicit val timeout = Timeout(5000 seconds)*/
  // println("Hello")
  /*val config=ConfigFactory.load()
  val system = ActorSystem("chessboard",config)
  val controller = system.actorOf(ControllerActor.props, "chessboard")

  val path = controller ? Tile(1, 1)
  import scala.concurrent.ExecutionContext.Implicits.global
  path.onComplete(fut => fut match {
    case Success(path) =>
      println(path.asInstanceOf[CompletePath].list)
    case Failure(ex) => List()
  })
*/

  /* startingTile.getValidPaths match{
    case Nil=>List(completePath)
    case list=>list.flatMap(path=>getCompletePath(path.getEndTile,completePath
      .copy(list = (completePath.list ::: list.filter(p=>p.isValidPath && p.getEndTile.equals(path.getEndTile) &&
        ! p.tiles.exists(x=>completePath.list.exists(path=>path.tiles.contains(x))))))))
      //
  }*/


  /* startingTile.getValidPaths.sorted(ordered) match {
    case Nil => list.size match {
      case 100 =>
        list
      case 0 =>
        List()
      case _ =>
        getCompletePath(list.head.endTile, list.drop(1))
    }
    case validList =>
      val updatedList = if (list.isEmpty)
        validList
      else if (list.head.tiles.size == board.size)
        list
      else {
        val x = validList.filter(p => !p.tiles.exists(tile => list.head.tiles.contains(tile)))
        x.map(path =>
          PathFromTile(path.tiles ::: list.head.tiles, path.endTile)) ::: list.drop(1)
      }
      updatedList.size match {
        case 0 =>
          list
        case _ =>
          getCompletePath(updatedList.head.endTile, updatedList)
      }

    //  getCompletePath(updatedList.head.endTile, updatedList)

  }*/

  /* (list.map(x=>PathFromTile((completePath.list.head.tiles ++ x.tiles),x.endTile))) ++:  completePath.list.drop(1)
      (updatedList.filter(_.allTilesCovered)  match{
        case Nil=>
        case sList=>
      }*/

  /*else
      getCompletePath((updatedList.filter(_.isValidPath)).head.getEndTile,CompletePath(updatedList.filter(_.isValidPath)))
*/

  /* startingTile.getValidPaths match {
    case Nil => allCoveredPath
    case list => getCompletePath(list.head.endTile,completePath.copy(list = completePath.list.head +: list).filterDuplicatePaths,allCoveredPath)
  }
}*/
  val srow = if(args(0)==null)1 else if(args(0).toInt>10 || args(0).toInt<1 ) throw new RuntimeException("Invalid row number") else args(0).toInt
  val scol = if(args(1)==null) 1 else if(args(1).toInt>10 || args(1).toInt<10 ) throw new RuntimeException("Invalid col number") else args(1).toInt
  println(startCalculation(srow, scol))

  def startCalculation(row: Int, col: Int): List[Tile] = {
    pathAndBoard._2 size match {
      case 4 =>
        pathAndBoard._1
      case _ =>

        pathAndBoard = pathAndBoard.copy(_2 = getBoard(row, col) :: pathAndBoard._2)
        val blockPath = findBlockPath(getBoard(row, col), row, col)
        pathAndBoard = pathAndBoard.copy(_1 = blockPath._2 ::: pathAndBoard._1)
        startCalculation(blockPath._1.row, blockPath._1.col)
    }
  }


  def dropHeads(finalList: List[Tile], pathFromTile: PathFromTile, board: Board): (List[Tile], PathFromTile) = {
    pathFromTile.tiles match {
      case head :: tail => if (head.isTraversed)
        dropHeads(finalList.drop(1), PathFromTile(pathFromTile.tiles.drop(1), pathFromTile.tiles.drop(1).head, board), board)
      else (finalList, pathFromTile)
      case _ => (finalList, pathFromTile)
    }
  }

  def getPath(row: Int, col: Int, board: Board): List[Tile] = {


    getCompletePath(board, Tile(row, col, board), (PathFromTile(List(Tile(row, col, board)), Tile(row, col, board), board)), List(Tile(row, col, board)))

  }

  def getBoard(row: Int, col: Int) = {

    if (row >= 1 && row <= 5 && col>=1 && col<=5) Board(1, 5, 1, 5)
    //  getCompletePath(Board(1, 5, 1, 5), Tile(row, col, board), (PathFromTile(List(Tile(row, col, board)), Tile(row, col, board), board)), List(Tile(row, col, board)))

    else if (row >= 6 && row <= 10 && col>=1 && col <=5)
      Board(6, 10, 1, 5)
    //   getCompletePath(board, Tile(row, col, board), (PathFromTile(List(Tile(row, col, board)), Tile(row, col, board), board)), List(Tile(row, col, board)))

    else if (row >= 1 && row <= 5 && col>=6 && col<=10)
      Board(1, 5, 6, 10)
    //  getCompletePath(board, Tile(row, col, board), (PathFromTile(List(Tile(row, col, board)), Tile(row, col, board), board)), List(Tile(row, col, board)))

    // println(getCompletePath(Tile(1, 1), List[PathFromTile](PathFromTile(List(Tile(1, 1)), Tile(1, 1)))))
    else Board(6, 10, 6, 10)
  }


  def findBlockPath(board: Board, row: Int, col: Int): (Tile, List[Tile], Board) = {
    val path = getPath(row, col, board)
    if(pathAndBoard._2.size==4)
      (path.head,path,board)
    else{
    path.head.getPaths.filter(tile => tile.row <= 10 && tile.row >= 1 && tile.col <= 10 && tile.col >= 1 && !pathAndBoard._2.contains(getBoard(tile.row, tile.col))) match {
      case Nil =>
        findBlockPath(board, row, col)
      case l =>
        val x=l
        (l.head, path, getBoard(l.head.row, l.head.col))
        }
    }

  }
}



