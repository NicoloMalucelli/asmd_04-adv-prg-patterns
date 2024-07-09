package scala.u04.llm

/*
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
 */

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