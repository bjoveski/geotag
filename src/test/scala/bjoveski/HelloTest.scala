package bjoveski

import org.scalatest._

class HelloTest extends FlatSpec with Matchers {
  "Hello" should "have tests" in {
    true should be === true
  }
}