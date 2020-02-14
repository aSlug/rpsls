package example

object Move {
    sealed trait EnumVal
    case object Rock extends EnumVal
    case object Paper extends EnumVal
    case object Scissors extends EnumVal

  def fromString(i: String): Option[Move.EnumVal] = {
    i match {
      case "0" => Some(Move.Rock)
      case "1" => Some(Move.Paper)
      case "2" => Some(Move.Scissors)
      case _ => None
    }
  }
}
