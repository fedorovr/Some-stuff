import scala.concurrent.stm._

class TMMap[K, V] extends scala.collection.mutable.Map[K, V] {
  private val ARRAY_SIZE = 1024
  private val array: TArray[Node] = TArray.ofDim(ARRAY_SIZE)

  override def put(key: K, value: V) : Option[V] = {
    val hashCode = Math.abs(key.hashCode() % ARRAY_SIZE)
    atomic { implicit txn =>
      val newNode = new Node(key, value, array.apply(hashCode))
      array.update(hashCode, newNode)
    }
    Option.apply(value)
  }

  override def remove(key: K): Option[V] = {
    val hashCode = Math.abs(key.hashCode() % ARRAY_SIZE)
    atomic { implicit txn =>
      var previousNode: Node = null
      var currentNode = array.apply(hashCode)
      while (currentNode != null) {
        if (currentNode.key.equals(key)) {
          if (previousNode != null) {
            previousNode.next() = currentNode.next()
          } else {
            array.update(hashCode, currentNode.next())
          }
          return Option.apply(currentNode.value)
        }
        previousNode = currentNode
        currentNode = currentNode.next.get
      }
    }
    Option.empty
  }

  override def get(key: K): Option[V] = {
    val hashCode = Math.abs(key.hashCode() % ARRAY_SIZE)
    atomic { implicit txn =>
      var currentNode = array.apply(hashCode)
      while (currentNode != null) {
        if (currentNode.key.equals(key)) {
          return Option.apply(currentNode.value)
        }
        currentNode = currentNode.next.get
      }
    }
    Option.empty
  }

  override def +=(kv: (K, V)): TMMap.this.type = ???

  override def -=(key: K): TMMap.this.type = ???

  override def iterator: Iterator[(K, V)] = ???

  class Node(var key: K, var value: V, next0: Node) {
    val next = Ref(next0)
    val hasNext = next == null
  }
}