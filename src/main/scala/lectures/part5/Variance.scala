package lectures.part5

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance ??
  // inheritance - type substitution of generics.

  class Cage[T]
  //should a cage of cat inherit from cage animal ?

  // if yes, Cage would be covariant

  class CCage[+T]

  val ccage: CCage[Animal] = new CCage[Cat]

  // invariance

  class ICage[T]
//  val icage: ICage[Animal] = new ICage[Cat]

  // hell no, opposite, cage of animal, should inherit from cage of cat.

  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](animal: T) // invariant

  // covariant positions

}
