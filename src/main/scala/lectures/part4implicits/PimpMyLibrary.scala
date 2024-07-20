package lectures.part4implicits

import scala.annotation.tailrec

object PimpMyLibrary extends App {

  implicit class RichInt(value:Int) {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
    def times(f: () => Unit): Unit = {

      def timesAux(n:Int): Unit = {
        if (n <=0) ()
        else {
          f()
          timesAux(n-1)
        }
      }
      timesAux(value)
    }
    def *[A](list: List[A]): List[A] = {
      // List(1 to value).flatten.flatMap(_ => list)
      def concatenate(n: Int): List[A] = {
        if (n <= 0) List()
        else concatenate(n - 1) ++ list
      }
      concatenate(value)
    }
  }

  implicit class RicherInt(value: Int) {
    def isOdd: Boolean = value % 2 == 1
  }

  println(42.isEven) // new RichInt(42).isEven
  println(42.sqrt)
  println(s"the value 42 % 2 is ${42.isOdd}")

  1 to 10 // example

  import scala.concurrent.duration._

  3.seconds

  println(3 * List(1,2))

  print(3.times(println))

  // compiler doesnt do multiple implicit searches.

  /*
  * Enrich the String class
  * - asInt
  * - encrypt
  *   "john" -> Lnpj
  *
  * Keep Enriching the int class
  * - times(function)
  *  3.times(() => ...)
  *  3 * List(1,2) => (List(1,2,1,2,1,2))
  * */

  implicit class RichString(string:String) {
    def asInt:Int = Integer.valueOf(string)
    def encrypt(cypherDistance:Int): String = string.map( c=> (c + cypherDistance).toChar)

  }

  println("6".asInt / 2)

  implicit def stringToInt(s:String): Int = {
    Integer.valueOf(s)
  }

  println("6" / 2 )



  // equivalent to implicit class RichAlt(value:Int)
  class RichAlt(value:Int)
  implicit def enrich(value:Int): RichAlt = new RichAlt(value)

  // danger zone
  // implicit def intToBoolean(i:Int): Boolean = i == 1

  // dont implicit convert with methods, use implicit classes.




}
