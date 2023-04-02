package bg3.mod.character.progression.generator.game
import scala.collection.immutable.Seq
trait GameDataProvider {

  def versions: Seq[model.GameVersion]

}
