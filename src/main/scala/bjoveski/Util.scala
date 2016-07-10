package bjoveski

/**
  * Created by bojan on 7/10/16.
  */
trait Util {

  def runAndTime[T](fn: => T): (T, Long) = {
    val start = System.currentTimeMillis()
    val res = fn
    val msTaken = System.currentTimeMillis() - start

    (res, msTaken)
  }

}


trait Colors {

  private def helper(color: String, str: String) = s"$color$str${Console.RESET}"

  def yellow(str: String) = helper(Console.YELLOW, str)
  def green(str: String) = helper(Console.GREEN, str)
  def red(str: String) = helper(Console.RED, str)
}