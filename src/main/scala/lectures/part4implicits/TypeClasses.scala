package lectures.part4implicits

import exercises.EqualityPlayground.Equal

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name:String,age:Int,email:String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  val john = User("John",32,"john@rockthejvm.com")
  User("John",32,"john@rockthejvm.com").toHtml

  /* option 1
  * 1. this works for the types we write
  * 2 - ONE IMPLEMENTATION out of quite a number
  * */

  // option 2 pm

  /*
  Loss type safety
  need to  modify code every time
  still one implementation
  * */

//  object HTMLSerializer {
//    def serializerToHtml(value: Any): Unit = value match {
//      case User(n,a,e) => println("")
//      case _ => println("asdf")
//    }
//  }

  //3 option

  //TYPE CLASS
  trait HTMLSerializer[T] {
    def serialize3(u1: User): Int

    def serialize(value: T): String
    def serialize2(value: T): Int
  }

  //TYPE CLASS INSTANCE
  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"

    override def serialize2(value: User): Int = value.age
    override def serialize3(value: User): Int = 3000
  }

//  we can define serializers for other types, we did not write.

  import java.util.Date
  implicit object DateSerializer extends HTMLSerializer[java.util.Date] {
    override def serialize(date: Date): String = date.toString
    override def serialize2(value: Date): Int = 100

    override def serialize3(u1: User): Int = 3000
  }

  // we can define multiple serializers

  //TYPE CLASS INSTANCE
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user:User): String = s"${user.name}"
    override def serialize2(value: User): Int = value.age + 1000
    override def serialize3(u1: User): Int = 3000

  }

  val u1 = User("John", 32, "john@rockthejvm.com")
  val u2 = User("John", 32, "john@rockthejvm.com")
  val u3 = User("Johny", 33, "john@rockthejvm.com")

  /*
  * Exercise implement Equal type class see Equality Playground on sexercises
  * */

  // part 2

  object HTMLSerializer {

    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)
    def serialize2[T](value: T)(implicit serializer: HTMLSerializer[T]): Int =
      serializer.serialize2(value)
    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  val a = HTMLSerializer.serialize(u1)
  val b = HTMLSerializer.serialize(new java.util.Date())

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value:Int): String = value.toString
    override def serialize2(value: Int): Int = value

    override def serialize3(u1: User): Int = 3000
  }

  println(IntSerializer.serialize(5))
  println(HTMLSerializer.serialize(5))


  println(HTMLSerializer.serialize2(u1))
  println(HTMLSerializer.serialize(u1))
  println(HTMLSerializer[User].serialize3(u1)) // note that this is not defined in the type class companion, but rather gets interface

  //access to entire type class interface
  println(HTMLSerializer[User].serialize3(u1))

   /*
   exercise, how would you implicitly convert user so you could
   do  john.serialize
   CONVERSION
   */

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(u1.toHTML)
  println(5.toHTML)



  /*
  * Exercise implement type class pattern for equality
  *   improve the Equal TC with an implicit conversion class
  *   ===(another value: T)
  *   !==(another value: T)
  *
  * */

  implicit class EqualEnrich[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, anotherValue)
  }

  println(u1 === u2)
  println(u1 !== u2)

  /*
  * First it rewrites the expr as
  * u1.===(u2)
  * it cannot find === method on user, than it tries to find
  * wrapped that does have ===, which is
  * new EqualEnrich[User](u1).===(u2)(isEqualUser: Equal[User]) = isEqualUser(u1,u2)
  * the implicit equalizer of type User is isEqualUser, it extends Equal[User]
  * */

  // note this iadds type safety

  //TYPE SAFE (u1 === 3) // TYPESAFE
  // NOT TYPE SAFE
  println(u1 == 3)


  def htmlBoilerPlate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"
  // context bounds
  def htmlSugar[T : HTMLSerializer](content: T): String =
    s"<html><body>${content.toHTML}</body></html>"

  // downside is you dont get to manipulate the serializer by name with the context-bounds

  // there is a third way though, gives you both less verbose and the retireves the implicit by name

  // implicitly

  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]

  // then we can say

  def htmlSugar2[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    s"<html><body>${content.toHTML}</body></html>"
  }


}
