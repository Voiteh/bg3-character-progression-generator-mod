package bg3.mod.character.progression.generator.layout.form

import bg3.mod.character.progression.generator.view.model.CharacterClass
import com.vaadin.flow.component.{AttachEvent, ComponentUtil}
import com.vaadin.flow.component.textfield.TextField
import validation.*
import com.vaadin.flow.data.binder.{ValidationResult, ValueContext, Validator as Dator}
import com.vaadin.flow.shared.Registration

import scala.collection.mutable.ArrayBuffer

//TODO add validation for names against already existing classes
class ClassNamer extends TextField {
  private val registrations = new ArrayBuffer[Registration]();

  setLabel("New class name")


  override def setErrorMessage(errorMessage: String): Unit = super.setErrorMessage(errorMessage)

  override def setInvalid(invalid: Boolean): Unit = super.setInvalid(invalid)

  override def onAttach(attachEvent: AttachEvent): Unit = {
    super.onAttach(attachEvent)
    setInvalid(true)
    setErrorMessage(ClassNamer.errorMessage)
    registrations.addOne(
      addValueChangeListener(event => ComponentUtil.fireEvent(attachEvent.getUI,
        new events.ClassNamed(this,event.getValue))
      )
    )
  }
}

object ClassNamer {

  val errorMessage = "Invalid class name";

  class Validator() extends Dator[String | Null] {
    override def apply(value: String | Null, context: ValueContext): ValidationResult = {
      if (value == null) {
        return ValidationResult.error(errorMessage)
      } else {
        return ValidationResult.ok()
      }
    }
  }
}
