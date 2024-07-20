package lectures.part1.as

import PartialFunction._

object pfun extends App {

  val squareRootPos: PartialFunction[Double, Double] = new PartialFunction[Double, Double] {
    def apply(x: Double) = Math.sqrt(x)

    def isDefinedAt(x: Double): Boolean = x >= 0
  }

  val squareRootNeg: PartialFunction[Double, Double] = {
    case x if x < 0 => Math.sqrt(x)
  }

  val sqW: PartialFunction[Double, Double] = {
    squareRootPos orElse squareRootNeg
  }

  val negativeOrZeroToPositive: PartialFunction[Int, Int] = {
    case x if x <= 0 => Math.abs(x)
  }

  val positiveToNegative: PartialFunction[Int, Int] = {
    case x if x > 0 => -1 * x
  }

  val swapSign: PartialFunction[Int, Int] = {
    positiveToNegative orElse negativeOrZeroToPositive
  }


}
