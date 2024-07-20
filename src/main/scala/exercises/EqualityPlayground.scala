package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer(a, b)
  }

  // use it for users

  object isEqualName extends Equal[User] {
    override def apply(u1: User, u2: User): Boolean = u1.name == u2.name
  }

  implicit object isEqualUser extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name) && (a.age == b.age)
  }

  val u1 = User("John", 32, "john@rockthejvm.com")
  val u2 = User("John", 32, "john@rockthejvm.com")
  val u3 = User("Johny", 33, "john@rockthejvm.com")

  println(isEqualUser(u1, u2))
  println(isEqualName(u1, u3))

  println(Equal(u1, u2))



}
