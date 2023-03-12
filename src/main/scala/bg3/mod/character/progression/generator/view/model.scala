package bg3.mod.character.progression.generator.view

import org.apache.commons.collections4.MultiValuedMap
import bg3.mod.character.progression.generator.view.model.form.CharacterProgression
import bg3.mod.character.progression.generator.view.model.form.CharacterProgression.Level

import java.util
import java.util.UUID
import java.util.stream.Collectors
import scala.collection.mutable.*
import scala.collection.{immutable, mutable}

object model {

  object GameVersion {
    case class Id(major: String, minor: String, patch: String);
  }

  object CharacterClass {
    case class Id(value: UUID)
  }

  case class GameVersion(
                          name: String,
                          id: GameVersion.Id,
                          classes: immutable.List[CharacterClass],
                          availableLevels: Integer
                        ) {
    override def toString: String = name

  }

  type Level = Integer;

  case class CharacterClass(
                             name: String,
                             id: CharacterClass.Id,
                             subclasses: MultiValuedMap[Level, CharacterSubclass]) {
    override def toString: String = name
  }

  case class CharacterSubclass(name: String, id: CharacterSubclass.Id) {
    override def toString: String = name
  }

  object CharacterSubclass {
    case class Id(id: UUID)
  }


  object form {
    case class CharacterProgression(
                                     gameVersion: GameVersion.Id,
                                     levels: immutable.Set[CharacterProgression.Level],
                                     className: String
                                   );


    object CharacterProgression {

      class Builder {
        private var className: String = "";
        private var gameVersionId: GameVersion.Id | Null = null;
        private val levelBuilders: mutable.ListBuffer[CharacterProgression.Level.Builder]
        = new mutable.ListBuffer[CharacterProgression.Level.Builder]();


        def className(name: String): Builder = {
          this.className = name;
          return this
        }

        def gameVersion(id: GameVersion.Id): Builder = {
          this.gameVersionId = id;
          return this;
        }

        def addLevel(builder: Level.Builder): Builder = {
          this.levelBuilders += builder;
          return this;
        }

        def build(): CharacterProgression = {

          val levels = levelBuilders
            .map(builder => builder.build())
            .toSet;
          return new CharacterProgression(
            gameVersion = this.gameVersionId,
            levels = levels,
            className = className
          )
        }
      }

      object Level {
        class Builder() {
          private var number: Integer | Null = null;
          private var classId: CharacterClass.Id | Null = null;
          private var subclassId: CharacterSubclass.Id | Null = null

          def number(number: Integer): Builder = {
            this.number = number;
            return this;
          }

          def classId(id: CharacterClass.Id): Builder = {
            this.classId = id;
            return this;
          }

          def subclassId(id: CharacterSubclass.Id): Builder = {
            this.subclassId = id;
            return this;
          }

          def build(): form.CharacterProgression.Level = {
            return new form.CharacterProgression.Level(
              number = this.number,
              classId = this.classId
            );
          }
        }

      }

      case class Level(number: Integer, classId: CharacterClass.Id) {

        override def equals(obj: Any): Boolean = {
          return obj match {
            case other: Level => other.number == this.number
            case _ => false
          }
        }

        override def hashCode: Int = number.hashCode
      }
    }
  }


}
