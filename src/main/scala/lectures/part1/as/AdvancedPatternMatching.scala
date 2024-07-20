package lectures.part1.as

import lectures.part1.as.AdvancedPatternMatching.Name.unapplySeq

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ => println("this happened")
  }

  /*
  * - constants
  * - wildcards
  * - case classes
  * - tyokes
  * - some special magic like above
  * */

  // how do we patternmatch against a random class
  class Person(val name: String, val age:Int)

  // define an object

  object Person {
    def unapply(person: Person): Option[(String,Int)] = {
      if (person.age < 25 ) None
      else Some(person.name,person.age)
    }
    def unapply(age: Int): Option[String] = Some(if (age < 25) "minor" else "mayor")
  }

  val bob = new Person("Bob",25)

  val greeting = bob match {
    case Person(name,age) => s"hi my name is $name and age is $age"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"his legal status is $status"
  }

  println(legalStatus)

  /*
  * match against multiple conditions
  * */

  val n: Int = 45
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2  == 0 => "an even number"
    case _ => "no property"
  }

  object even {
    def unapply(arg: Int) = {
      if (arg % 2 == 0) Some(true)
      else None
    }
  }

  object singleDigit {
    def unapply(arg: Int) : Option[Boolean] = {
      if (arg < 10 && arg > -10) Some(true)
      else None
    }
  }

  val res = bob.age match {
    case even(_) => "the age is even"
    case singleDigit(_) => "single digit"
    case _ => "neither single digit nor even age"
  }

  println(res)

  // infix patterns

  case class Or[A,B](a:A,b:B)

  val either = Or(2,"two")
  val humanDescription = either match {
//    case Or(number,string) => s"$number is written as $string"
    case number Or string => "s$number is written as $string"
  }

  // decomposing sequences
  val numbers2 = Seq(1,2,3,4,54)
  val vararg = numbers2 match {
    case List(1,_*) => "starting with 1 "
    case _ => "something else "
  }

  println(vararg)


  // unapplyseq

  abstract class MyList[+A]{
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head:A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)

  }

  val myList: MyList[Int] = Cons(1,Cons(2,Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1,2, _*) => "starting with one and two"
    case _ => "something else "
  }

  println(decomposed)

  class Name(val firstName: String, val middleName: String, val lastName: String) {
    override def toString: String = {
      s"$lastName, $firstName [$middleName]"
    }
  }

  object Name {
    def apply(name: String): Name = {
      val names = name.split(" ")
      new Name(names.head, names.tail.dropRight(1).mkString(" "), names.last)
    }

//    def unapply(name:Name): Option[(String, String)] = {
//      Option(name.firstName,name.lastName)
//    }

    def unapplySeq(name: Name): Option[Seq[String]] = {
      Some(Seq(name.lastName, name.firstName) ++ name.middleName.split(" ").toSeq)
    }
  }

  val jr: Name = Name("John Ronald Tolkien")

  // extracting
  //val Name(first,last) = jr

  val Name(myF,myL,_*) = jr
  println(myF)
  println(myL)

  val namo = jr match {
    //case Name(all @ _*) => s"im $all"
    case Name(a,b,_*) => s"$a and $b"
  }

  println(namo)




  //println(s"$first an $last")

  // custom return types for unnapply, needs two defined methods
  // isEmpty: Boolean, get: something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get:T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get: String = person.name
    }

  }

  val boby = new Person("boby",30)

  println(boby match {
    case PersonWrapper(name) => s"im $name"
    case _ => "im an alien"
  })




}
