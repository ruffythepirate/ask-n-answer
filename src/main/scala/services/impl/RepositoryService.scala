package services.impl



class RepositoryService(fileService : FileService) extends services.RepositoryService{

  import scala.concurrent.ExecutionContext.Implicits.global
  def getRepositories = {
    Seq(new LocalRepository( fileService) )
  }

}
