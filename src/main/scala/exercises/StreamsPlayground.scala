package exercises

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def isEmpty: Boolean

  def head: A

  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // prepend operator

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concatenate two streams

  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): MyStream[B]

  def flatMap[B](f: A => MyStream[B]): MyStream[B]

  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elems from the Stream



  /*
  * [1 2 3].toList([])
  * [2 3].toList([1])
  * [ 3].tolist([2 1])
  * [].toList([3 2 1])
  * [3 2 1]
  * */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object MyStream {
  def from[A](start: A)(generator: A=> A): MyStream[A] = new Cons(start, MyStream.from(generator(start))(generator))
}

object EmptyStream extends MyStream[Nothing] {
  override def isEmpty: Boolean = true

  override def head: Nothing = throw new NoSuchElementException

  override def tail: MyStream[Nothing] = throw new NoSuchElementException

  override def #::[B >: Nothing](element: B): MyStream[B] = new Cons[B](element,this)

  override def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): MyStream[B] = this

  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  override def take(n: Int): MyStream[Nothing] = this

//  override def takeAsList(n: Int): List[Nothing] = Nil


}

class Cons[A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  override def isEmpty: Boolean = false

  override val head: A = hd

  override lazy val tail: MyStream[A] = tl  // CALL BY NEED


  /*
  * val s = new Cons(1, EmptyStream)
  * val prepended = 1 #:: s = new Cons(1, s)
  *
  * */
  override def #::[B >: A](element: B): MyStream[B] = new Cons(element,this)

  override def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  override def map[B](f: A => B): MyStream[B] = {
    new Cons(f(head),tail.map(f)) // preserves lazy evaluation
  }
  /*
  * s = new Cons(1,?)
  * mapped = s.map(_ + 1) = new Cons(2, s.tail.map( _ +1 ) )
  * note until i use mapped.tail will not evaluate second arg.
  * */

  override def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  override def filter(predicate: A => Boolean): MyStream[A] =
    if (predicate(head)) new Cons(head,tail.filter(predicate))
    else tail.filter(predicate) // preserve lazu evaluation

  override def take(n: Int): MyStream[A] = {
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons(head,EmptyStream)
    else new Cons(head, tail.take(n-1))
  }

//  override def takeAsList(n: Int): List[A] = ???
}


object StreamsPlayground extends App{
  val naturals = MyStream.from(start=1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.tail.head)

  val startFrom0 = 0 #:: naturals
  println(startFrom0.head)

//  startFrom0.take(10000).foreach(println)

  println(startFrom0.map(_ * 2).take(100).toList())
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).take(20).toList()) // impossible to know if the filter will give finite/infinite.


  // Exercises on streams

  // 1 - stream of fibonacci numbers
  // 2 - stream of prime numbers with Erathostenes sleve

  // fibo

 /*
  [ first, [ ...]
  [ first, fibo(second, first + second)]
  */

  def fibonacci(first: Int, second: Int): MyStream[Int] =
    new Cons(first,fibonacci(second, first + second))

  println(fibonacci(1,1).take(10).toList())

  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if( numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())
}
