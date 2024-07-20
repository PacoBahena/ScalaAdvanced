package lectures.part2

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  // curried methods
  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  // BREAKS val add4 = curriedAdder(4)
  val add4: Int => Int = curriedAdder(4)

  // notice without type definition, it fails add4, only for methods
  // when you add the type definition, you get lifting

  // lifting means it gets turned to a function  ETA expansion, CREATES FUNCTIONS OUT OF METHODS

  def inc(x:Int): Int = x + 1
  List(1,2,3).map(inc)  // ETA expansion (x => inc(x)

  // why its important? partial function applications

  val add5 = curriedAdder(5) _  // the _ tells the compiler to do Int => Int

  /*
  * Exercise
  *
  *
  *
  *
  * */

  val simpleAddFunction: (Int, Int) => Int = (x: Int, y:Int) => x + y
  def simpleAddMethod(x:Int,y:Int): Int = x + y
  def curriedAddMethod(x: Int)(y:Int): Int = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above

  val add7 = (x:Int) => simpleAddFunction(7,x)
  val add7_1 = simpleAddFunction.curried(7)
  println(add7_1(3))
  val add7_6 = simpleAddFunction(7,_:Int)
  val add7_2 = simpleAddMethod(7,_)
  println(add7_2(3))
  val add7_3 = curriedAddMethod(7) _
  val add7_4 = curriedAddMethod(7)(_)

  val add7_5 = simpleAddMethod(7, _: Int) // compiler reqriutes to y => SimpleAddMethod(7,y)

  // underscores are powerful

  def concatenator(a:String,b:String, c: String) = a+b+c

  val insertName = concatenator("hello im",_: String,"how are you")
//  x: String => concatenator("hello",x,"how are you")
  println(insertName("Paco"))

  val insertNameMessage = concatenator("Hello ",_: String, _: String)

  println(insertNameMessage("paco","scala hurts but its great"))

  /*
  * Write up a formatter. %4.2f, %8.6f %14.12f with a curried function
  * */

  println("%8.6f".format(3.14161718293))

  def formatNumMeth(myNum: Double)(formatter: String): String = formatter.format(myNum)
  val formatNumFun = (myNum: Double,formatter: String) => formatter.format(myNum)

  val formatter_1 = formatNumMeth(Math.PI) _

  println(formatter_1("%4.2f"))
  println(formatter_1("%8.6f"))

  val formatter_2 = formatNumFun(Math.PI,_: String)
  println(formatter_2("%4.2f"))
  println(formatter_2("%8.6f"))

}
