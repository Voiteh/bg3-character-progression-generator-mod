package bg3.mod.character.progression.generator.game

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.junit.jupiter.api.extension.Extensions
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.boot.test.context.{SpringBootTest, TestConfiguration}
import org.springframework.context.annotation.{Bean, Import}
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.{InstanceOfAssertFactories, ListAssert, ThrowingConsumer}

import scala.jdk.CollectionConverters.*

@SpringBootTest(classes = Array(classOf[FileResourceGameDataProviderTest.Config]))
@Import(Array(classOf[FileResourceGameDataProvider]))
class FileResourceGameDataProviderTest(@Autowired provider: GameDataProvider) {

  @Test
  def whenProvidedEmptySave_then_shouldParse(): Unit = {
    val version = provider.version("EmptySave");
    assert(version != null)
    assert(version.files.size == 1)
    val file = version.files.find(item => true).orNull;
    assert(file != null)
    assert(file.name == "test.lsx")
    assert(file.save != null)
    assert(file.save.version == null)
    assert(file.save.regions == List.empty)
  }

  @Test
  def whenProvidedSaveWithEmptyRegion_then_shouldParse(): Unit = {
    val version = provider.version("SaveWithEmptyRegion");

    assertThat(version).isNotNull
    assertThat(version.files.size).isOne

    val versionAsserts = Seq[ThrowingConsumer[model.Version]](
      item => assertThat(item.major).isEqualTo("1"),
      item => assertThat(item.minor).isEqualTo("1"),
      item => assertThat(item.revision).isEqualTo("1"),
      item => assertThat(item.build).isEqualTo("1")
    )
    val regionAssert = Seq[ThrowingConsumer[model.Region]](
      item => assertThat(item.id).isEqualTo("Some id"),
      item => assertThat(item.nodes.asJava).isEmpty()
    )
    val saveAsserts = Seq[ThrowingConsumer[model.Save]](
      item => assertThat(item.version).isNotNull,
      item => assertThat(item.regions.asJava).hasSize(1)
        .first
        .satisfies(regionAssert: _*),
      item => assertThat(item.version).satisfies(versionAsserts: _*),
      item => assertThat(item)

    )

    val lsxAsserts = Seq[ThrowingConsumer[model.LSX]](
      lsx => assertThat(lsx.name).isEqualTo("test.lsx"),
      lsx => assertThat(lsx.save).satisfies(saveAsserts: _*)
    )

    assertThat(version.files.asJava).first.satisfies(lsxAsserts: _*)

  }

  @Test
  def whenProvidedRegionWithEmptyNode_then_shouldParse(): Unit = {
    val version = provider.version("RegionWithEmptyNode");

    assertThat(version).isNotNull
    assertThat(version.files.size).isOne

    val versionAsserts = Seq[ThrowingConsumer[model.Version]](
      item => assertThat(item.major).isEqualTo("1"),
      item => assertThat(item.minor).isEqualTo("1"),
      item => assertThat(item.revision).isEqualTo("1"),
      item => assertThat(item.build).isEqualTo("1")
    )
    val nodeAsserts = Seq[ThrowingConsumer[model.Node]](
      item => assertThat(item.id).isEqualTo("root"),
      item => assertThat(item.children).isNull()
    )
    val regionAssert = Seq[ThrowingConsumer[model.Region]](
      item => assertThat(item.id).isEqualTo("Some id"),
      item => assertThat(item.nodes.asJava).hasSize(1).first()
        .satisfies(nodeAsserts: _*)
    )
    val saveAsserts = Seq[ThrowingConsumer[model.Save]](
      item => assertThat(item.version).isNotNull,
      item => assertThat(item.regions.asJava).hasSize(1)
        .first
        .satisfies(regionAssert: _*),
      item => assertThat(item.version).satisfies(versionAsserts: _*)
    )

    val lsxAsserts = Seq[ThrowingConsumer[model.LSX]](
      lsx => assertThat(lsx.name).isEqualTo("test.lsx"),
      lsx => assertThat(lsx.save).satisfies(saveAsserts: _*)
    )

    assertThat(version.files.asJava).first.satisfies(lsxAsserts: _*)

  }

