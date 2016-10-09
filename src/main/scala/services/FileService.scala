package services

import java.io.File

import scala.io.Source


trait FileService {
  def listFiles : Seq[File]

  def saveFile(name: String, newContent : String)

  def deleteFile(name : String)

  def openFile(name: String) : Source

  def pwd : String
}
