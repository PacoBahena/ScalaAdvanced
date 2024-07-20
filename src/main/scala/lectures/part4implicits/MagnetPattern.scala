package lectures.part4implicits

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object MagnetPattern extends App {

  // method overloading
  class P2PRequest
  class P2PResponse
  class Serializer[T]
  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message:T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // def receive(future: Future[P2PResponse]): Int
  }

  /*
  Problems:
  1.- type erasure . see commented recevi of Future[P2PRequest]
  2.- lifting does n work for all overloads receive _
  3. code duplication
  4. type inference and default args.
  * */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("im P2PRequest")
      42
    }
  }

  implicit class FromP2PResponse(request: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("im P2PResponse")
      42
    }
  }
  val p2p = new P2PRequest
  val p2pr = new P2PResponse
  println(receive(p2p))
  println(receive(p2pr))

  /*
  * No more type erausre problems
  * */

  implicit class FromResponse(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply() = 2
  }

  implicit class FromRequest(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply() = 3
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(x: String) extends AddMagnet {
    override def apply(): Int = x.toInt + 1
  }

  val aFour: Int = add1(4)

  // say you ant to lift
  val aFValue: AddMagnet => Int = add1 _

  aFValue(4)
  aFValue("4")

  // you cannot lift if the magnet apply returns a generic type.

  // for example you cannot do
//  val rFv = receive _
//  rFv(new P2PResponse)
  // compiler gets confused with

  // magnet drawbacks

  /*1. super verbose
    2. harder to read
    3. cant name or place default arguments
    4. cannot use call by name. (side effects)
    */

  class handler {
    def handle(s: => String): Unit = {
      println(s)
      println(s)
    }
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }



  def sideEffectMethod(): String = {
    println("Hello Scala")
    "weird"
  }

  handle(sideEffectMethod())

  handle {
    println("hello hello")
    "completer"
  }

}
