package scala.u04.llm

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen

object OptionalsCheck extends Properties("Optional") {
  import Optionals._

  // Generator for Int values
  val genInt: Gen[Int] = Gen.choose(-1000, 1000)

  // Property for isNone
  property("isNone") = forAll { (value: Int) =>
    val opt = if (value < 0) OptionalDIY.none() else OptionalDIY.some(value)
    OptionalDIY.isNone(opt) == (value < 0)
  }

  // Property for isSome
  property("isSome") = forAll { (value: Int) =>
    val opt = if (value < 0) OptionalDIY.none() else OptionalDIY.some(value)
    OptionalDIY.isSome(opt) == (value >= 0)
  }

  // Property for getOrElse
  property("getOrElse") = forAll(genInt) { (value: Int) =>
    val opt = if (value < 0) OptionalDIY.none() else OptionalDIY.some(value)
    OptionalDIY.getOrElse(opt, 0) == (if (value >= 0) value else 0)
  }

  // Property for map
  property("map") = forAll(genInt) { (value: Int) =>
    val opt = if (value < 0) OptionalDIY.none() else OptionalDIY.some(value)
    OptionalDIY.map(opt, _ * 2) == (if (value >= 0) OptionalDIY.some(value * 2) else OptionalDIY.none())
  }

  // Property for flatMap
  property("flatMap") = forAll(genInt) { (value: Int) =>
    val opt = if (value < 0) OptionalDIY.none() else OptionalDIY.some(value)
    OptionalDIY.flatMap(opt, x => OptionalDIY.some(x * 2)) == (if (value >= 0) OptionalDIY.some(value * 2) else OptionalDIY.none())
  }

  // Property for filter
  property("filter") = forAll(genInt) { (value: Int) =>
    val opt = if (value < 0) OptionalDIY.none() else OptionalDIY.some(value)
    OptionalDIY.filter(opt, _ % 2 == 0) == (if (value >= 0 && value % 2 == 0) OptionalDIY.some(value) else OptionalDIY.none())
  }
}