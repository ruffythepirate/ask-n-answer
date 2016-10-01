package services.impl
import entities.{RepositoryType, TopicSmall}
import services.FileService

class LocalRepository (fileService : FileService) extends services.Repository {

  override def getTopics: Seq[TopicSmall] =  {
    val files = fileService.listFiles

    files.map( file => TopicSmall(file.getName))
  }

  override def getType: _root_.entities.RepositoryType.Value = RepositoryType.Local

  val currentDir = fileService.pwd

  override def name: String = s"Local ($currentDir)"
}