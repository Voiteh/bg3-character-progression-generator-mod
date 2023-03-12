package bg3.mod.character.progression.generator.view

import bg3.mod.character.progression.generator.view.model.form.CharacterProgression
import bg3.mod.character.progression.generator.view.model.form
import org.springframework.stereotype.Component

import java.net.URL

@Component
class MockProgressionGenerator extends ClassProgressionGenerator {
  override def generate(progression: CharacterProgression): URL = URL("https://www.clickdimensions.com/links/TestPDFfile.pdf")
}
