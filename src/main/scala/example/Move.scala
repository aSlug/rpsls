package example

sealed trait Move
object Move {
    case object Rock extends Move
    case object Paper extends Move
    case object Scissors extends Move

  def fromString(i: String): Option[Move] = {
    i match {
      case "0" => Some(Rock)
      case "1" => Some(Paper)
      case "2" => Some(Scissors)
      case _ => None
    }
  }
}
