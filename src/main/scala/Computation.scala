package scala

import scala.collection.mutable.Set
import scala.collection.mutable.Map

object Computation:

  import SetExp.*

  // Aliasing 'Any' to write more concise code
  type BasicType = Any

  // Map to store the macros
  val macroSetting: scala.collection.mutable.Map[BasicType, SetExp] = Map()

  // Map to store named scopes
  val namedScopeSetting: scala.collection.mutable.Map[BasicType, BasicType] = Map()

  // Map to store variables
  val variableSetting: scala.collection.mutable.Map[BasicType, BasicType] = Map()

  enum SetExp:
    case Value(input: BasicType)                      // Get the value of the element passed
    case Variable(name: String)                       // Fetch a variable value from variableSetting Map
    case Check(list: SetExp, item: SetExp)            // Check if item present in set
    case Create(name: String, item: SetExp)           // Create a variable and assign value to it
    case Insert(set: SetExp, item: SetExp)            // Insert an item into the set
    case Delete(set: SetExp, item: SetExp)            // Delete an item from the set
    case Union(set1: SetExp, set2: SetExp)            // Union of 2 sets
    case Intersect(set1: SetExp, set2: SetExp)        // Intersection of 2 sets
    case Diff(set1: SetExp, set2: SetExp)             // Difference of 2 sets
    case Cross(set1: SetExp, set2: SetExp)            // Cartesian Product of 2 sets
    case SetMacro(macroName: String, op: SetExp)      // Create a macro in macroSetting Map
    case GetMacro(macroName: String)                  // Fetch a macro from macroSetting Map
    case SetNamedScope(scope: String, op: SetExp)     // Set Named Scope in namedScopeSetting Map
    case GetNamedScope(scopeName: String)             // Fetch Named Scope from namedScopeSetting Map
    case SetAnonScope(scopeName: SetExp)              // Set Anonymous Scope

    // enums evaluated using eval function and the type of enum identified using 'match' statement
    def eval: BasicType =
      this match {

        // Get the value of the element passed
        case Value(i) => i

        // Fetch a variable value from variableSetting Map
        case Variable(name) => variableSetting(name)

        case Check(list, item) => {
          // Convert SetExp element to Set[BasicType] element
          val l: Set[BasicType] = list.eval.asInstanceOf[Set[BasicType]]

          // Check if set contains item
          l.contains(item.eval)
        }

        case Create(name, item) => {
          // Add the variable name and its value to the variableSetting map
          variableSetting += (name -> item.eval)
        }

        case Insert(list, item) => {
          // Upserting the variable with the new value
          variableSetting.update(list.eval, list.eval.asInstanceOf[Set[BasicType]] += item.eval)

          // Return the new value added
          variableSetting(list.eval)
        }

        case Delete(list, item) => {
          // Convert SetExp element to Set[BasicType] element
          val l: Set[BasicType] = list.eval.asInstanceOf[Set[BasicType]]

          // Remove the item from the set
          l -= item.eval

          // Update the set in the Map
          variableSetting.update(list.eval, l)

          // Return the updated set
          variableSetting(list.eval)
        }

        case Union(set1, set2) => {
          // Convert SetExp element to Set[BasicType] element
          val l1: Set[BasicType] = set1.eval.asInstanceOf[Set[BasicType]]
          val l2: Set[BasicType] = set2.eval.asInstanceOf[Set[BasicType]]

          // Perform union of 2 sets and return the result
          val l: Set[BasicType] = l1.union(l2)
          l
        }

        case Intersect(set1, set2) => {
          // Convert SetExp element to Set[BasicType] element
          val l1: Set[BasicType] = set1.eval.asInstanceOf[Set[BasicType]]
          val l2: Set[BasicType] = set2.eval.asInstanceOf[Set[BasicType]]

          // Perform intersection of 2 sets and return the result
          val l: Set[BasicType] = l1.intersect(l2)
          l
        }

        case Diff(set1, set2) => {
          // Convert SetExp element to Set[BasicType] element
          val l1: Set[BasicType] = set1.eval.asInstanceOf[Set[BasicType]]
          val l2: Set[BasicType] = set2.eval.asInstanceOf[Set[BasicType]]

          // Perform union of 2 sets
          val list_concat = l1.union(l2)

          // Perform intersection of 2 sets
          val list_intersect = l1.intersect(l2)

          // Filter the items from list_concat which are present in list_intersect and resturn the result
          list_concat filterNot list_intersect.contains
        }

        case Cross(set1, set2) => {
          // Convert SetExp element to Set[BasicType] element
          val l1: Set[BasicType] = set1.eval.asInstanceOf[Set[BasicType]]
          val l2: Set[BasicType] = set2.eval.asInstanceOf[Set[BasicType]]

          // Converting Set of Sets into Set of tuples: Set(Set(1,2), Set(1,3)) => Set((1,2), (1,3))
          l1.flatMap(a => l2.map(b => (a, b)))
        }

        case SetMacro(str, op) => {
          // Adding the macro to macroSetting Map
          macroSetting += (str -> op)
        }

        case GetMacro(scopeName) => {
          // Fetching the macro from macroSetting Map using its name and then evaluating the macro and return the result
          macroSetting(scopeName).eval
        }


        case SetNamedScope(str, op) => {
          // Adding the named scope to namedScopeSetting Map
          namedScopeSetting += (str -> op.eval)
        }

        case GetNamedScope(name) => {
          // Fetching the scope from namedScopeSetting Map using its name and then evaluating the scope and return the result
          namedScopeSetting(name)
        }

        case SetAnonScope(str) => {
          // Evaluate the anonymous scope and return the result
          str.eval
        }
      }

  @main def runArithExp: Unit =
    import SetExp.*




