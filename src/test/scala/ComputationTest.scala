import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.Computation.SetExp.{Create, Cross, Delete, Diff, GetMacro, GetNamedScope, Insert, Intersect, SetAnonScope, SetMacro, SetNamedScope, Union, Value, Variable}
import scala.collection.mutable
import scala.collection.mutable.Set

class ComputationTest extends AnyFlatSpec with Matchers{

  // Test 1
  it should "Check if sets are created" in {
    // Create Set A and check the result
    assert(Create("A", Value(Set(1,2,3))).eval === mutable.HashMap("A" -> Set(1, 2, 3)))

    // Create Set B and now the map should have both Set A and Set B
    assert(Create("B", Value(Set(4,5,6))).eval === mutable.HashMap("A" -> Set(1, 2, 3), "B" -> Set(4, 5, 6)))
  }

  // Test 2
  it should "Check the result of union and intersection operation" in {
    // Create a third set E
    Create("E", Value(Set(1,5,6))).eval

    // Check for Union of 2 sets
    assert(Union(Variable("A"), Variable("B")).eval === Set(1, 2, 3, 4, 5, 6))

    // Check for Intersection of 2 sets union with 3rd set
    assert(Union(Intersect(Variable("A"), Variable("B")), Variable("E")).eval === Set(1, 5, 6))
  }

  // Test 3
  it should "Check the result of difference and intersection operation" in {
    // Create 2 sets C and D
    Create("D", Value(Set(1,5,6))).eval
    Create("C", Value(Set(7,8,9))).eval

    // Check Symmetric Difference of set A and D
    assert(Diff(Variable("A"), Variable("D")).eval === Set(2, 3, 5, 6))

    // Check Catesian Product of set B and C
    assert(Cross(Variable("B"), Variable("C")).eval === Set((4,7), (4,8), (4,9), (5,7), (5,8), (5,9), (6,7), (6,8), (6,9)))
  }

  // Test 4
  it should "Check the result of insert and delete with and without macro" in {

    // Delete item from a set without using a macro
    assert(Delete(Variable("A"), Value(1)).eval === Set(2, 3))

    // Insert an item into a set without using a macro
    assert(Insert(Variable("A"), Value(100)).eval === Set(2, 3, 100))

    // Set Macros for insert and delete operations
    SetMacro("delete", Delete(Variable("B"), Value(5))).eval
    SetMacro("insert", Insert(Variable("B"), Value(200))).eval

    // Compute macros using the GetMacro function
    assert(GetMacro("delete").eval === Set(4, 6))
    assert(GetMacro("insert").eval === Set(4, 6, 200))
  }

  // Test 5
  it should "Check the result of the named and anonymous scope" in {

    // Set inner named scope
    SetNamedScope("inner", Union(Value(Set(1)), Value(Set(2)))).eval

    // Use inner named scope inside outer named scope
    SetNamedScope("outer", Union(GetNamedScope("inner"), Value(Set(3)))).eval

    // Compute the result of both the scopes
    assert(GetNamedScope("outer").eval === mutable.HashSet(1, 2, 3))
    assert(GetNamedScope("inner").eval === mutable.HashSet(1, 2))

    // Set an anonymous scope inside a named scope
    SetNamedScope("scope1", Union(SetAnonScope(Union(Value(Set(5)), Value(Set(10)))), Value(Set(0)))).eval
    // Compute the result of the named scope
    assert(GetNamedScope("scope1").eval === mutable.HashSet(0, 10, 5))
  }
}
