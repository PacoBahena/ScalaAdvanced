package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  /*
  *  implemetn a functional set.
  * */

  def apply(elem: A): Boolean = contains(elem)
  def contains(elem: A): Boolean
  def +(elem:A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]


  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  /*
  * Exercise
  * - and element
  * &ion
  * --
  * */
  def -(elem: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]

  /*
  * Exercise implement unary negation of a set
  * ! MySet(1,2,3) => everything but 3
  * */

  def unary_! : MySet[A]
}

//class AllInclusiveSet[A] extends MySet[A] {
//  override def contains(elem: A): Boolean = true
//
//  override def +(elem: A): MySet[A] = this
//
//  override def ++(anotherSet: MySet[A]): MySet[A] = this
//
//  // val natural = new allInclusive[Int]
//  // natural.filter(_ % 3) = MySet(1,2,3)
//  override def map[B](f: A => B): MySet[B] = ???
//
//  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
//
//  override def filter(predicate: A => Boolean): MySet[A] = ??? // property based set
//
//  override def foreach(f: A => Unit): Unit = ???
//
//  override def -(elem: A): MySet[A] = this.filter(x => x != elem)
//
//  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
//
//  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
//
//  override def unary_! : MySet[A] = new EmptySet[A]
//}

// all elemts of type A that satisfy a property
// { xin A property }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  override def contains(elem: A): Boolean = property(elem)

  // { x in A | property(x) || x == element}
  override def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == elem)

  // property(x) holds or x is in anotherSet
  override def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => (property(x) || anotherSet(x)))

  override def map[B](f: A => B): MySet[B] = politelyFail

  override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

  override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = politelyFail

  override def -(elem: A): MySet[A] = filter(_ != elem)

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def unary_! : MySet[A] = new PropertyBasedSet[A](!property(_))

  def politelyFail = throw new IllegalArgumentException("really deep rabbit hole ")
}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean = false

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem,this)

  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def filter(predicate: A => Boolean): MySet[A] = this

  override def foreach(f: A => Unit): Unit = ()

  override def -(elem: A): MySet[A] = this

  override def &(anotherSet: MySet[A]): MySet[A] = this

  override def --(anotherSet: MySet[A]): MySet[A] = this

  override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}
class NonEmptySet[A](head: A, tail: MySet[A] ) extends MySet[A] {

  override def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  override def +(elem: A): MySet[A] = {
    if(this.contains(elem)) this
    else new NonEmptySet[A](elem,this)
  }

  override def ++(anotherSet: MySet[A]): MySet[A] = {
    tail ++ anotherSet + head
  }

  override def map[B](f: A => B): MySet[B] = {
    tail.map(f) + f(head)
  }

  override def flatMap[B](f: A => MySet[B]): MySet[B] = {
    tail.flatMap(f) ++ f(head)
  }

  override def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  override def -(elem: A): MySet[A] = {
    if (this.contains(elem)) {
      if (head == elem) tail
      else tail - elem + head
    }
    else {
      this
    }
  }

  override def &(anotherSet: MySet[A]): MySet[A] = this.filter(anotherSet)

  override def --(anotherSet: MySet[A]): MySet[A] = this.filter(elem => !anotherSet(elem))

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this(x))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1,2,3,4)
  s + 5 foreach println
  (s ++ MySet(-1,-2) + (2)) flatMap(x => MySet(10 * x)) filter ( _ % 2 == 0) foreach println

  val negative = !s  // all naturals not equal to 1,2,3,4
  println(negative(-5))
  println(negative(2))
}


