package org.clazz.progression.generator.view

import com.vaadin.flow.component.HasValue.ValueChangeListener
import com.vaadin.flow.component.accordion.{Accordion, AccordionPanel}
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.{Anchor, Div, Paragraph}
import com.vaadin.flow.component.icon.{Icon, VaadinIcon}
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.{HorizontalLayout, VerticalLayout}
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.tabs.{Tab, TabSheet, Tabs}
import com.vaadin.flow.component.{ClickEvent, Component, ComponentEventListener}
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.{StreamResource, StreamResourceWriter, VaadinSession}
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.material.Material
import org.clazz.progression.generator.view.model.*
import org.clazz.progression.generator.view.model.form.CharacterProgression
import org.springframework.beans.factory.annotation.Autowired
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.server.StreamResource

import java.io.OutputStream
import java.util.function.{Consumer, Supplier}


@Route("")
class ClassProgressionFormLayout(
                                  @Autowired viewFactory: ViewFactory,
                                  @Autowired classProgressionGenerator: ClassProgressionGenerator
                                ) extends AppLayout {


  val title = new Paragraph("Class progression generator")
  val content = viewFactory.classProgressionContent(
    onVersionSelect = (gameVersion: GameVersion, accordion: Accordion, generate: Button) => {
      generate.setEnabled(false)
      accordion.getChildren.forEach(item => accordion.remove(item));
      val progressionBuilder = new CharacterProgression.Builder();
      Range(1, gameVersion.availableLevels + 1).map(number => Level(number)).foreach(level => {
        val levelBuilder = new CharacterProgression.Level.Builder();
        val levelView = viewFactory.levelView(
          gameVersion = gameVersion,
          level = level,
          builder = levelBuilder
        )
        accordion.add(levelView)
        progressionBuilder.addLevel(levelBuilder);
      })
      generate.addClickListener(event => {
        generate.setEnabled(false)
        val characterProgression = progressionBuilder.build();
        val url = classProgressionGenerator.generate(characterProgression);
        val dialog = viewFactory.downloadDialog(url);
        dialog.addDialogCloseActionListener(event => generate.setEnabled(true))
        dialog.open();
      });
      generate.setEnabled(true)
    });


  addToNavbar(title)
  setContent(content)


}
