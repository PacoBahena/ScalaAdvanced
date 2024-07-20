package lectures.part4implicits



object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1,4,5,3,2).sorted)

  /*
  * implicits (USED AS IMPLICIT PARAMETERS ):
  *  - val/var
  *  - object
  *  - accessor methods = defs with no parentheses, repeat, NO PARENTHESES
  * */

  case class Person(name:String, age:Int)

  val persons = List(
    Person("Steve",30),
    Person("Amy",22),
    Person("John",66)
  )

//  implicit val personOrderAge: Ordering[Person] = Ordering.fromLessThan((p1: Person,p2: Person) => p1.age < p2.age)
//
//  println(persons.sorted)

  object SomeObject {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.name.compareTo(p2.name) < 0)
  }

  object Person {

    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.name.compareTo(p2.name) < 0)

  }


//  println(persons.sorted(alphabeticOrdering.reverse))
//  println(persons.sorted(Ordering[Person].reverse))

//  implicit val orderingPersons: Ordering[Person] = Ordering.fromLessThan((p1,p2) => p1.age < p2.age)
//  println(persons.sorted)



  /*
  * Implicit scope
  * - normal scope is higher priority, where we write our code. LOCAL SCOPE
  * - imported scope
  * - companions of all types involved in the method signature.
  *     First List
  *     Second Ordering
  *     Third All types involved A or any supertype.
  *
  * */

  // def sorted[B >: A](implicit ord: Ordering[B]): C

  // best practices

   // 1) single possible value, define in companion

  // 2) multiple implicit values,but there is a good one,
  // good one goes in the companion


  object AlphabeticNameOrdering {

    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.name.compareTo(p2.name) < 0)

  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a,b) => a.age < b.age)
  }

  //import AlphabeticNameOrdering._
  import AgeOrdering.ageOrdering

  println(persons.sorted)

  case class Purchase(nunits: Int, unitPrice:Double)

  /*
  * 1. order by total price 50%
  * 2. order by count 25%
  * 3. order by price 25%
  * */

  object Purchase {
    implicit val orderingByPrice: Ordering[Purchase] = Ordering.fromLessThan((p1,p2) => (p1.nunits * p1.unitPrice) < (p2.nunits * p2.unitPrice))
  }

  object OrderingNunits {
    implicit val orderingByCount: Ordering[Purchase] = Ordering.fromLessThan((p1,p2) => p1.nunits < p2.nunits)
  }

  object OrderingUPrice {
    implicit val orderingByNPrice: Ordering[Purchase] = Ordering.fromLessThan((p1,p2) => p1.unitPrice < p2.unitPrice)

  }

//  import OrderingUPrice._

  println(List(Purchase(2,10),Purchase(1,5)).sorted)









}
