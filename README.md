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

As second step, I defined the `Sequence` trait using Scala and I wrote the tests by translating the axioms in Scala Check code.
`SequenceDIY` and `SequenceList` have been implemented by extending the trait `Sequence` and then tested using the `SequenceCheck`
object.

## Task 5: ADVANCED-FP-LLM

### Writing

I first tried to ask ChatGPT to write the ADT of the generic class Optional but the result it provided was not in the format I wanted, so I tried again but
giving this time an example to follow (i.e. the ATD of the Sequence class described in the previous task).

This time the result was really good, also considered the small amount of information I gave it.

```
type:
  Optional[T]

constructor:
  none: Optional[T]
  some: T => Optional[T]

operations:
  is_none: Optional[T] => bool
  is_some: Optional[T] => bool
  get_or_else: Optional[T] x T => T
  map[U]: Optional[T] x (T => U) => Optional[U]
  flat_map[U]: Optional[T] x (T => Optional[U]) => Optional[U]
  filter: Optional[T] x (T => bool) => Optional[T]

axioms:
  is_none(none) => true
  is_none(some(x)) => false
  is_some(none) => false
  is_some(some(x)) => true
  get_or_else(none, default) => default
  get_or_else(some(x), default) => x
  map(none, f) => none
  map(some(x), f) => some(f(x))
  flat_map(none, f) => none
  flat_map(some(x), f) => f(x)
  filter(none, f) => none
  filter(some(x), f) => if f(x) then some(x) else none
```

### implementing

I then asked ChatGpt to implement this ADT, but the implementation it gave me, even though is working, is quite far from the one I
would have made by my own:

```
sealed trait Optional[+T] {
  def isNone: Boolean
  def isSome: Boolean
  def getOrElse[B >: T](default: B): B
  def map[U](f: T => U): Optional[U]
  def flatMap[U](f: T => Optional[U]): Optional[U]
  def filter(p: T => Boolean): Optional[T]
}

case object None extends Optional[Nothing] {
  def isNone: Boolean = true
  def isSome: Boolean = false
  def getOrElse[B](default: B): B = default
  def map[U](f: Nothing => U): Optional[U] = None
  def flatMap[U](f: Nothing => Optional[U]): Optional[U] = None
  def filter(p: Nothing => Boolean): Optional[Nothing] = None
}

case class Some[+T](value: T) extends Optional[T] {
  def isNone: Boolean = false
  def isSome: Boolean = true
  def getOrElse[B >: T](default: B): B = value
  def map[U](f: T => U): Optional[U] = Some(f(value))
  def flatMap[U](f: T => Optional[U]): Optional[U] = f(value)
  def filter(p: T => Boolean): Optional[T] = if (p(value)) this else None
}
```

Therefore, once again I ask ChatGPT to adhere to my coding style, providing it as example the implementation of SequenceDYI previously coded:

```
object Optionals {

  trait Optional:
    /* type */
    type Optional[T]
    /* constructors */
    def none[T](): Optional[T]
    def some[T](value: T): Optional[T]
    /* operations */
    def isNone[T](opt: Optional[T]): Boolean
    def isSome[T](opt: Optional[T]): Boolean
    def getOrElse[T](opt: Optional[T], default: T): T
    def map[T, U](opt: Optional[T], f: T => U): Optional[U]
    def flatMap[T, U](opt: Optional[T], f: T => Optional[U]): Optional[U]
    def filter[T](opt: Optional[T], p: T => Boolean): Optional[T]

  object OptionalDIY extends Optional:
    private enum OptionalImpl[+T]:
      case None()
      case Some(value: T)

    import OptionalImpl.*

    opaque type Optional[T] = OptionalImpl[T]

    override def none[T](): Optional[T] = None()
    override def some[T](value: T): Optional[T] = Some(value)

    override def isNone[T](opt: Optional[T]): Boolean = opt match {
      case None() => true
      case Some(_) => false
    }

    override def isSome[T](opt: Optional[T]): Boolean = opt match {
      case None() => false
      case Some(_) => true
    }

    override def getOrElse[T](opt: Optional[T], default: T): T = opt match {
      case None() => default
      case Some(value) => value
    }

    override def map[T, U](opt: Optional[T], f: T => U): Optional[U] = opt match {
      case None() => none()
      case Some(value) => some(f(value))
    }

    override def flatMap[T, U](opt: Optional[T], f: T => Optional[U]): Optional[U] = opt match {
      case None() => none()
      case Some(value) => f(value)
    }

    override def filter[T](opt: Optional[T], p: T => Boolean): Optional[T] = opt match {
      case None() => none()
      case Some(value) => if (p(value)) some(value) else none()
    }
}
```

### testing

Finally, I asked ChatGPT to test the implementation using ScalaCheck and it did at the first try with quite good results without
the necessity of providing additional information.

### conclusions

To conclude, I think that LLMs such as ChatGPT can be a really helpful tool when we have to define an ADT. In the scenario I tested, the class
to describe was a well known one (Optional), therefore I didn't have to describe  how it works, the possible operations and the expected results, anyway
I think that this process can also be applied to any class previous description of the scenario.