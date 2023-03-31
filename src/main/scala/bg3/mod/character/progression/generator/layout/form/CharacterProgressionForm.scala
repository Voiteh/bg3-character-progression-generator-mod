package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.model.GameVersion
import bg3.mod.character.progression.generator.view.{ClassProgressionGenerator, VersionProvider}
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.InMemoryDataProvider
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import com.vaadin.flow.component.{AbstractField, AttachEvent, ComponentUtil, DetachEvent}
import com.vaadin.flow.data.binder.{Binder, ValidationResult, Validator, ValueContext}
import com.vaadin.flow.function.ValueProvider
import com.vaadin.flow.shared.Registration
import bg3.mod.character.progression.generator.layout.form.validation._
import scala.collection.mutable

@Route("")
class CharacterProgressionForm
(@Autowired versionProvider: VersionProvider, @Autowired generator: ClassProgressionGenerator)
  extends AppLayout {
  addToNavbar(new Paragraph("Character progression generator"))
  val dynamicContent = new VerticalLayout();
  dynamicContent.getStyle.set("align-items", "center")
  val versionSelector = new VersionSelector(
    provider = new VersionSelector.DataProvider(versionProvider)
  );
  val menuBar=new GeneratorMenuBar()
  val content: VerticalLayout = new VerticalLayout(
    versionSelector,
    dynamicContent,
    menuBar
  )
  content.getStyle.set("align-items", "center")
  setContent(content)
  var data: CharacterProgression.Builder | Null = null;

  private val registrations = mutable.ArrayBuffer.empty[Registration];

  protected override def onAttach(attachEvent: AttachEvent): Unit = {
    super.onAttach(attachEvent)
    // Register to events from the event bus
    registrations.addAll(
      Seq(
        ComponentUtil.addListener(attachEvent.getUI, classOf[events.VersionSelect], (event: events.VersionSelect) => {
          dynamicContent.removeAll()
          dynamicContent.add(
            new ClassNamer(),
            new LevelSelector(event.gameVersion.availableLevels, event.gameVersion.classes)
          )
          this.data = new CharacterProgression.Builder(event.gameVersion.availableLevels);
          this.data match {
            case builder: CharacterProgression.Builder =>
              ComponentUtil.fireEvent(attachEvent.getUI,
                new events.FormValidation(this,
                  builder.gameVersion(event.gameVersion.id).validate()
                )
              )

          }
        }),
        ComponentUtil.addListener(attachEvent.getUI, classOf[events.LevelConstructed],
          (event: events.LevelConstructed) => {
            this.data match {
              case builder: CharacterProgression.Builder => ComponentUtil.fireEvent(attachEvent.getUI,
                new events.FormValidation(this, builder.putLevel(event.level).validate()
                )
              )
            }
          }),
        ComponentUtil.addListener(attachEvent.getUI, classOf[events.GenerateProgression],
          (event: events.GenerateProgression) => {
            this.data match {
              case builder: CharacterProgression.Builder => {
                val progression = builder.build()
                val url = generator.generate(progression);
                new DownloadDialog(url).open();
              }
            }
          }),
        ComponentUtil.addListener(attachEvent.getUI, classOf[events.ClassNamed], event => {
          this.data match {
            case builder: CharacterProgression.Builder => ComponentUtil.fireEvent(attachEvent.getUI,
              new events.FormValidation(this, builder.className(event.name).validate())
            )
          }
        })
      )
    )
  }

  protected override def onDetach(detachEvent: DetachEvent): Unit = {
    super.onDetach(detachEvent)
    // Unregister from the event bus
    registrations.foreach(registration => registration.remove())
  }

}
