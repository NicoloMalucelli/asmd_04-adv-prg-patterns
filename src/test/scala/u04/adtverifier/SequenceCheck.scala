package scala.u04.adtverifier

import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalacheck.Test.Parameters
import org.scalacheck.Prop.forAll
import org.scalacheck.Arbitrary.arbitrary

import Sequences.SequenceDIY.*
//import Sequences.SequenceList.*

object SequenceCheck extends Properties("Sequence"):
  def sequenceGen[A](implicit arbA: Arbitrary[A]): Gen[Sequence[A]] = Gen.frequency(
    20 -> nil(),
    80 -> (for {
          head <- arbitrary[A]
          tail <- sequenceGen
        } yield cons(head, tail))
  )

  implicit def arbitrarySequence[A](implicit arbA: Arbitrary[A]): Arbitrary[Sequence[A]] =
    Arbitrary(sequenceGen)

  property("filter(nil, f) => nil") =
    forAll { (f: Int => Boolean) =>
      filter(nil[Int](), f) == nil[Int]()
    }

  property("filter(cons(h, t), f) => cons(h, filter(t, f)) if f(h) else filter(t, f)") =
    forAll { (h: Int, t: Sequence[Int], f: Int => Boolean) =>
      filter(cons(h, t), f) == (if (f(h)) cons(h, filter(t, f)) else filter(t, f))
    }

  property("concat(nil, s) => s") =
    forAll { (s: Sequence[Int]) =>
      concat(nil[Int](), s) == s
    }

  property("concat(cons(h, t), s) => cons(h, concat(t, s))") =
    forAll { (h: Int, t: Sequence[Int], s: Sequence[Int]) =>
      concat(cons(h, t), s) == cons(h, concat(t, s))
    }

  property("map(nil, f) => nil") =
    forAll { (f: Int => String) =>
      map(nil[Int](), f) == nil[String]()
    }

  property("map(cons(h, t), f) => cons(f(h), map(t, f))") =
    forAll { (h: Int, t: Sequence[Int], f: Int => String) =>
      map(cons(h, t), f) == cons(f(h), map(t, f))
    }