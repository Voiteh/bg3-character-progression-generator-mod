package org.clazz.progression.generator;

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.material.Material
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class GeneratorApplication


object application extends GeneratorApplication {
  def main(args: Array[String]): Unit = SpringApplication.run(
    classOf[GeneratorApplication], args: _*
  )
}

@Theme(themeClass = classOf[Material])
class ShellConfigurator extends AppShellConfigurator{

}
