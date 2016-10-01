package services.impl
import java.io.File

class FileService extends services.FileService{
  override def listFiles: Seq[File] = {
    File.listRoots()
  }

  override def pwd: String = {
    System.getProperty("user.dir")
  }

}
