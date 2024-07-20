package lectures.part2

import scala.util.Try

object Monads extends App {

  // our own try monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  // lets prove the monad laws

  // left - identity
  // unit(x).flatMap(f) = f(x)
  // Attempt(x).flatMap(f)
  // Success(x).flatMap(f) = f(x)


  val f = (x:Int) => Success(x + 2)
  val myv1 = Success(5).flatMap(f)
  val myv2 = f(5)
  println(myv1)
  println(myv2)
  println(Success(5).flatMap(f) == Success(7))

  /*
  * right identity
  *
  * attempt(x).flatMap(unit) = attempt(x)
  * Success(x).flatMap(x => Attempt(x)) = Success(x)
  * Fail(_).flatMap(x => Attempt(x)) = Fail(_)
  * */
  //Try(Success(5))
  println(Success(5).flatMap(Attempt(_)) == Success(5))

  /* associativity
  * Attempt(x).flatMap(f).flatMap(g) = Attempt(x).flatMap(x => f(x).flapMap(g))
  * Fail(e).flatMap(f).flatMap(g) = Fail(e)
  * Fail(e).flatMap( x => f(x).flatMap(g)) = Fail(e)
  *
  * Success(x).flatMap(f).flatMap(g) = g(f(x))
  * Success(x).flatMap(x => f(x).flatMap(g)) = Success(x).flatMap(x => g(f(x))) = g(f(x))
  * * */

  val attempt = Attempt {
    throw new RuntimeException("My own monad yes")
  }

  println(attempt)

  /*
  * 1) Implement your own monad. A Lazy[T] Monad = computation which will be only executed when its needed
  * unit/apply
  * flatMap
  */

  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalvalue = value
    def use: A = internalvalue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalvalue)
  }

  object Lazy {
    def apply[A](value: => A) = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today i feel like not doing anything Paco")
    42
  }

  println(lazyInstance.use)

  val flatMappedInstance: Lazy[Int] = lazyInstance.flatMap(x => Lazy {10 * x})

  val flatMappedInstance2: Lazy[Int] = lazyInstance.flatMap(x => Lazy {12 * x})

  println(flatMappedInstance.use)
  println(flatMappedInstance2.use)
  println(lazyInstance.use)

  val a2 = flatMappedInstance2
  println(a2.use)
  /*proove monad laws
  * */
  val f1 = (x: Int) => Lazy{x * 10}
  val x = Lazy(3).flatMap{x => Lazy {10 * x}}
  println(x.use)
//  println(Lazy(4).use)

  def whileLoop(condition: => Boolean)(body: => Unit): Unit =
    if (condition) {
      body
      whileLoop(condition)(body)
    }

  var i = 2

  whileLoop(i > 0) {
    println(i)
    i -= 1
  } // prints 2 1

  /* 2) Monadas have unit/apply and flatMap. Alternative definition are unit + map + flatten
  *
  *  Monad[T] {
  *   def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
  *
  *   def map[B](f: T => B): Monad[B] = ???
  *
  *   def flatten(m: Monad[Monad[T]]): Monad[T] = ???
  *
  * }
  *
  *
  * */

  println(List(1,2,3).flatMap(x => List(x + 2)))
  println(List(1,2,3).map(_ + 2))

  println(List(1,2,3).flatMap(x => List(x)))

  println(Some(2).flatMap((x:Int) => Some(x+3)))

  List(List(1,2),List(2),List(4)).flatten

}
