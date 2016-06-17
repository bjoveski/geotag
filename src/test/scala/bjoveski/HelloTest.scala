package bjoveski

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class HelloTest extends FlatSpec with ShouldMatchers {
  "Hello" should "have tests" in {
    true should be === true
  }
}