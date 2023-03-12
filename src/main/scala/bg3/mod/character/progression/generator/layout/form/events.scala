package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.model.{CharacterClass, CharacterSubclass, GameVersion}
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.{Component, ComponentEvent}
import com.vaadin.flow.data.selection.{SelectionEvent, SingleSelectionEvent}

object events {
  abstract class FormEvent[Source <: Component](source: Source)
    extends ComponentEvent[Source](source, true)

  class VersionSelect(source: Component, val gameVersion: GameVersion) extends FormEvent[Component](source)

  class ClassSelect(source: Component, val characterClass: CharacterClass) extends FormEvent[Component](source)

  class SubclassSelect(source: Component, val characterSubclass: CharacterSubclass) extends FormEvent[Component](source)
}
