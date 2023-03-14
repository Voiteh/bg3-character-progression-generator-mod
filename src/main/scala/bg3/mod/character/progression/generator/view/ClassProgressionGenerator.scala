package bg3.mod.character.progression.generator.view

import bg3.mod.character.progression.generator.layout.form.CharacterProgression

import java.net.URL

trait ClassProgressionGenerator {


  def generate(progression: CharacterProgression): URL;

}
