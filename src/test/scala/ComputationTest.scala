package scala

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.rishabh.hw1.Computation.SetExp.{Assign, Cross, Delete, Diff, GetMacro, Insert, Intersect, Scope, SetMacro, Union, Value, Variable}

import scala.collection.mutable
import scala.collection.mutable.Set

class ComputationTest extends AnyFlatSpec with Matchers{

  // Test 1
  it should "Check if sets are created" in {
    // Create 2 sets A and B in the default scope
    Assign("A", Value(Set(1,2,3))).eval()
    Assign("B", Value(Set(4,5,6))).eval()

    // Check if variables have been created in the default scope
    assert(Variable("A").eval() === scala.collection.mutable.HashSet(1, 2, 3))
    assert(Variable("B").eval() === scala.collection.mutable.HashSet(4, 5, 6))
  }

  // Test 2
  it should "Check the result of union and intersection operation" in {
    // Create a third set E  in default
    Assign("E", Value(Set(1,5,6))).eval()

    // Check for Union of 2 sets in default scope
    assert(Union(Variable("A"), Variable("B")).eval() === scala.collection.mutable.Set(1, 2, 3, 4, 5, 6))

    // Check for Intersection of 2 sets union with 3rd set in default scope
    assert(Union(Intersect(Variable("A"), Variable("B")), Variable("E")).eval() === scala.collection.mutable.Set(1, 5, 6))
  }

  // Test 3
  it should "Check the result of difference and intersection operation" in {
    // Create 2 sets C and D  in default scope
    Assign("D", Value(Set(1,5,6))).eval()
    Assign("C", Value(Set(7,8,9))).eval()

    // Check Symmetric Difference of set A and D in default scope
    assert(Diff(Variable("A"), Variable("D")).eval() === scala.collection.mutable.Set(2, 3, 5, 6))

    // Check Catesian Product of set B and C in default scope
    assert(Cross(Variable("B"), Variable("C")).eval() === scala.collection.mutable.Set((4,7), (4,8), (4,9), (5,7), (5,8), (5,9), (6,7), (6,8), (6,9)))
  }

  // Test 4
  it should "Check the result of insert and delete with and without macro" in {

    // Delete item from a set without using a macro in default scope
    assert(Delete(Variable("A"), Value(1)).eval() === scala.collection.mutable.Set(2, 3))

    // Insert an item into a set without using a macro in default scope
    assert(Insert(Variable("A"), Value(100)).eval() === scala.collection.mutable.Set(2, 3, 100))

    // Set Macros for insert and delete operations in default scope
    SetMacro("delete", Delete(Variable("B"), Value(5))).eval()
    SetMacro("insert", Insert(Variable("B"), Value(200))).eval()

    // Compute macros using the GetMacro function in default scope
    assert(GetMacro("delete").eval() === scala.collection.mutable.Set(4, 6))
    assert(GetMacro("insert").eval() === scala.collection.mutable.Set(4, 6, 200))
  }

  // Test 5
  it should "Check the result of the named and anonymous scope" in {

    // Create 2 sets A and B  in default scope
    Assign("A", Value(Set(1,2,3))).eval()
    Assign("B", Value(Set(4,5,6))).eval()

    // Create 2 sets A and B  in anonymous scope
    Scope(Assign("A", Value(Set(1)))).eval("")
    Scope(Assign("B", Value(Set(2)))).eval("")

    // Create 2 sets A and B  in a scope called "inner"
    Scope(Assign("A", Value(Set(1,2)))).eval("inner")
    Scope(Assign("B", Value(Set(3,4)))).eval("inner")

    // Check if Union of set A and B in default scope gives the correct output
    assert(Union(Variable("A"), Variable("B")).eval() === scala.collection.mutable.HashSet(1, 2, 3, 4, 5, 6))

    // Check if Union of set A and B in anonymous scope gives the correct output
    assert(Union(Variable("A"), Variable("B")).eval("") === scala.collection.mutable.HashSet(1, 2))

    // Check if Union of set A and B in "inner" scope gives the correct output
    assert(Union(Variable("A"), Variable("B")).eval("inner") === scala.collection.mutable.HashSet(1, 2, 3, 4))
  }
}
