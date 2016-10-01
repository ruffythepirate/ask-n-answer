package services.impl

import services.FileService

class RepositoryService(fileService : FileService) extends services.RepositoryService{

  def getRepositories = {
    Seq(new LocalRepository( fileService) )
  }

}
