# Verified Specification, ADTs, type classes, and monads

## Task 2: ADT-VERIFIER

I started by defining the ADT for the class Sequence following the example of what we have seen during the lesson: 

- I first defined the constructors of the class
  ```
  nil: Sequence[A]
  cons: A x Sequence[A] => Sequence[A]
  ```
- Then I defined the operations that can be performed on the class
  ```
  filter: Sequence[A] x (A => bool) => Sequence[A]
  concat: Sequence[A] x Sequence[A] => Sequence[A]
  map[B]: Sequence[A] x (A => B) => Sequence[B]
  ```
- At the end I defined the formal axioms that accurately describe the expected input and output of the previously defined
  operations using a notation very close to the logic programming one
  ```
  filter(nil, f) => nil
  filter(cons(h, t), f) => cons(h, filter(t, f)) if f(h) else filter(t, f)

  concat(nil, s) => s
  concat(cons(h, t), s) =>  cons(h, concat(t, s))

  map(nil, f) => nil
  map(cons(h, t), f) => cons(f(h), map(t, f))
  ```

As second step, I defined the `Sequence` trait using Scala and I wrote the tests by translating the axioms in Scala Check
code. (`SequenceDIY` and `SequenceList`) have been realized by extending the trait and then tested using the `SequenceCheck` 
object.