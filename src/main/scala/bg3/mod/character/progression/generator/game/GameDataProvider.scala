package bg3.mod.character.progression.generator.game

import scala.collection.immutable.Seq

trait GameDataProvider {

  def versions: List[model.GameVersion]

  def version(name: String): model.GameVersion
}
