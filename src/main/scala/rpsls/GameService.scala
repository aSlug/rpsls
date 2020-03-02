package rpsls

import scala.util.Random

import model._
import Move._
import Outcome._
import java.{util => ju}
import scala.concurrent.Future
import cats.effect.Sync

trait GameService[F[_]] {
  def makePlay(playerMove: Move): F[Either[ApiError, Int]]
  def getResult(id: Int): F[Either[ApiError, Game]]
}

class GameServiceImpl[F[_]](repo: GameRepo[F])(implicit F: Sync[F])
    extends GameService[F] {

  override def makePlay(playerMove: Move): F[Either[ApiError, Int]] = {
    val botMove = generateBotMove()
    val outcome = calculateOutcome(playerMove, botMove)
    repo.write(Game(playerMove, botMove, outcome))
  }

  override def getResult(id: Int): F[Either[ApiError, Game]] = {
    repo.read(id)
  }

  private def generateBotMove(): Move = {
    Random.shuffle(Move.values.toList).head
  }

  private def calculateOutcome(playerMove: Move, botMove: Move): Outcome = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => Draw
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) |
          (Rock, Lizard) | (Scissors, Lizard) | (Paper, Spock) |
          (Lizard, Paper) | (Lizard, Spock) | (Spock, Rock) |
          (Spock, Scissors) =>
        Win
      case _ => Lose
    }
  }

}
