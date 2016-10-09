package testutils

import java.util.concurrent.Executor

import scala.concurrent.ExecutionContext

trait SynchronousExecutionContext {
  implicit val currentThreadExecutionContext = ExecutionContext.fromExecutor(
    new Executor {
      // Do not do this!
      def execute(runnable: Runnable) {
        runnable.run()
      }
    })

}
