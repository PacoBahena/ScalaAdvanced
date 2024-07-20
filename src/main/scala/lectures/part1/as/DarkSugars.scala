package lectures.part1.as

import scala.util.{Failure, Success, Try}

object DarkSugars extends App {

  // 1

  def singleArgMethod(arg:Int): String = "hahahaha"

  singleArgMethod {
    // write some complex code
    54
  }

  // like Try

  val getMyNumber: Try[Int] = Try {
    val rand = new scala.util.Random
    val ranVal = rand.nextInt % 2
    if (ranVal == 0) 100 else throw new RuntimeException("wrong hahahaha")
  }

  val myans = getMyNumber.getOrElse(0)

  println(myans)

  List(1,2,3).map {
    x => x + 1
  }

  // syntax sugar #2 single abstract method

  trait Action {
    def act(x:Int): Int
  }

  val aP:Action  = (x: Int) => x + 1

  aP.act(5)

  // example :
  //Runnable

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("helo scala")
  })

  val aSweeterThread = new Thread( () => println("Hello Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a:Int): Unit
  }

  val anAbstractInstace: AnAbstractType = (b:Int) => b + 3

  // which is syntax sugar for
  val aProperAbstractInstace: AnAbstractType = new AnAbstractType {
      override def f(a: Int): Unit = println("nothing happens")
  }

  class prop(name:String) extends AnAbstractType {
    override def f(a:Int): Unit = println("im proper")
  }

  // 3

  val prependedList = 2 :: List(3,4)
  println(prependedList)

  // because the method ends with :, it is right associative .
  // if it does not end in :, it is left associative.
  // hence ffor case above 2 :: List(3,4) works as List(3,4).::(2)

  class MyStream[T](a: T) {
    def -->:(x:T): MyStream[T] = new MyStream(x)
    def getVal: T = a
  }

  object MyStream {
    def apply[T](theval: T): MyStream[T] = new MyStream[T](theval)
  }

  val startStream: MyStream[Int] = MyStream(5)

  val myStream = 1 -->: 2 -->: 3 -->: startStream
  // becomes

  println(startStream.-->:(3).isInstanceOf[MyStream[Int]])

  println(myStream.getVal)
  println(myStream.getVal == 1)

  // ultiword method naming

  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val Lilly = new TeenGirl("lilly")
  Lilly `and then said` "tony has diarrea"

  // infix types

  class -->[A,B](x: A,y:B)

  val towards: String --> String = new -->[String,String]("caca","puntoB")

  // update is very special, just like apply

  val anArray =
    Array(1,2,3)

  anArray(2) = 7 // reqritten to anArray.update(2,7)

  // used in multiple collections
  // remember apply() and update()

  // syntax sugar: setters for multiple containers

  class Mutable {
    private var internalMember: Int = 0
    def member: Int = internalMember // "getter"
    def member_=(value:Int): Unit = {
      internalMember = value
    }
  }

  val aMutable = new Mutable

  println(aMutable.member)
  aMutable member_= 30
  println(aMutable.member)





  // becomes MyStream(5)


}
