/*
package chesspath

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.event.Logging
class ControllerActor(implicit board: Board) extends Actor{
  val log = Logging(context.system, this)
  var s:ActorRef=_
  implicit val ordered:Ordering[PathFromTile]=new Ordering[PathFromTile]{
    override def compare(x: PathFromTile, y: PathFromTile): Int = {
      if(x.endTile.getValidPaths.size > y.endTile.getValidPaths.size) -1
      else if(x.endTile.getValidPaths.size == y.endTile.getValidPaths.size) 0
      else 1
    }
  }
  var pathList:List[PathFromTile]=List()
  def receive = {

    case "test"=>
      context.parent! "test"
    case firstTile:Tile => log.info("received test")
       s =sender()
      firstTile.getValidPaths.sorted(ordered) match{
        case Nil=> CompletePath(List(PathFromTile(List(),Tile(0,0))))
        case list=>
          var pathFromTile=list.tail.map(p=>p.tiles:::List(firstTile))
        context.actorOf(PathActor.props,list.head.endTile.row.toString+list.head.endTile.col.toString) ! Message( list.head.tiles::: List(firstTile),list.head.endTile )

      }

    case path:CompletePath=>
      path.list.filter(_.allTilesCovered) match{
        case Nil=>pathList.isEmpty match{
          case true=> s ! CompletePath(List())
          case false=>var pathFromTile=pathList.tail
            context.actorOf(PathActor.props,pathList.head.endTile.row.toString+pathList.head.endTile.col.toString) ! Message( pathList.head.tiles,pathList.head.endTile )

        }
        case list=>
          s !  path.copy(list= path.list.filter(_.allTilesCovered))
          context.stop(self)
      }

    case _ =>
      log.info("received unknown message")


  }

}
object ControllerActor {
  def props(implicit board: Board) = Props(new ControllerActor())
}
*/
