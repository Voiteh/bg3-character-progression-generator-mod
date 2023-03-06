package org.clazz.progression.generator.view

import org.clazz.progression.generator.view.model.form.CharacterProgression

import java.net.URL

trait ClassProgressionGenerator {


  def generate(progression: CharacterProgression): URL;

}
