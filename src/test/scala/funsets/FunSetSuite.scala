package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.8/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s1000 = singletonSet(1000)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton s1 contains 1")
    }
  }

  test("singletonSet(1) does not contain 2") {
    new TestSets {
      assert(!contains(s1, 2), "Singleton s1 does not contain 2")
    }
  }

  test("singletonSet(3) contains 3") {
    new TestSets {
      assert(contains(s3, 3), "Singleton s3 contains 3")
    }
  }

  test("singletonSet(3) does not contain 2") {
    new TestSets {
      assert(!contains(s3, 2), "Singleton s3 does not contain 2")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains the elements on both sets") {
    new TestSets {
      val s = intersect(s1, s2)
      assert(!contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("intersect contains the element on any set and the union of that set with any other set") {
    new TestSets {
      val s = intersect(s1, union(s1,s2))
      assert(contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("diff contains the elements on set s1 but not on s2") {
    new TestSets {
      val s = diff(s1, s2)
      assert(contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("diff is empty for a set and a union of that set with any other set") {
    new TestSets {
      val s = diff(s1, union(s1,s2))
      assert(!contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("filter from set with element 1 the elements that are not greater than one") {
    new TestSets {
      val s = filter(s1, x => x > 1)
      assert(!contains(s, 1), "Filter 1")
      assert(!contains(s, 2), "Filter 2")
      assert(!contains(s, 3), "Filter 3")
    }
  }

  test("filter from set with element 3 the elements that are not greater than one") {
    new TestSets {
      val s = filter(s3, x => x > 1)
      assert(!contains(s, 1), "Filter 1")
      assert(!contains(s, 2), "Filter 2")
      assert(contains(s, 3), "Filter 3")
    }
  }

  test("forall positive numbers") {
    new TestSets {
      val s = union(s1, union(s2,s3))
      assert(forall(s, x => x > 0), "forall positive")
    }
  }

  test("forall not all numbers bigger than 1") {
    new TestSets {
      val s = union(s2, union(s2,s1))
      assert(!forall(s, x => x > 1), "forall greater than 1")
    }
  }

  test("forall upper limit") {
    new TestSets {
      assert(forall(s1000, x => x > 1), "forall empty limit")
    }
  }

  test("forall for empty set") {
    new TestSets {
      assert(forall(intersect(s1,s2), x => x > 1), "forall empty set")
    }
  }

  test("exists for empty set") {
    new TestSets {
      assert(!exists(intersect(s1,s2), x => x > 1), "exists empty set")
    }
  }

  test("exists at least a number greater than 1") {
    new TestSets {
      assert(exists(union(s1,s2), x => x > 1), "exists greater 1")
    }
  }

  test("map multiplying by 3") {
    new TestSets {
      assert(contains(map(s3, x => x * 3), 9), "map multiplying by 3 set s3")
    }
  }

  test("map substracting 2 to set with several elements") {
    new TestSets {
      assert(contains(map(union(s1, union(s2,s3)), x => x - 2),-1), "map substracting 2 to s1")
      assert(contains(map(union(s1, union(s2,s3)), x => x - 2),0), "map substracting 2 to s2")
      assert(contains(map(union(s1, union(s2,s3)), x => x - 2),1), "map substracting 2 to s3")
    }
  }

}