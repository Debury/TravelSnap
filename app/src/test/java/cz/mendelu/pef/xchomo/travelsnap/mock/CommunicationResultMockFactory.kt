package cz.mendelu.pef.xchomo.travelsnap.mock

import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult

object CommunicationResultMockFactory {

    fun <T : Any> createSuccessResult(data: T): CommunicationResult.Success<T> {
        return CommunicationResult.Success(data)
    }

    fun createErrorResult(error: cz.mendelu.pef.xchomo.travelsnap.architecture.Error): CommunicationResult.Error {
        return CommunicationResult.Error(error)
    }

    fun createCommunicationErrorResult(): CommunicationResult.CommunicationError {
        return CommunicationResult.CommunicationError()
    }

    fun createExceptionResult(exception: Throwable): CommunicationResult.Exception {
        return CommunicationResult.Exception(exception)
    }
}