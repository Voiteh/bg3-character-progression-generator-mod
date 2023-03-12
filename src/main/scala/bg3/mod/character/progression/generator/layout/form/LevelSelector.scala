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

import java.util.stream

class LevelSelector(availableLevels: Integer, classes: Seq[CharacterClass]) extends Accordion() {
  val selected = new HashSetValuedHashMap[CharacterClass.Id, Level]();
  Range(1, availableLevels + 1).foreach(level => {
    val classSelect = new Select[CharacterClass](classes: _*)
    classSelect.setLabel("Select class")
    classSelect.addValueChangeListener(event => {
      val formLayout = classSelect.getParent.get().asInstanceOf[HasComponents & Component];
      formLayout.getChildren
        .filter(item => item != classSelect)
        .forEach(item => formLayout.remove(item));
      val selectedLevels = selected.get(event.getValue.id).size();
      val subclasses = event.getValue.subclasses.get(selectedLevels+1);
      if (!subclasses.isEmpty) {
        val subclassSelect = new Select[CharacterSubclass]()
        subclassSelect.setItems(new ListDataProvider[CharacterSubclass] (subclasses))
        subclassSelect.setLabel("Select subclass")
        formLayout.add(subclassSelect)
      }
      selected.put(event.getValue.id, level);
    })
    val formLayout = new FormLayout(classSelect);
    add(new AccordionPanel(s"Level $level", formLayout));
  })


}
