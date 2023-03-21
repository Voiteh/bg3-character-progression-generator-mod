package bg3.mod.character.progression.generator.layout.form

import com.vaadin.flow.component.{AbstractField, AttachNotifier, Component, HasValidation, HasValue}
import com.vaadin.flow.component.HasValue.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.flow.data.binder.{Validator, ValueContext}
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.shared.Registration

object validation {
  extension[Value, Event <: HasValue.ValueChangeEvent[Value]] (target: HasValue[Event, Value] & HasValidation & AttachNotifier) {
    def addValidator[Dator <: Validator[Value]](validator: Dator): Registration = {
      return target.addValueChangeListener(event => {
        val result = validator.apply(event.getValue, new ValueContext());
        if (result.isError) {
          target.setErrorMessage(result.getErrorMessage)
          target.setInvalid(true)
        } else {
          target.setErrorMessage(null);
          target.setInvalid(false)
        }
      })
    }
  }
}