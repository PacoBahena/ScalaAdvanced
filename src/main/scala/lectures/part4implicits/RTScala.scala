package lectures.part4implicits

import java.io.InputStream
import scala.io.Source
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object RTScala extends App {

  // sandboxing

  val l1 = List(1, 2, 3)
  val l2 = List(4, 5, 6)


  def filterCond(y: (Int, Int)): Boolean = {
    y._1 < 2 && y._2 < 5
  }

  val z0 = l1.flatMap(x => l2.map(y => (x, y)).filter { case (x, y) => x < 2 && y < 5 })
  val z1 = l1.flatMap(x => l2.map(y => (x, y)).filter(filterCond))
  val z2 = l1.flatMap(x => l2.map(y => (x, y)).filter((t: (Int, Int)) => t._1 < 2 && t._2 < 5))

  val z3 = l1.flatMap(x => l2.map(y => (x, y)).filter { case (x, y) => x + y < 35 })



  //  for {
  //    x <- l1
  //    y <- l2
  //    if y < 5 & x < 2
  //  } println((x,y))


  // pattern matchin

  def aStringInt(x: Int): String = {
    x match {
      case 1 => "one"
      case 2 => "two"
      case _ => "unrecognized "

    }
  }

  val aStringInt2 = (x: Int) => x match {
    case 1 => "one"
    case 2 => "two"
    case _ => "unrecognized "
  }

  println(2)
  println(aStringInt(3))
  val theval = aStringInt2(2)

  abstract class Color

  case class Red(code: String,tone:String) extends Color

  case class Yellow(code: String) extends Color


  val bobColor = Yellow("asdfasdf")


  // constructor matching
  def whichColor(col: Color): String = {
    col match {
      case Red(rgb, tone) => s"im red color $rgb, tone $tone"
      case Yellow(rgb) => s"im yellow code is $rgb"
      case _ => "im purple"
    }
  }

  println(whichColor(bobColor))


  // removing type safet y but you can also pull

  val myTypeis1: Any => String = (x:Any) => x match {
    case s: String => s" im a string $s"
    case y: Int => s"Im an int $y.toString"
    case _ => s"dont know what you are"
  }

  val myTypeis2: Any => String = {
    case s:String => s" im a string $s"
    case y: Int => s"Im an int $y.toString"
    case _ => s"dont know what you are"
  }

  println(myTypeis1("asdfsadf"))
  println(myTypeis2(2))

  println(myTypeis1.getClass)
  println(myTypeis2.getClass)


  // partial functions

  val squareRoot: PartialFunction[Double, Double] = new PartialFunction[Double,Double] {
    def apply(x: Double) = Math.sqrt(x)

    def isDefinedAt(x: Double) = x >= 0
  }

  val sqrt: PartialFunction[Double,Double] = {
    case x if x > 0 => x * x
  }

  val negToPos: PartialFunction[Double,Double] = {
    case x if x <0 => x * -1
  }


//  println(squareRoot(-10))
//  println(sqrt(16))

  val myT: Try[Double] = Try {
    Math.sqrt(16.0)
  }

  val c = myT.getOrElse(1.0)
  println(c)

  // this fails because of nan

  // better example coming up.

  def getMyRiskyInt(): Try[Int] = Try{
    val rand = new scala.util.Random
    val myval = rand.nextInt()
    if (myval % 2 == 0) throw new RuntimeException("not even hahaha")
    else myval
  }

  println(getMyRiskyInt.getOrElse(-1))

  val d = getMyRiskyInt orElse getMyRiskyInt getOrElse 2
  println(d)

  val buildingStatus: Try[Int] = Try {
    // Some dangerous code.
    val rand = new scala.util.Random
    val myval = rand.nextInt()
//    if (myval % 2 == 0)
    throw new RuntimeException("The building is on fire!!!")
    //else myval
    // Program crash. The fire departments are alerted. There is nothing for you to do.
  }

  buildingStatus match {
    case Success(myint) => println(s"The int is $myint")
    case Failure(e) => println(s"exception is ${e.toString}")
  }

  val buildingStatus2: Try[Int] = Try {
    // Some dangerous code.
    val rand = new scala.util.Random
    val myval = rand.nextInt()
    if (myval % 2 == 0) {
      throw new RuntimeException("The building is on fire!!!")
    } else {
      myval
    }
    // Program crash. The fire departments are alerted. There is nothing for you to do.
  }


  println(buildingStatus2.getOrElse(4))

  val buildingStatus3: Int => Int = (x:Int) =>  {
    val rand = new scala.util.Random
    val myval = rand.nextInt()
    (x + myval) / 0
  }

  println(Try(buildingStatus3(4)).getOrElse(4))


  // Options

  val o1: Option[String] = Some("paquito")
  val o2: Option[String] = None

  println(o2.getOrElse("default"))
  println(o2.isDefined)

  o1 match {
    case Some(a) => s"soy el belicon$a"
    case None => s"no soy nadie"
  }

  val o3 = o1.map(_ ++ "asdfsadf")

  // exercise

  trait Player {
    def name:String
    def getFavoriteTeam: Option[String]
  }

  trait Tournament {
    def getTopScore(team: String): Option[Int]
  }

  def getTopScore(player: Player, tournament:Tournament): Option[(Player,Int)] = {
    player.getFavoriteTeam
      .flatMap(team => tournament.getTopScore(team))
      .map(score => (player, score))
//    player.getFavoriteTeam
//    .flatMap(tournament.getTopScore)
//      .map((player,_))
  }

  // for comprehension with options

  val mires: Option[String] = for {
    p1 <- Some("param1")
    p2 <- None
  } yield p1 + "" + p2


  println(mires)

  val resFile = "hamlet.txt"

  val resource = Source.fromResource(resFile)

  val lines = resource.getLines.toList

//  lines.slice(0,5).foreach(println)

  //REGEX

  val numeric_group = "([0-9]+)".r
  val numeric_only: Regex = "[\\d]+".r
  val alphabetic = "([a-zA-Z]+)".r

  val numletters = "123123asdfsadf"
  val num = "1232454542"
  val letters = "asdfasgfgdghsfg"

 val tokens = Seq(numletters, num, letters)

  val f1 = numeric_group.findFirstIn(numletters).getOrElse("removeme")

  val f2 = numeric_only.findFirstIn(num).getOrElse("removeme")

  println(f1)
  println(f2)

  val aLine = "The little fox jumped "

  val aCorpus = """Mar. Good now, sit 1234 down, and ?  tell me he that knows,
                  |    Why this same strict and most observant watch
                  |    So nightly toils the subject of the land,
                  |    And why such daily cast of brazen cannon
                  |    And foreign mart for implements of war;
                  |    Why such impress of shipwrights, whose sore task
                  |    Does not divide the Sunday from the week.
                  |    What might be toward, that this sweaty haste
                  |    Doth make the night joint-labourer with the day?
                  |    Who is't that can inform me?""".stripMargin

  val myl = "The  little fox    jumped".split("""\s+""").toSeq
  println(myl)

//  val myLines: List[String] = aCorpus.split("\\n").toList
//  myLines.slice(0,3).foreach(println)

  val myLines:List[String] = Source.fromResource("hamlet.txt")
    .getLines().toList slice (0,10)
//  myLines.foreach(println)

  /// get empty space tokens and remove them
  /// all non empty space tokens shoud be lower cased.



  val toks = myLines.slice(0,10)
    .flatMap((line:String) => line.split("""\s+"""))
    .filter((token: String) =>  token.matches("""\d+|\w+.|[^\w\d\s]"""))

  val interest: Regex = """\d+|\w+|[^\w\d\s]""".r
  val toks2 = interest.findAllIn(aCorpus)
    .mkString("__--__").split("__--__").toList
    .map(_.toLowerCase)
  println(toks2)

  sealed trait Token

  case class Word(value:String) extends Token
  case class Symbol(value:String) extends Token
  case class Number(value:String) extends Token

  val wordP = """\w+"""
  val numberP = """\d+"""
  val punctP = """[^\w\d\s]"""

  val myTokens: List[Token] = toks2.map {
    case word if word.matches(wordP) => Word(word)
    case number if number.matches(numberP) => Number(number)
    case punc if punc.matches(punctP) => Symbol(punctP)
  }

  println(myTokens)
  println(myTokens(0))

//    .map(_.toLowerCase)

//  println(toks)
//  println(toks(8))

  // we want punctuation, numbers and words





//  rows


















}

