/*
package chesspath

import akka.actor.{Actor, PoisonPill, Props}
import akka.event.Logging

class PathActor(implicit board: Board) extends Actor {
  val log = Logging(context.system, this)
  implicit val ordered:Ordering[PathFromTile]=new Ordering[PathFromTile]{
    override def compare(x: PathFromTile, y: PathFromTile): Int = {
      if(x.endTile.getValidPaths.size > y.endTile.getValidPaths.size) -1
      else if(x.endTile.getValidPaths.size == y.endTile.getValidPaths.size) 0
      else 1
    }
  }
  def receive = {
    case msg:Message => {
      msg.tile.getValidPaths.sorted(ordered) match {
        case Nil => CompletePath(List(PathFromTile(msg.list, msg.tile)))
        case list => list .filter(p=>
         ! p.tiles.exists(tile=> msg.list.contains(tile))) match{
          case Nil=>
              PathFromTile(msg.list,msg.tile).allTilesCovered match{
                case true=>
                  context.parent  !  CompletePath(List(PathFromTile(msg.list, msg.tile)))
                case false=>
                  CompletePath(List(PathFromTile(msg.list, msg.tile)))
                 // context.parent ! PoisonPill
                  context.stop(self)
              }
        //    CompletePath(List(PathFromTile(msg.list, msg.tile)))
          case filteredList=>
           val x=filteredList
            filteredList.foreach({
        path =>
        if (path.copy(tiles = path.tiles ::: msg.list).allTilesCovered)
       context.parent !  CompletePath(List(path.copy(tiles = path.tiles :::msg.list )))

        else {
        val tiles:List[Tile] = path.tiles.toList
        context.actorOf(PathActor.props, path.endTile.row.toString + path.endTile.col.toString) ! Message(tiles:::msg.list, path.endTile)
        }
        })
        }


      }
    }
    case path: CompletePath => log.info("path received from child")
      context.parent  ! path

    case PoisonPill=>
      context.parent ! PoisonPill
      context.stop(self)
     // context.stop(self)
  }


  }
object PathActor {
  def props(implicit board: Board) = Props(new PathActor())
}
*/
