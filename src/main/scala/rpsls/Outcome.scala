package rpsls

import io.buildo.enumero.annotations.enum

@enum trait Outcome {
  object Win
  object Lose
  object Draw
}