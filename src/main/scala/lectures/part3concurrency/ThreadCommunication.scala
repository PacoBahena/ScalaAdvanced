package lectures.part3concurrency

object ThreadCommunication extends App{

  /*
  the producer consumer problem

  producer -> [ ? ] -> consumer

  * */

  class SimpleContainer {
    private var value:Int = 0
    def isEmpty: Boolean = value == 0

    // set
    def set(newValue: Int): Unit = {
      value = newValue
    }

    //consuming
    def get: Int = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting for value to be set....")
      while( container.isEmpty) {
        println("[consumer] actively waiting....")
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread( () => {
      println("[producer] computing....")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] i have produced, after long work, the value $value")
      container.set(value)

    } )

    consumer.start()
    producer.start()
  }

//  naiveProdCons()


  //waiiting is waiting. best to wait and notifdy.

  // lock with synchronized expressions. Only anyref have synchronized blocks
  // make no asumption who gets locks first, keep locks to a minimum.
  // mantain thread safety.


  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread( () => {
      println("[consumer] waiting for a value to be set ")
      container.synchronized{
        container.wait()
      }

      println(s"[consumer] i have consumed ${container.get.toString}")
    } )

    val producer = new Thread( () => {

      val theval = 42
      println(s"[producer] hard at work setting val $theval")
      container.synchronized{
        container.set(theval)
        println("[producer ] i have set the value ")
        container.notify()  // releases only one thread waiting, dont know which. can do notify all releases all
      }

    } )

    consumer.start()
    producer.start()
  }

  smartProducerConsumer()

}
