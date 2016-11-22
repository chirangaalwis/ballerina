# Introducing Ballerina: A New Programming Language for Integration

Ballerina is a new programming language for integration built on a sequence diagram metaphor. Ballerina is:
- Simple
- Intuitive
- Visual
- Powerful
- Lightweight
- Cloud Native
- Container Native
- Fun

The conceptual model of Ballerina is that of a sequence diagram. Each participant in the integration gets its own lifeline and Ballerina defines a complete syntax and semantics for how the sequence diagram works and execute the desired integration.

Ballerina is not designed to be a general purpose language. Instead you should use Ballerina if you need to integrate a collection of network connected systems such as HTTP endpoints, Web APIs, JMS services, and databases. The result of the integration can either be just that - the integration that runs once or repeatedly ona  schedule, or a reusable HTTP service that others can run.

This is an informal introduction to the Ballerina language.

## Structure of a Ballerina Program
Every Ballerina program has both a textual representation and a normative visual representation. A Ballerina program can be modularized into a collection of packages, with each package(collection of files) contributing one or more resources, functions, actions or types. To access these from another package they must be explicitly imported by the other package.

The structure of a file in Ballerina is as follow:

```
[package PackageName;]
[import (PackageWildCard|PackageName);]*

(VariableDeclaration | ActorDeclaration | TypeDefinition)*

(ResourceDefinition | FunctionDefinition | ActionDefinition)+
```

### Resource Definition

Resources are externally invokable whereas functions are internal subroutines that can only be invoked form a resource. Actions are subroutines that are associated with an actor.

The overall structure of a resource is as follows:

```
@Annotation+
resource ResourceName (Message VariableName
        [, (@Context | @QueryParam | @PathParam) VariableName]*) {
    VariableDeclaration*
    Statement+
}*
```

The visual representation of this (without the annotations) is as follows:

![bal-resource-skeleton.png]()

### Function Definition

A file may also contain functions who’s structure is as follows:

```
[public] function FunctionName ((TypeName VariableName)*) (TypeName*)
        [throws ExceptionName [, ExceptionName]*] {
    VariableDeclaration*
    Statement+
}
```

All functions are private to the package unless explicitly declared to be public with the `public` keyword.. Functions can be invoked from a resource or a function in the same package without an import. It may also be invoked from another package by either importing it first or by using its fully qualified name.

### Action Definition

Actions are operations (functions) that can be executed against an actor. The overall structure of an action is as follows:

```
action ActionName (TypeName ActorName, (TypeName VariableName)*) (TypeName*)
        [throws ExceptionName [, ExceptionName]*] {
    VariableDeclaration*
    Statement+
}
```

First argument of an action should be associated with an actor.

All actions are public. Actions can be invoked from a resource or a function in the same package without an import. It may also be invoked from another file by either importing it first or by using its fully qualified name.

### Variable Declaration

A VariableDeclaration has the following structure:

```
var TypeName VariableName[(, VariableName)*];
```
variable delcaration can assign a value as following.

```
var TypeName VariableName = value;
```

Value can be a integer, a float, a string, a map, or XML or Json literals. Following are examples.

```
var int age = 4;
var double price = 4.0;
var string name = "John"
var xmlElement address_xml = `<address><name>$name</name></address>`
var json address_json = `{ "name":"$name", "streetName":"$street"}`
var map = {"name":"John", "age":34 }
```
Here $name is a variable that is avialable at the current scope.

A TypeName is one of the following built in types or a user defined type name.
- boolean
- int
- long
- float
- double
- string
- xmlElement[<{XSD_namespace_name}type_name>]
- xmlDocument
- json[<json_schema_name>]
- message
- map

### Type Definition

User defined types are defined using a TypeDefinition as follows:
```
type TypeName {
    TypeName VariableName;+
}
```

### Array Types

Array types can be defined by using the array constructor as follows:
- int[]
- long[]
- float[]
- double[]
- string[]
- TypeName[]

All arrays are unbounded in length and support 0 based indexing. Array length can be determined by checking the `.length` property of the array typed variable.

### Type Conversion

For XML and JSON types we can declare a schema as discussed in XXXXXX.
To convert between types we can define a typeconverter as follows:
```
typeconverter TypeConverterName (TypeName VariableName) (TypeName) {
    Statement;+
}
```

When the typeconverter has been defined, we can do the type conversion through an assignment between types.
```
TypeName<{schema_type1}> VariableName1 = ...;   
TypeName<{schema_type2}> VariableName2 = VariableName1;
```
//todo Do we need to convert from multiple input types to a single output type

### Actor Declaration

TODO: What is an actor? Representation of an external system.

A ActorDeclaration has the following structure:

```
actor TypeName ActorName;
```

### Statements

A Statement may be one of the following:
- assignment statement
- if statement
- switch statement
- foreach statement
- fork/join statement
- try/catch statement
- return statement
- reply statement

#### Assignment statement

Assignment statements look like the following:
```
VariableName = Expression;
```

#### If statement

Provides a way to perform conditional execution.
```
if (condition) {
  VariableDeclaration*
  Statement+
}
[else if (condition){
  VariableDeclaration*
  Statement+
}]*
  else {
  VariableDeclaration*
  Statement+
}
```

#### Switch statement

Provides a way to perform conditional execution.
```
switch (predicate) {
        (case valueX:)+
	default:
}*
```

#### Foreach statement

A `foreach` statement provides a way to iterate through a list in order. A `foreach` statement has the following structure:
```
foreach (VariableType VariableName : ValueList) {
  VariableDeclaration*
  Statement+
}
```
A ValueList may be an array or any object which supports iteration.

#### Fork/join statement

TODO: Fix the following definition

```
fork (MessageName) {
  worker workerName (message variableName) {
    Statement;+
    return MessageName;
  }+       
} join JoinCondition (message[] data) {
  Statement;*
}
```
When the `JoinCondition` has been satisfied, the corresponding slots of the message array will be filled with the returned messages from the workers in the order the workers' lexical order. If the condition asks for up to some number of results to be available to satisfy the condition, it may be the case that more than that number are avaialble by the time the statements within the join condition are executed. If a particular worker has not yet completed, the corresponding message slot will be null.

#### Worker statement

```
worker WorkerName(message variableName) {
  Statement;+
  return MessageName;
}
```

#### Wait statement

```
MessageName = wait WorkerName;
```

#### Try/catch statement


```
try {
  VariableDeclaration*
  Statement+
} catch (exception e) {
  VariableDeclaration*
  Statement+
} finally {
  VariableDeclaration*
  Statement+
}
```

#### Return statement

```
return (VariableName)*
```

#### Reply statement

```
reply
```

## Configuration Management

Several Ballerina constructs such as actors and resources have configurable parameters. Examples include the URI of an HTTP endpoint and timeout values. These values MAY be set explicitly within the program using annotations but such values can be overridden from outside the program by applying appropriate property values. These values may be set via environment variables or other deployment management approaches.