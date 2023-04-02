package bg3.mod.character.progression.generator.game

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.junit.jupiter.api.extension.Extensions
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.boot.test.context.{SpringBootTest, TestConfiguration}
import org.springframework.context.annotation.{Bean, Import}

@SpringBootTest(classes = Array(classOf[FileResourceGameDataProviderTest.Config]))
@Import(Array(classOf[FileResourceGameDataProvider]))
class FileResourceGameDataProviderTest(@Autowired provider: GameDataProvider) {

  @Test
  def shouldLoadFiles(): Unit = {
    provider.versions;
  }


}

object FileResourceGameDataProviderTest {
  @TestConfiguration
  class Config() {

    @Bean
    def xmlMapper(): XmlMapper = {
      val mapper = new XmlMapper();
      mapper.registerModule(DefaultScalaModule)
      return mapper;

    }

    @Bean
    @Qualifier("versions")
    def versions(): ClassPathResource = {
      return new ClassPathResource("game/data/versions")
    }

  }
}
