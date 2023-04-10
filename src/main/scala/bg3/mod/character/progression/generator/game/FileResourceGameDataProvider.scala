package bg3.mod.character.progression.generator.game

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

import java.io.{File, FileFilter}
import java.util.stream.Collectors

private case class VersionFolder(name: String, files: List[File])

@Component
class FileResourceGameDataProvider(@Autowired mapper: XmlMapper, @Autowired @Qualifier("versions") classPathResource: ClassPathResource) extends GameDataProvider {
  private val versionsFolder = classPathResource.getFile;
  assert(versionsFolder.exists)
  assert(versionsFolder.isDirectory)
  private val versionFolders = versionsFolder.listFiles().filter(file => file.isDirectory)
    .map(file => VersionFolder(
      name = file.getName,
      files = file.listFiles.filter(file => file.isFile && file.getName.endsWith(".lsx"))
        .to(List)
    ))


  override def version(name: String): model.GameVersion | Null = {
    return versionFolders.find(item => item.name.equals(name))
      .map(folder => model.GameVersion(
        name = folder.name,
        files = folder.files.map(file => model.LSX(
          name = file.getName,
          save = mapper.readValue(file, classOf[model.Save])
        ))
        ))
        .orNull;
  }

  override def versions: List[model.GameVersion] = {
    return versionFolders.map(folder => version(folder.name))
      .filter(item => item != null)
      .to(List)
  }
}
