package services

import java.io.File


trait FileService {
  def listFiles : Seq[File]
}
