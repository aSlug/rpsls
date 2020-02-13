package example

object RPSLS extends Greeting with GameProposal with App {
  println(greeting)
  println(gameProposal)
  Game.play()
}

trait Greeting {
  lazy val greeting: String = "hello"
}

trait GameProposal {
  lazy val gameProposal: String = "wanna play?"
}
