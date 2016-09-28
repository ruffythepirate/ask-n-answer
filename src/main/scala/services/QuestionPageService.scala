package services

import entities.{QuestionPage, QuestionPageSmall}

/**
  * Created by ruffy on 9/26/16.
  */
trait QuestionPageService {

  def getAll() : Seq[QuestionPageSmall]

  def get(name : String) : QuestionPage

  def update(page : QuestionPage) : Unit

  def delete(name : String) : Unit
}


//class FileQuestionPageService extends QuestionPageService {
//  def getAll() = ???
//
//  def get(name : String) : QuestionPage = {
//
//  }
//
//  def update(page : QuestionPage) {
//
//  }
//
//  def delete(name : String) {
//
//  }
//
//}


trait QuestionPageServiceComponent {
  val loginService : QuestionPageService
}