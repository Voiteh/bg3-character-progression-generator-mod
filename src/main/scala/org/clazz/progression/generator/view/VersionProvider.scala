package org.clazz.progression.generator.view

import org.clazz.progression.generator.view.model.GameVersion

trait VersionProvider {

  def provide(): Seq[GameVersion]


}
