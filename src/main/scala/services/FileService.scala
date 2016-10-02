package services

import java.io.File

import scala.io.Source


trait FileService {
  def listFiles : Seq[File]

  def openFile(name: String) : Source

  def pwd : String
}
