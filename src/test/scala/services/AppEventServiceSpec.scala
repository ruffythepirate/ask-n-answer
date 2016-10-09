package services

import constants.AppEventConstants
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec}

class AppEventServiceSpec extends FunSpec with MockitoSugar with BeforeAndAfter{

  var sut : impl.AppEventService = _
 before {
   sut = new impl.AppEventService
 }

  describe("AppEventService") {

    describe("subscribe and publish") {

      it("can publish to an event with no listeners") {
        sut.publishEvent("random topic", null)
      }

      it("can publish event to one listeners") {
        var wasCalled = false
        val myFunc = (e : AppEvent) => {
          wasCalled = true
        }
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc)

        sut.publishEvent(AppEventConstants.openTopic, null)

        assert(wasCalled === true)
      }

      it("listener gets nothing before publish") {
        var wasCalled = false
        val myFunc = (e : AppEvent) => {
          wasCalled = true
        }
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc)

        assert(wasCalled === false)
      }

      it("can publish event to two listeners") {
        var wasCalled1 = false
        val myFunc1 = (e : AppEvent) => {
          wasCalled1 = true
        }
        var wasCalled2 = false
        val myFunc2 = (e : AppEvent) => {
          wasCalled2 = true
        }
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc1)
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc2)
        sut.publishEvent(AppEventConstants.openTopic, null)

        assert(wasCalled1 === true)
        assert(wasCalled2 === true)
      }

      it("publishes event to second listener event when first fails") {
        val myFunc1 = (e : AppEvent) => ???
        var wasCalled2 = false
        val myFunc2 = (e : AppEvent) => {
          wasCalled2 = true
        }
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc1)
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc2)
        sut.publishEvent(AppEventConstants.openTopic, null)

        assert(wasCalled2 === true)
      }
    }

    describe("unsubscribe") {
      it("can called unsubscribe on a method that isnt registered") {
        var wasCalled1 = false
        val myFunc1 = (e : AppEvent) => {
          wasCalled1 = true
        }
        sut.unsubscribeFromEvent(AppEventConstants.openTopic, myFunc1)
      }

      it("unsubscribes a method") {
        var wasCalled1 = false
        val myFunc1 = (e : AppEvent) => {
          wasCalled1 = true
        }
        sut.subscribeToEvent(AppEventConstants.openTopic, myFunc1)
        sut.unsubscribeFromEvent(AppEventConstants.openTopic, myFunc1)
        sut.publishEvent(AppEventConstants.openTopic, null)
        assert(wasCalled1 === false)

      }
    }


  }
}
