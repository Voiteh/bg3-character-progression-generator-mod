package bg3.mod.character.progression.generator.game

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

import java.io.{File, FileFilter}
import java.util.stream.Collectors

private case class VersionFolder(name: String, files: Seq[File])

@Component
class FileResourceGameDataProvider(@Autowired mapper: XmlMapper, @Autowired @Qualifier("versions") classPathResource: ClassPathResource) extends GameDataProvider {
  private val versionsFolder = classPathResource.getFile;
  assert(versionsFolder.exists)
  assert(versionsFolder.isDirectory)
  private val versionFolders = versionsFolder.listFiles().filter(file => file.isDirectory)
    .map(file => VersionFolder(
      name = file.getName,
      files = file.listFiles.filter(file => file.isFile && file.getName.endsWith(".lsx"))
        .to(Seq)
    ))
  versionsFolder.toString;

  override def versions: Seq[model.GameVersion] = {
    return versionFolders.to(LazyList)
      .map(folder => new model.GameVersion(
        name = folder.name,
        files = folder.files.map(file => mapper.readValue(file, classOf[model.LSX]))
      ))
  }
}
