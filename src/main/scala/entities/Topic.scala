package entities

import services.Repository

case class TopicSmall(name: String, repo : Repository)

case class Topic(name: String, questions : Seq[Question])


