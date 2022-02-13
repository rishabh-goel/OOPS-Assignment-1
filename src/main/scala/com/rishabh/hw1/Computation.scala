package com.rishabh.hw1

object Computation:

  import SetExp.*

  // Aliasing 'Any' to void hardcoding of Variable types
  type BasicType = Any

  // Map to store the macros
  val macroSetting: scala.collection.mutable.Map[BasicType, SetExp] = scala.collection.mutable.Map()

  // Map to store variables
  val variableSetting: scala.collection.mutable.Map[BasicType, BasicType] = scala.collection.mutable.Map()

  enum SetExp:
    case Value(input: BasicType) // Get the value of the element passed
    case Variable(name: String) // Fetch the value assigned to the variable
    case Check(list: SetExp, item: SetExp) // Check if item present in set
    case Assign(name: String, item: SetExp) // Assign value to a variable
    case Insert(set: SetExp, item: SetExp) // Insert an item into the set
    case Delete(set: SetExp, item: SetExp) // Delete an item from the set
    case Union(set1: SetExp, set2: SetExp) // Union of 2 sets
    case Intersect(set1: SetExp, set2: SetExp) // Intersection of 2 sets
    case Diff(set1: SetExp, set2: SetExp) // Difference of 2 sets
    case Cross(set1: SetExp, set2: SetExp) // Cartesian Product of 2 sets
    case SetMacro(macroName: String, op: SetExp) // Create a macro in macroSetting Map
    case GetMacro(macroName: String) // Fetch a macro from macroSetting Map
    case Scope(op: SetExp) // Set Scope of variables

    // enums evaluated using eval function and the type of enum identified using 'match' statement.
    // Default value for scope is "default"
    def eval(scope: String = "default"): BasicType =
      this match {

        // Get the value of the element passed
        case Value(i) => i

        // Fetch a variable value from variableSetting Map
        case Variable(name) =>

          // Check the scope of the variable
          if (scope.equals("default")) {
            // Check if variable exists. If not, throw an error
            if (!variableSetting.contains(name))
              throw new Exception("Variable " + name + " not found")

            // Return the variable created
            variableSetting(name)
          }
          else {

            // Save the variable with the name of its scope
            val temp = scope + "_" + name

            // Check if variable exists. If not, throw an error
            if (!variableSetting.contains(temp))
              throw new Exception("Variable " + temp + " not found")

            // Return the variable created
            variableSetting(temp)
          }

        case Check(set, item) =>
          // Convert SetExp element to Set[BasicType] element in the appropriate scope
          val l: scala.collection.mutable.Set[BasicType] = set.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]

          // Check if set contains item
          l.contains(item.eval(scope))


        case Assign(name, item) =>
          // Check the scope of the variable
          if (scope.equals("default")) {
            // Add the variable name and its value to the variableSetting map
            variableSetting += (name -> item.eval(scope))

            // Return the value associated with name
            variableSetting(name)
          }
          else {
            // Save the variable with the name of its scope
            val temp = scope + "_" + name

            // Add the variable name and its value to the variableSetting map
            variableSetting += (temp -> item.eval(scope))

            // Return the value associated with name
            variableSetting(temp)
          }

        case Insert(list, item) =>
          // Update the set in the Map by inserting the value in the appropriate scope
          variableSetting.update(list.eval(scope), list.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]] += item.eval(scope))

          // Return the new value added
          variableSetting(list.eval(scope))

        case Delete(list, item) =>

          // Update the set in the Map by removing the value in the appropriate scope
          variableSetting.update(list.eval(scope), list.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]] -= item.eval(scope))

          // Return the updated set
          variableSetting(list.eval(scope))

        case Union(set1, set2) =>
          // Convert SetExp element to Set[BasicType] element using the appropriate scope
          val l1: scala.collection.mutable.Set[BasicType] = set1.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]
          val l2: scala.collection.mutable.Set[BasicType] = set2.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]

          // Perform union of 2 sets and return the result
          val result = l1.union(l2)
          result


        case Intersect(set1, set2) =>
          // Convert SetExp element to Set[BasicType] element using the appropriate scope
          val l1: scala.collection.mutable.Set[BasicType] = set1.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]
          val l2: scala.collection.mutable.Set[BasicType] = set2.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]

          // Perform intersection of 2 sets and return the result
          val result = l1.intersect(l2)
          result


        case Diff(set1, set2) =>
          // Convert SetExp element to Set[BasicType] element using the appropriate scope
          val l1: scala.collection.mutable.Set[BasicType] = set1.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]
          val l2: scala.collection.mutable.Set[BasicType] = set2.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]

          // Perform union of 2 sets
          val list_concat = l1.union(l2)

          // Perform intersection of 2 sets
          val list_intersect = l1.intersect(l2)

          // Filter the items from list_concat which are present in list_intersect and return the result
          val result = list_concat.diff(list_intersect)
          result


        case Cross(set1, set2) =>
          // Convert SetExp element to Set[BasicType] element
          val l1: scala.collection.mutable.Set[BasicType] = set1.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]
          val l2: scala.collection.mutable.Set[BasicType] = set2.eval(scope).asInstanceOf[scala.collection.mutable.Set[BasicType]]

          // Converting Set of Sets into Set of tuples: Set(Set(1,2), Set(1,3)) => Set((1,2), (1,3))
          val result = l1.flatMap(a => l2.map(b => (a, b)))
          result

        case SetMacro(str, op) =>
          // Adding the macro to macroSetting Map
          macroSetting += (str -> op)


        case GetMacro(scopeName) =>
          // Fetching the macro from macroSetting Map using its name and scope and then evaluating the macro and return the result
          macroSetting(scopeName).eval(scope)

        case Scope(op) =>
          // Executing the operation in the defined scope
          op.eval(scope)
      }

  @main def runArithExp: Unit =
    import SetExp.*
