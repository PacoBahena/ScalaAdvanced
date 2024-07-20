package lectures.part1.as

import scala.annotation.tailrec

object Recap extends App {

    val aCondition: Boolean = false
    val aConditionEval = if (aCondition) 42 else 65

    // instructions vs expressions. scala is expression based

  // compiler infers types.
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit expresions that only do side effects, equivalent void

  val theUnit = println("hello Scala")

  //functions

  def aFunction(x: Int): Int = x + 1

  //recursion: stack and tail.
  @tailrec def factorial(n:Int, accumulator: Int): Int =
    if (n<=0) accumulator
    else factorial(n-1, n * accumulator)

  class Animal
  class Dog extends Animal

  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a:Animal): Unit = println("crunch")
  }

  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  // anonymous classes

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("eating, not a croc")
  }

  aCarnivore.eat(aCroc)

  val aCroc2: Crocodile = new Crocodile

  // generics

  abstract class MyList[+A] // covariance

  //singletons and companions
  object MyList

  case class Person(name:String,age:Int)
  // PErson is serializable, has companion object, getters and serters,
  // name and age are fields.

  //exceptions and try/catch/finally

  val throwsException: Nothing = throw new RuntimeException("this is invalid") // Nothing  has not instances.

  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "i caught an exception"
  } finally {
    println("some logs")
  }

  // packaging and imports.

  // functional programming

  val incrementer: Int => Int = new Function1[Int,Int] {
    override def apply(v1:Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x:Int) => x + 1

  List(1,2,3).map(anonymousIncrementer)
  val c = List(1,2,3).map(_+1)
  val d = List(1,2,3).map((x:Int) => x+1)

  // for - comprehension

  val pairs = for {
    num <- List(1,2,3)
    char <- List('a','b','c')
  } yield num + "-" + char

  // Scala collections: Seqs, Arrays, Lists

  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // collections: Option, Try

  val anOption = Some(2)

  val myval = anOption.getOrElse(54)

  // pattern matching

  val x = 2

  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case _ => "do not know"
  }

  val bob: Person = Person("bob",30)

  val greeting = bob match {
    case Person(n,_) => s"hi, my name is $n"
    case _ => "greetings stranger "
  }






}
