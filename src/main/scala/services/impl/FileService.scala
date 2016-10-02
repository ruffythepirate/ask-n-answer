package services.impl
import java.io.File

import scala.io.Source

class FileService(homePath : String) extends services.FileService{

  if(!pathExists(homePath)) {
    createPath(homePath)
  }
  val home = new File(homePath)

  private def pathExists(path : String) = {
    val file = new File(path)
    file.exists()
  }

  private def createPath(path : String) = {
    new File(path).mkdirs
  }


  override def openFile(name: String): Source = ???

  override def listFiles: Seq[File] = {
    home.listFiles()
  }

  override def pwd: String = {
    home.getPath
  }

}
