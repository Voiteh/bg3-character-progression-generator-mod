package org.clazz.progression.generator.view

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap
import org.clazz.progression.generator.view.model.{CharacterClass, CharacterSubclass, GameVersion, Level}
import org.springframework.stereotype.Component

import java.util.UUID
import scala.collection.mutable
import scala.collection.JavaConverters._

@Component
class MockVersionProvider extends VersionProvider {

  override def provide(): Seq[GameVersion] = Seq(
    new GameVersion(
      name = "Patch 9",
      id = GameVersion.Id(major = "1", minor = "0", patch = "0"),
      availableLevels = 5,
      classes = List(
        CharacterClass(
          name = "Bard",
          id = CharacterClass.Id(
            UUID.randomUUID()
          ),
          subclasses = {
            val multimap = new ArrayListValuedHashMap[Level, CharacterSubclass]()
            multimap.putAll(Level(1), List[CharacterSubclass]().asJava)
            multimap.putAll(Level(2), List[CharacterSubclass]().asJava)
            multimap.putAll(Level(3), List[CharacterSubclass](
              new CharacterSubclass(name = "College of Valor", CharacterSubclass.Id(UUID.randomUUID())),
              new CharacterSubclass(name = "College of Lore", CharacterSubclass.Id(UUID.randomUUID()))
            ).asJava)
            multimap.putAll(Level(4), List[CharacterSubclass]().asJava)
            multimap.putAll(Level(5), List[CharacterSubclass]().asJava)
            multimap
          }
        )
      )
    )
  )


}
