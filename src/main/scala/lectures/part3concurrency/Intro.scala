package lectures.part3concurrency

import java.lang.Thread.sleep
import java.util.concurrent.Executors


object Intro extends App {
  /*
  * Runnable is an interfae, with a void method called run.
  * */
  // JVM Threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  // notice the single abstract meethod can use syntax sugar.

//  aThread.start() // gives the signal to start.
  // differentiate the thread instance from the JVM thread
  // creates a JVM thread => OS thread

  //common pitfal

  val run2: Runnable = new Runnable {
    override def run(): Unit = {
      sleep(5000)
      println("im thread2. ")
    }
  }

//  run2.run() // This does not do anything on threads/parallel.

  val thread2 = new Thread(run2)
  // thread2.start() // this does occur in parallel.

  //thread2.join() // blockjs until thread2 finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodBye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))

//  threadHello.start()
//  threadGoodBye.start()

  // different runs produce different results !

  // thread scheduling respond to os factors.

  // executors
//  val pool = Executors.newFixedThreadPool(10)
//  pool.execute(() => println("something in the threadpool"))

//  pool.execute(() => {
//    Thread.sleep(1000)
//    println("done after 1 second")
//
//  })

//  pool.execute(() => {
//    Thread.sleep(1000)
//    println("almost done")
//    Thread.sleep(2000)
//    println("done after 3 seconds")
//  })

//  pool.shutdown()  // stops accepting threads
//  pool.execute(() => println("This should not appear")) // calls exception in the main thread.

//    pool.shutdownNow() // stops running threads.
//
//    println(pool.isShutdown)


  def runInParallel(): Unit = {
    var x = 0
    var thread1 = new Thread( () => {
      x = 1
    } )
    var thread2 = new Thread ( () => {
      x = 2
    } )

    thread1.start()
    thread2.start()
    println(s"x is: $x")

  }



//  for (_ <- 1 to 100) runInParallel
  //RACE CONDITION

  class BankAccount(var amount: Int) {
    override def toString:String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
//    println(s"I've bought $thing")
//    println(s"My account is now ${account.amount}")
  }

  def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
    account.synchronized {
      account.amount -= price
      println(s"I've bought a $thing")
      println(s"My account is now ${account.amount}")
    }
  }



//  for ( _ <- 1 to 1000000) {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread( () => buy(account, thing = "shoes", price=3000 ))
//    val thread2 = new Thread( () => buy(account,"iphone10",4000))
//
//
//    thread2.start()
//    thread1.start()
//    Thread.sleep(10)
//    if (account.amount != 43000) println("AHA " + account.amount)
//  }

  // fix with synchronized

  // option #2: use @volatile, but not the best option , usa to single op. += is not single, reads, substract assign

  /*
  1) construct 50 inception threads
  Thread 1 -> Thread2 ......
  print hello from trhead #3
  in Reverse order
  * */

  def buildThreads(n: Int): Unit = {
    val t = new Thread( () => {
      println(s"hello from thread $n")
    } )
    t.start()
    t.join()
    if (n != 0) buildThreads(n-1)
  }

//  buildThreads(50)

  /*
  Exercise 2


  * */

  var x = 0
  val threads: Seq[Thread] = (1 to 100).map(_ => new Thread(() => x.synchronized(x += 1)  ))
  threads.foreach(_.start())
  println(s"x is :$x")
  // Biggest value possible for x ?  easy, 100
  // Smallest possible value for x.  1.

  /*
  * exercise 3 Sleep fallacy
  * */

  var message = ""
  val awesomeThread = new Thread( () => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  } )

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)

  /*
  * what's the value of message ?
  * is it guaranteed ?
  * why ? why not ?
  * */



}
