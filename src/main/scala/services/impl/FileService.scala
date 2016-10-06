package services.impl

import java.io.{File, PrintWriter}

import scala.io.Source

class FileService(homePath: String) extends services.FileService {

  import scala.io.Codec.UTF8

  if (!pathExists(homePath)) {
    createPath(homePath)
  }
  val home = new File(homePath)

  private def pathExists(path: String) = {
    val file = new File(path)
    file.exists()
  }

  private def createPath(path: String) = {
    new File(path).mkdirs
  }

  override def saveFile(name: String, newContent: String): Unit = {
    val file = new File(home.getPath + "/" + name)

    val tmpFile = new File(home.getPath + "/" + name + ".tmp")
    if (file.exists()) {
      file.renameTo(tmpFile)
      file.delete()
    }

    val pw = new PrintWriter(file)
    pw.write(newContent)
    pw.close
    if(tmpFile.exists()) {
      tmpFile.delete()
    }
  }

  override def openFile(name: String): Source = {
    val file = new File(home.getPath + "/" + name)
    Source.fromFile(file)
  }

  override def listFiles: Seq[File] = {
    home.listFiles()
  }

  override def pwd: String = {
    home.getPath
  }

}
