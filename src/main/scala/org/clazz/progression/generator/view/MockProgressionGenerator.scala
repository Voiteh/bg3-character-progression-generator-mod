package org.clazz.progression.generator.view

import org.clazz.progression.generator.view.model.form
import org.springframework.stereotype.Component

import java.net.URL

@Component
class MockProgressionGenerator extends ClassProgressionGenerator {
  override def generate(progression: form.CharacterProgression): URL = URL("https://www.clickdimensions.com/links/TestPDFfile.pdf")
}
