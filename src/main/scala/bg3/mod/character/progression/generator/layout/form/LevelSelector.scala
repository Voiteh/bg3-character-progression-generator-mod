package bg3.mod.character.progression.generator.layout.form

import com.vaadin.flow.component.accordion.{Accordion, AccordionPanel}
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.select.Select
import bg3.mod.character.progression.generator.view.model.{CharacterClass, CharacterSubclass, GameVersion, Level}
import com.vaadin.flow.component.{Component, HasComponents}
import com.vaadin.flow.data.provider.{DataProviderListener, InMemoryDataProvider, ListDataProvider, Query}
import com.vaadin.flow.function.{SerializableComparator, SerializablePredicate}
import com.vaadin.flow.shared.Registration
import org.apache.commons.collections4.MultiValuedMap
import org.apache.commons.collections4.multimap.HashSetValuedHashMap
import com.vaadin.flow.data.binder.{ValidationResult, ValueContext, Validator as Dator}
import java.util.stream
import bg3.mod.character.progression.generator.layout.form.validation._

class LevelSelector(availableLevels: Integer, classes: Seq[CharacterClass]) extends Accordion() {
  val selected = new HashSetValuedHashMap[CharacterClass.Id, Level]();
  Range(1, availableLevels + 1).foreach(level => {
    val classSelect = new Select[CharacterClass](classes: _*)
    classSelect.setLabel("Select class")
    classSelect.addValidator(new LevelSelector.ClassValidator())
    classSelect.setInvalid(true)
    classSelect.setErrorMessage(LevelSelector.classSelectError)
    classSelect.addValueChangeListener(event => {
      val formLayout = classSelect.getParent.get().asInstanceOf[HasComponents & Component];
      formLayout.getChildren
        .filter(item => item != classSelect)
        .forEach(item => formLayout.remove(item));
      val selectedLevels = selected.get(event.getValue.id).size();
      val subclasses = event.getValue.subclasses.get(selectedLevels + 1);

      if (!subclasses.isEmpty) {
        val subclassSelect = new Select[CharacterSubclass]()
        subclassSelect.setInvalid(true)
        subclassSelect.setErrorMessage(LevelSelector.subclassSelectError)
        subclassSelect.addValidator(new LevelSelector.SubclassValidator())
        subclassSelect.setItems(new ListDataProvider[CharacterSubclass](subclasses))
        subclassSelect.setLabel("Select subclass")
        formLayout.add(subclassSelect)
      }
      selected.put(event.getValue.id, level);
    })
    val formLayout = new FormLayout(classSelect);
    add(new AccordionPanel(s"Level $level", formLayout));
  })

  object LevelSelector {
    val classSelectError = "Invalid class selected"
    val subclassSelectError = "Invalid subclass selected"

    class ClassValidator extends Dator[CharacterClass | Null] {
      override def apply(value: CharacterClass | Null, context: ValueContext): ValidationResult = {
        if (value == null) {
          return ValidationResult.error(classSelectError)
        } else {
          return ValidationResult.ok()
        }
      }
    }

    class SubclassValidator extends Dator[CharacterSubclass | Null] {
      override def apply(value: CharacterSubclass | Null, context: ValueContext): ValidationResult = {
        if (value == null) {
          return ValidationResult.error(subclassSelectError)
        } else {
          return ValidationResult.ok()
        }
      }
    }

  }
}