  @Test
  def `When provided children with nodes with attributes, Then should parse`(): Unit = {
    val version = provider.version("ChildrenWithNodesWithAttributes");
    assertThat(version).isNotNull
    assertThat(version.files.size).isOne

    val versionAsserts = Seq[ThrowingConsumer[model.Version]](
      item => assertThat(item.major).isEqualTo("1"),
      item => assertThat(item.minor).isEqualTo("1"),
      item => assertThat(item.revision).isEqualTo("1"),
      item => assertThat(item.build).isEqualTo("1")
    )
    val node1Asserts = Seq[ThrowingConsumer[model.Node]](
      node => assertThat(node.id).isEqualTo("node 1"),
      node => assertThat(node.attributes.asJava)
        .anySatisfy(
          attribute => assertThat(attribute).satisfies(
            Seq[ThrowingConsumer[model.Attribute]](
              value => assertThat(value.id).isEqualTo("node 1 attribute 1"),
              value => assertThat(value.`type`).isEqualTo("some type"),
              value => assertThat(value.value).isEqualTo("some value")
            ): _*
          )
        )
        .anySatisfy(
          attribute => assertThat(attribute).satisfies(
            Seq[ThrowingConsumer[model.Attribute]](
              value => assertThat(value.id).isEqualTo("node 1 attribute 2"),
              value => assertThat(value.`type`).isEqualTo("some type"),
              value => assertThat(value.value).isEqualTo("some value")
            ): _*
          )
        )
        .anySatisfy(
          attribute => assertThat(attribute).satisfies(
            Seq[ThrowingConsumer[model.Attribute]](
              value => assertThat(value.id).isEqualTo("node 1 attribute 3"),
              value => assertThat(value.`type`).isEqualTo("some type"),
              value => assertThat(value.value).isEqualTo("some value")
            ): _*
          )
        )
    )
    val node2Asserts = Seq[ThrowingConsumer[model.Node]](
      node => assertThat(node.id).isEqualTo("node 2"),
      node => assertThat(node.attributes.asJava)
        .anySatisfy(
          attribute => assertThat(attribute).satisfies(
            Seq[ThrowingConsumer[model.Attribute]](
              value => assertThat(value.id).isEqualTo("node 2 attribute 1"),
              value => assertThat(value.`type`).isEqualTo("some type"),
              value => assertThat(value.value).isEqualTo("some value")
            ): _*
          )
        )
        .anySatisfy(
          attribute => assertThat(attribute).satisfies(
            Seq[ThrowingConsumer[model.Attribute]](
              value => assertThat(value.id).isEqualTo("node 2 attribute 2"),
              value => assertThat(value.`type`).isEqualTo("some type"),
              value => assertThat(value.value).isEqualTo("some value")
            ): _*
          )
        )
        .anySatisfy(
          attribute => assertThat(attribute).satisfies(
            Seq[ThrowingConsumer[model.Attribute]](
              value => assertThat(value.id).isEqualTo("node 2 attribute 3"),
              value => assertThat(value.`type`).isEqualTo("some type"),
              value => assertThat(value.value).isEqualTo("some value")
            ): _*
          )
        )
    )

    val nodeChildrenAsserts = Seq[ThrowingConsumer[model.Children]](
      item => assertThat(item.nodes).isNotNull,
      item => assertThat(item.nodes.asJava)
        .satisfies(list => assertThat(list)
          .size()
          .isEqualTo(2)
        ),
      item => assertThat(item.nodes.asJava)
        .allSatisfy(node => assertThat(node.children).isNull())
        .anySatisfy(node => assertThat(node).satisfies(node1Asserts: _*))
        .anySatisfy(node => assertThat(node).satisfies(node2Asserts: _*))
    )

    val nodeAsserts = Seq[ThrowingConsumer[model.Node]](
      item => assertThat(item.id).isEqualTo("root"),
      item => assertThat(item.children).isNotNull(),
      item => assertThat(item.children).satisfies(nodeChildrenAsserts: _*)
    )
    val regionAssert = Seq[ThrowingConsumer[model.Region]](
      item => assertThat(item.id).isEqualTo("Some id"),
      item => assertThat(item.nodes.asJava).hasSize(1).first()
        .satisfies(nodeAsserts: _*)
    )
    val saveAsserts = Seq[ThrowingConsumer[model.Save]](
      item => assertThat(item.version).isNotNull,
      item => assertThat(item.regions.asJava).hasSize(1)
        .first
        .satisfies(regionAssert: _*),
      item => assertThat(item.version).satisfies(versionAsserts: _*)
    )

    val lsxAsserts = Seq[ThrowingConsumer[model.LSX]](
      lsx => assertThat(lsx.name).isEqualTo("test.lsx"),
      lsx => assertThat(lsx.save).satisfies(saveAsserts: _*)
    )

    assertThat(version.files.asJava).first.satisfies(lsxAsserts: _*)

  }

