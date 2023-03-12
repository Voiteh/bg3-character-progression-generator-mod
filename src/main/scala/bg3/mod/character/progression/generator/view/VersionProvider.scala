package bg3.mod.character.progression.generator.view

import bg3.mod.character.progression.generator.view.model.GameVersion

trait VersionProvider {

  def provide(): Seq[GameVersion]


}
