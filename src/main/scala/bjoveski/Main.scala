package bjoveski


/**
  * Created by bojan on 6/21/16.
  */
object Main extends App with Colors {

  require(args.length == 2, "Please provide two arguments, photoPath and historyPath")
  val photoPath = args(0)
  val historyPath = args(1)

  val images = Reader.readImages(photoPath)
  val history = Parser.parseGoogleHistoryFromPath(historyPath)

  images.zipWithIndex.foreach{ case (image, idx) =>
    if (idx % 10 == 0) {
      println(f"processed $idx%4d images")
    }

    Decorator.addLocationMetadata(image, history)
  }
  println(green("done!"))
  println("######")

}


trait Colors {

  private def helper(color: String, str: String) = s"$color$str${Console.RESET}"

  def yellow(str: String) = helper(Console.YELLOW, str)
  def green(str: String) = helper(Console.GREEN, str)
  def red(str: String) = helper(Console.RED, str)
}