  @Test
  def whenProvidedNodeWithChildrenWithNodes_then_shouldParse(): Unit = {
    val version = provider.version("NodeWithChildrenWithNodes");

    assertThat(version).isNotNull
    assertThat(version.files.size).isOne

    val versionAsserts = Seq[ThrowingConsumer[model.Version]](
      item => assertThat(item.major).isEqualTo("1"),
      item => assertThat(item.minor).isEqualTo("1"),
      item => assertThat(item.revision).isEqualTo("1"),
      item => assertThat(item.build).isEqualTo("1")
    )
    val nodeChildrenAsserts = Seq[ThrowingConsumer[model.Children]](
      item => assertThat(item.nodes).isNotNull,
      item => assertThat(item.nodes.asJava)
        .satisfies(list => assertThat(list)
          .size()
          .isEqualTo(2)
        ),
      item => assertThat(item.nodes.asJava)
        .anySatisfy(item => assertThat(item.id).isEqualTo("node 1"))
        .anySatisfy(item => assertThat(item.id).isEqualTo("node 2"))
    )

    val nodeAsserts = Seq[ThrowingConsumer[model.Node]](
      item => assertThat(item.id).isEqualTo("root"),
      item => assertThat(item.children).isNotNull(),
      item => assertThat(item.children).satisfies(nodeChildrenAsserts: _*)
    )
    val regionAssert = Seq[ThrowingConsumer[model.Region]](
      item => assertThat(item.id).isEqualTo("Some id"),
      item => assertThat(item.nodes.asJava).hasSize(1).first()
        .satisfies(nodeAsserts: _*)
    )
    val saveAsserts = Seq[ThrowingConsumer[model.Save]](
      item => assertThat(item.version).isNotNull,
      item => assertThat(item.regions.asJava).hasSize(1)
        .first
        .satisfies(regionAssert: _*),
      item => assertThat(item.version).satisfies(versionAsserts: _*)
    )

    val lsxAsserts = Seq[ThrowingConsumer[model.LSX]](
      lsx => assertThat(lsx.name).isEqualTo("test.lsx"),
      lsx => assertThat(lsx.save).satisfies(saveAsserts: _*)
    )

    assertThat(version.files.asJava).first.satisfies(lsxAsserts: _*)

  }

}

object FileResourceGameDataProviderTest {
  @TestConfiguration
  class Config() {

    @Bean
    def xmlMapper(): XmlMapper = {
      val mapper = new XmlMapper();
      mapper.registerModule(DefaultScalaModule)
      mapper.setDefaultUseWrapper(false)
      return mapper;

    }

    @Bean
    @Qualifier("versions")
    def versions(): ClassPathResource = {
      return new ClassPathResource("test/game/data/versions")
    }

  }
}
