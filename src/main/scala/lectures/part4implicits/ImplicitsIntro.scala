package lectures.part4implicits



object ImplicitsIntro extends App {

  val pair = "Daniel" -> "555"
  val intPair = 1 -> 2

  val a = new ArrowAssoc[String]("Daniel")
  val c = a.->("B")

  case class Person(name: String) {
    def greet = s"Hi my name is $name"
  }

   //implicit conversion
  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("paco".greet)
//  println("paco".name)

  class A {
    def greet2: Int = 2
  }

  implicit def fromStringToA(str: String): A = new A

  val imA = "paco".greet2
  println(imA)


  // implicit parameters
  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val cuenta:Int = 100
  println(increment(2))
  println(increment(2)(4))




}
