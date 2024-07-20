package lectures.part2

import scala.collection.WithFilter

object LazyEvaluation extends App {

  // lazy delays de evaluation of values
  //lazy val x: Int = throw new RuntimeException

  lazy val x: Int = {
    println("hello")
    42
  }

  println(s"The value for $x")
  println(s"The value again for $x")

  // note on second call to print, no more print statement.

  // examples of implication

  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // why was boo not printed ? because lazy condition is never evaluated , since first argument is false

  // in conjunction with call by name

  def byNameMethod2(n: => Int): Int = n + n + n + 1
  def byNameMethod(n: => Int): Int = {
    lazy val t = n // only evaluated once // CALL BY NEED
    t + t + t + 1
  }
  def retrieveMagicValue: Int = {
    println("waiting")
    Thread.sleep(1000)
    42
  }



  println("example call by name")
  println(byNameMethod2(retrieveMagicValue))
  println
  println("example call by need")
  println(byNameMethod(retrieveMagicValue))
  println
  // filtering with lazy vals

  def lessThan30(i:Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1,25,40,5,23)

  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy: WithFilter[Int, List] = numbers.withFilter(lessThan30)  // lazy vals under the hood.
  val gt20lazy: WithFilter[Int, List] = lt30lazy.withFilter(greaterThan20)

  println
  println(gt20lazy)

  gt20lazy.foreach(println)

  // for-comprehension use withFilter with guards


  /*
  * Exercise implement a lazily evaluated singly linked STREAM of elements
  *
  * Naturals = MyStream.from(1)(x => x + 1) = stream of natura lnumbers, potentially infinite.
  * naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals
  * naturals.foreach(println) will crash, infinite stream
  * naturals.map(_ * 2) stream of all even numbers potentially infinite
  *
  *  */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A=> MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elems from the Stream
    def takeAsList(n: Int): List[A]


  }


}
