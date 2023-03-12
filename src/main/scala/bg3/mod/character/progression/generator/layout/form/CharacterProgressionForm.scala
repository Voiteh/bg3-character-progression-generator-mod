package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.VersionProvider
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.InMemoryDataProvider
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.shared.Registration

@Route("")
class CharacterProgressionForm(@Autowired versionProvider: VersionProvider) extends AppLayout {
  addToNavbar(new Paragraph("Character progression generator"))
  val dynamicContent= new VerticalLayout();
  dynamicContent.getStyle.set("align-items", "center")
  val content = new VerticalLayout(
    new VersionSelector(
      provider = new VersionSelector.DataProvider(versionProvider)
    ),
    dynamicContent,
    new GeneratorMenuBar()
  )
  content.getStyle.set("align-items", "center")
  setContent(content)

  private var registration: Registration | Null = null

  protected override def onAttach(attachEvent: AttachEvent): Unit = {
    super.onAttach(attachEvent)
    // Register to events from the event bus
    registration = ComponentUtil.addListener(attachEvent.getUI, classOf[events.VersionSelect], (event: events.VersionSelect) => {
      dynamicContent.removeAll()
      dynamicContent.add(new LevelSelector(event.gameVersion.availableLevels, event.gameVersion.classes))
    })
  }

  protected override def onDetach(detachEvent: DetachEvent): Unit = {
    super.onDetach(detachEvent)
    // Unregister from the event bus
    registration.remove
  }

}
