package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.model.{CharacterClass, CharacterSubclass, GameVersion}
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.{Component, ComponentEvent, DomEvent}
import com.vaadin.flow.data.selection.{SelectionEvent, SingleSelectionEvent}

object events {
  abstract class FormEvent[Source <: Component](source: Source)
    extends ComponentEvent[Source](source, true)

  class VersionSelect(source: Component, val gameVersion: GameVersion) extends FormEvent[Component](source)

  class LevelConstructed(source: Component, val level: CharacterProgression.Level) extends FormEvent[Component](source)

  class GenerateProgression(source: Component) extends FormEvent[Component](source)

  class ClassNamed(source: Component, val name: String) extends FormEvent[Component](source)

  class FormValidation(source: Component, val valid: Boolean) extends FormEvent[Component](source)

}
