package scala.u04.adt_verifier

/*

type:
Sequence[A]

constructor:
  nil: Sequence[A]
  cons: A x Sequence[A] => Sequence[A]

operations:
  filter: Sequence[A] x (A => bool) => Sequence[A]
  concat: Sequence[A] x Sequence[A] => Sequence[A]
  map[B]: Sequence[A] x (A => B) => Sequence[B]
  flatMap[B]: Sequence[A] x (A => Sequence[B]) => Sequence[B]
  reduce: Sequence[A] x (A x A => A) => Opt[A]
  fold: Sequence[A] x A x (A x A => A) => A

axioms:
  filter(nil, f) => nil
  filter(cons(h, t), f) => cons(h, filter(t, f)) if f(h) else filter(t, f)

  concat(nil, s) => s
  concat(cons(h, t), s) =>  cons(h, concat(t, s))

  map(nil, f) => nil
  map(cons(h, t), f) => cons(f(h), map(t, f))

  flatMap(nil, f) => nil
  flatMap(cons(h, t), f) => concat(f(h), flatmap(t, f))

  reduce(nil, f) => Option.empty()
  reduce(cons(h, nil), f) => Option.some(h)
  reduce(cons(h1, cons(h2, t), f) => cons(f(h1, h2), t).reduce(f)

  fold(nil, A, f) => A
  fold(cons(h, t), A, f) => f(h, fold(t, A, f))

*/

object Sequences:

  trait Sequence:
    /* type */
    type Sequence[A]
    /* constructors */
    def nil[A](): Sequence[A]
    def cons[A](): Sequence[A]
    /* properties */
    def filter[A](seq: Sequence[A], f: A => Boolean): Sequence[A]
    def concat[A](seq: Sequence[A], seq2: Sequence[A]): Sequence[A]
    def map[A, B](seq: Sequence[A], f: A => B): Sequence[B]
    def flatMap[A, B](seq: Sequence[A], f: A => Sequence[B]): Sequence[B]
    def fold[A](seq: Sequence[A], el: A, f: (A, A) => A): A

