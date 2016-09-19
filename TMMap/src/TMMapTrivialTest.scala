class TMMapTrivialTest {
  val map = new TMMap[String, String]

  map.put("One", "1")
  map.put("Two", "2")
  map.put("Three", "3")
  print(map)

  def main(args: Array[String]): Unit = {
    println("Hello, world!")
  }
}
