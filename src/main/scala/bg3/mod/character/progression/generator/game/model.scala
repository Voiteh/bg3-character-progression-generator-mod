package bg3.mod.character.progression.generator.game

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

object model {
  type Id = String;
  type Type = String;
  type Value = String;

  case class GameVersion(name: String, files: List[LSX])

  case class LSX(
                  name: String,
                  save: Save
                )

  @JacksonXmlRootElement(localName = "save")
  case class Save(
                   @JacksonXmlProperty(isAttribute = false, localName = "version")
                   version: Version | Null,
                   @JacksonXmlProperty(isAttribute = false, localName = "region")
                   regions: List[Region] = List()
                 )

  case class Version(
                      major: String | Null,
                      minor: String | Null,
                      revision: String | Null,
                      build: String | Null
                    )

  case class Region(
                     id: Id | Null,
                     @JacksonXmlProperty(isAttribute = false, localName = "node")
                     nodes: List[Node] = List()
                   )

  case class Node(
                   id: Id | Null,
                   @JacksonXmlProperty(isAttribute = false, localName = "attribute")
                   attributes: List[Attribute] = List(),
                   children: Children|Null
                 )

  case class Children(
                       @JacksonXmlProperty(isAttribute = false, localName = "node")
                       nodes: List[Node] = List()
                     )

  case class Attribute(
                        id: Id | Null,
                        `type`: Type | Null,
                        value: Value | Null
                      )
}
