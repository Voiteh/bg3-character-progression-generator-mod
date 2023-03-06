package org.clazz.progression.generator.view

import com.vaadin.flow.component.accordion.{Accordion, AccordionPanel}
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.{Icon, VaadinIcon}
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.select.Select
import org.clazz.progression.generator.view.model.{CharacterClass, CharacterSubclass, GameVersion, Level}
import org.clazz.progression.generator.view.model.form.CharacterProgression
import org.springframework.stereotype.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.server.StreamResource
import org.springframework.beans.factory.annotation.Autowired

import java.net.URL
import java.util.Optional
import java.util.function.{Consumer, Supplier}


@Component
class ViewFactory(@Autowired versionProvider: VersionProvider) {
  val SUBCLASS_PREFIX: String = "Subclass of "
  val GENERATE_PROGRESSION_ID = "Generate progression id";

  def levelView(gameVersion: GameVersion, level: Level, builder: CharacterProgression.Level.Builder): AccordionPanel = {
    builder.number(level.number);
    val formLayout = new FormLayout()
    val classSelect = new Select[CharacterClass]()
    classSelect.setLabel("Select class")

    classSelect.setItems(gameVersion.classes: _*)
    classSelect.addValueChangeListener(event => {
      formLayout.getChildren
        .filter(item => item.getId.orElse("")
          .startsWith(SUBCLASS_PREFIX))
        .forEach(item => formLayout.remove(item))
      val subClasses = event.getValue.subclasses
      builder.classId(event.getValue.id);
      val providedSubclasses = subClasses.get(level)
      if (!providedSubclasses.isEmpty) {
        val subClassSelect = new Select[CharacterSubclass]()
        subClassSelect.setLabel("Select subclass")
        subClassSelect.setId(s"$SUBCLASS_PREFIX ${event.getValue.name}")
        subClassSelect.setItems(providedSubclasses)
        subClassSelect.addValueChangeListener(event => builder.subclassId(event.getValue.id))
        formLayout.add(subClassSelect)
      }
    })
    formLayout.add(classSelect)

    val panel = new AccordionPanel(s"Level $level", formLayout);

    return panel;
  }


  def downloadDialog(url: URL): Dialog = {
    val dialog = new Dialog();

    dialog.setHeaderTitle("Download generated progression")

    val dialogLayout = new VerticalLayout()
    dialog.add(dialogLayout)

    val downloadAnchor = new Anchor(url.toString)
    downloadAnchor.getElement.setAttribute("download", true)
    downloadAnchor.removeAll()
    val download = new Button("Download progression", new Icon(VaadinIcon.DOWNLOAD_ALT))
    downloadAnchor.add(download)

    val cancelButton = new Button("Cancel", (e) => dialog.close())
    dialog.getFooter.add(cancelButton)
    dialog.getFooter.add(downloadAnchor)
    return dialog;
  }

  def accordionContainer(): Accordion = {
    val accordion = new Accordion();
    return accordion
  }

  def menuBar(): MenuBar = {
    val menuBar = new MenuBar();
    return menuBar;
  }


  def versionSelect(onSelection: (gameVersion: GameVersion, accordion: Accordion, generateButton: Button) => Unit, accordion: Accordion, generateButton: Button): Select[GameVersion] = {
    val versionSelect = new Select[GameVersion]();
    versionSelect.setLabel("Game Version");
    versionSelect.setItems(versionProvider.provide(): _*)
    versionSelect.addValueChangeListener(event => {
      onSelection(gameVersion = event.getValue, accordion = accordion, generateButton = generateButton);
    });
    return versionSelect;
  }

  def generateProgression(): Button = {
    val button = new Button("Generate progression", new Icon(VaadinIcon.AUTOMATION));
    return button;
  }

  def classProgressionContent(
                               onVersionSelect: (version: GameVersion, accordion: Accordion, generate: Button) => Unit
                             ): VerticalLayout = {
    val accordion = accordionContainer();
    val menuBar = this.menuBar()
    val generate = generateProgression()
    menuBar.addItem(generate)
    val versionSelect = this.versionSelect(onSelection = onVersionSelect, accordion = accordion, generateButton = generate);


    val content = new VerticalLayout(
      versionSelect,
      accordion,
      menuBar
    )
    content.getStyle.set("align-items", "center")

    return content;

  }
}

