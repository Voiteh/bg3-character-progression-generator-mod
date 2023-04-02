package bg3.mod.character.progression.generator.game

object model {
  type Id = String;
  type Type = String;
  type Value = String;

  case class GameVersion(name: String, files: Seq[LSX])

  case class LSX(save: Save)

  case class Save(
                   version: Version,
                   regions: Seq[Region]
                 )

  case class Version(
                      major: Integer, minor: Integer,
                      revision: Integer,
                      build: Integer
                    )

  case class Region(
                     id: Id,
                     nodes: Seq[Node]
                   )

  case class Node(
                   id: Id,
                   attributes: Seq[Attribute],
                   children: Children
                 )

  case class Children(
                       nodes: Seq[Node]
                     )

  case class Attribute(
                        id: Id,
                        `type`: Type,
                        value: Value
                      )
}
