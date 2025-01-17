package lectures.part5

object RockingInheritance extends App {

  // convenience

  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status:Int): Unit
  }

  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // the diamond problem

  trait Animal {
    def name: String
  }
  trait Lion extends Animal {
    override def name: String = "Lion"
  }
  trait Tiger extends Animal {
    override def name: String = "Tiger"
  }
  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name)

  // LAST OVERRIDE GETS PICKED FOR DIAMOND PROBLEM.

  // the super problem + type linearization

  trait Cold {
    def print: Unit = println("cold")
  }

  trait Green extends Cold {
    override def print(): Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print(): Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print: Unit = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }

  }

  val color = new White
  color.print

}
