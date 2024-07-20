package lectures.part4implicits

object MyP extends App {

  val whati = Seq(1,2,3,4,5,6).groupBy(_ % 3)
  println(whati)
  val cu = whati.map(_._2)
  println(cu)

}
