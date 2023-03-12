package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.VersionProvider
import bg3.mod.character.progression.generator.view.model.GameVersion
import com.vaadin.flow.component.{AbstractField, HasValue}
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.select.data.SelectDataView
import com.vaadin.flow.data.provider.{AbstractBackEndDataProvider, BackEndDataProvider, DataProvider, DataProviderListener, Query, QuerySortOrder}
import com.vaadin.flow.shared.Registration
import org.springframework.beans.factory.annotation.Autowired

import scala.jdk.javaapi.StreamConverters
import java.util
import java.util.stream

import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.UI

class VersionSelector(provider: DataProvider[GameVersion, Void]) extends Select[GameVersion]() {
  val selectedVersion: SelectDataView[GameVersion] = setItems(provider);

  this.addValueChangeListener(event => ComponentUtil.fireEvent(
    UI.getCurrent, new events.VersionSelect(this, event.getValue))
  )


  override def setErrorMessage(errorMessage: String): Unit = super.setErrorMessage(errorMessage)

  override def setInvalid(invalid: Boolean): Unit = super.setInvalid(invalid)
}

object VersionSelector {
  class DataProvider(versionProvider: VersionProvider) extends AbstractBackEndDataProvider[GameVersion, Void] {
    override def fetchFromBackEnd(query: Query[GameVersion, Void]): stream.Stream[GameVersion] = StreamConverters.asJavaSeqStream(versionProvider.provide().toStream)

    override def sizeInBackEnd(query: Query[GameVersion, Void]): Int = fetchFromBackEnd(query).count().toInt
  }
}