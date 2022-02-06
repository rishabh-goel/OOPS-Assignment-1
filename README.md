# CS 474 : HW1 by Rishabh Goel

**SetEx** is a DSL(Domain Specific Language) developed for people who study Set Theory. This language will help people to run various set operations. 

Operations included:


| OPERATION | DESCRIPTION |
| :-------------: |:-------------:|
| `Value(2)`      | Get the value of the item passed |
| `Variable("A")`      | Get the value of a variable |
| `Create("A", Value(Set(1,2,3)))` | Create a set |
| `Insert(Variable("A"), Value(4))` | Insert in a set |
| `Delete(Variable("A"), Value(2))` | Delete from a set |
| `Check(Variable("A"), Value(3))` | Check element in a set |
| `Union(Variable("A"), Variable("B"))` | Union of 2 sets |
| `Intersection(Variable("A"), Variable("B"))` | Intersection of 2 sets |
| `Diff(Variable("A"), Variable("B"))` | Symmetric Difference of 2 sets |
| `Cross(Variable("A"), Variable("B"))` | Cartesian Product of 2 sets |
| `SetMacro("delete", Delete(Variable("A"), Value(1)))` | Initialize a Macro |
| `GetMacro("delete")` | Fetch a Macro by name |
| `SetNamedScope("inner", Union(Variable("A"), Variable("B")))` | Initialize a named scope |
| `GetNamedScope("inner")` | Fetch a named scope |
| `SetAnonScope(Union(Value(Set(5)), Value(Set(10))))` | Initialize an anonymous scope |



## <u>Instructions to Execute</u>

### Using IntelliJ
1. Clone the repository from Github
2. Open the project using IntelliJ.
3. Run the testcases to see the result.

### Using Command Line
1. Navigate to the folder OOPSAssignment1/
2. Run the test cases using the command : sbt clean compile test