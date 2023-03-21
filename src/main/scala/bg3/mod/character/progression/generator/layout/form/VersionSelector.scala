package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.VersionProvider
import bg3.mod.character.progression.generator.view.model.GameVersion
import com.vaadin.flow.component.{AbstractField, AttachEvent, ComponentUtil, DetachEvent, HasValue, UI}
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.select.data.SelectDataView
import com.vaadin.flow.data.provider.{AbstractBackEndDataProvider, BackEndDataProvider, DataProvider, DataProviderListener, Query, QuerySortOrder}
import com.vaadin.flow.shared.Registration
import org.springframework.beans.factory.annotation.Autowired

import scala.jdk.javaapi.StreamConverters
import java.util
import java.util.{function, stream}
import com.vaadin.flow.component.HasValue.ValueChangeEvent
import com.vaadin.flow.data.binder.{ValidationResult, ValueContext, Validator as Dator}
import com.vaadin.flow.data.selection.SingleSelectionEvent
import validation.*

import java.util.function.BiFunction
import scala.collection.mutable.ArrayBuffer

class VersionSelector(provider: DataProvider[GameVersion, Void]) extends Select[GameVersion]() {
  val selectedVersion: SelectDataView[GameVersion] = setItems(provider);
  private val registrations = new ArrayBuffer[Registration]();
  this.addValueChangeListener(event => ComponentUtil.fireEvent(
    UI.getCurrent, new events.VersionSelect(this, event.getValue))
  )

  override def onAttach(attachEvent: AttachEvent): Unit = {
    super.onAttach(attachEvent)
    setInvalid(true)
    setErrorMessage(VersionSelector.errorMessage)
    registrations.addOne(
      this.addValidator(VersionSelector.Validator())
    )
  }

  override def onDetach(detachEvent: DetachEvent): Unit = {
    super.onDetach(detachEvent)
    registrations.foreach(registrations => registrations.remove())
  }

  override def setErrorMessage(errorMessage: String): Unit = super.setErrorMessage(errorMessage)

  override def setInvalid(invalid: Boolean): Unit = super.setInvalid(invalid)
}

object VersionSelector {

  val errorMessage = "Invalid game version";

  class DataProvider(versionProvider: VersionProvider) extends AbstractBackEndDataProvider[GameVersion, Void] {
    override def fetchFromBackEnd(query: Query[GameVersion, Void]): stream.Stream[GameVersion] = StreamConverters.asJavaSeqStream(versionProvider.provide().toStream)

    override def sizeInBackEnd(query: Query[GameVersion, Void]): Int = fetchFromBackEnd(query).count().toInt
  }

  class Validator extends Dator[GameVersion | Null] {
    override def apply(value: GameVersion | Null, context: ValueContext): ValidationResult = {
      if (value == null) {
        return ValidationResult.error(errorMessage)
      } else {
        return ValidationResult.ok()
      }
    }
  }
}