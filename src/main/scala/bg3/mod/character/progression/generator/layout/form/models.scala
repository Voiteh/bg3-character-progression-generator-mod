package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.model.{CharacterClass, CharacterSubclass, GameVersion}

import javax.validation.constraints.NotEmpty
import scala.collection.{immutable, mutable}


case class CharacterProgression(
                                 gameVersion: GameVersion.Id,
                                 levels: immutable.Set[CharacterProgression.Level],
                                 className: String
                               )


object CharacterProgression {

  class Builder {
    private var className: String = "";
    private var gameVersionId: GameVersion.Id | Null = null;
    private val levels: mutable.Set[CharacterProgression.Level] = mutable.Set.empty;


    def className(name: String): Builder = {
      this.className = name;
      return this
    }

    def gameVersion(id: GameVersion.Id): Builder = {
      this.gameVersionId = id;
      return this;
    }

    def putLevel(level: Level): Builder = {
      this.levels.add(level);
      return this;
    }

    def build(): CharacterProgression = {

      return new CharacterProgression(
        gameVersion = this.gameVersionId,
        levels = levels.toSet,
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

      def build(): CharacterProgression.Level = {
        return new CharacterProgression.Level(
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
