package bg3.mod.character.progression.generator.view

import bg3.mod.character.progression.generator.layout.form.CharacterProgression
import org.springframework.stereotype.Component

import java.net.URL
import java.util.logging.{Logger, Level}

@Component
class MockProgressionGenerator extends ClassProgressionGenerator {
  private val logger = Logger.getLogger(classOf[MockProgressionGenerator].getName)

  override def generate(progression: CharacterProgression): URL = {
    logger.log(Level.INFO, progression.toString)
    URL("https://www.clickdimensions.com/links/TestPDFfile.pdf")
  }
}
