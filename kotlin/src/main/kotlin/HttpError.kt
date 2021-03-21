
/******************************************************************************
 * Thrown when a HTTP service encounters an error
 *
 * @property[status] HTTP status code
 * @property[publicMsg] Error message
 * @param[cause] Cause of the error
 *****************************************************************************/
open class HttpError(
  val status: Int,
  val publicMsg: String,
  cause: Exception? = null
): Exception(publicMsg, cause) {

  init {
    when {
      status < 400 -> throw bug(
        "HTTP error status codes must be 400 or greater")
      status >= 600 -> throw bug(
        "HTTP error status codes must be less than 600")
    }
  }

  companion object {

    /**************************************************************************
     * Returns a client error with a 400 status when a client supplied
     * parameter is missing or invalid
     *
     * @param[publicMsg] Public response message
     *************************************************************************/
    fun badRequest(publicMsg: String) = HttpError(400, publicMsg)

    /**************************************************************************
     * Returns a client error with a 401 status when a client attempts to
     * access a resource before they have authenticated or there authentication
     * details were invalid
     *
     * @param[publicMsg] Public response message
     *************************************************************************/
    fun badLogin(publicMsg: String) = HttpError(401, publicMsg)

    /**************************************************************************
     * Returns a client error with a 403 status when a client attempts to
     * access a resource they are not authorised to access
     *
     * @param[publicMsg] Public response message
     *************************************************************************/
    fun notAllowed(publicMsg: String) = HttpError(403, publicMsg)

    /**************************************************************************
     * Returns a client error with a 404 'not found' status
     *
     * @param[publicMsg] Public response message
     *************************************************************************/
    fun notFound(
      publicMsg: String = "No such resource"
    ) = HttpError(404, publicMsg)

    /**************************************************************************
     * Returns a service error with a 500 status when a bug is detected
     *
     * @param[internalMsg] Message for internal use, logs etc..
     * @param[cause] Cause of the error
     *************************************************************************/
    fun bug(
      internalMsg: String,
      cause: Exception? = null
    ) = HttpError(
      500,
      "Internal service error",
      Exception(internalMsg, cause)
    )

    /**************************************************************************
     * Returns a service error with a 500 status when a bad service
     * configuration is detected
     *
     * @param[publicMsg] Public response message
     * @param[cause] Cause of the error
     *************************************************************************/
    fun badConfig(
      publicMsg: String,
      cause: Exception? = null
    ) = HttpError(500, publicMsg, cause)

    /**************************************************************************
     * Returns a service error with a 501 status when a service feature has
     * been declared but not implemented
     *
     * @param[publicMsg] Public response message
     * @param[cause] Cause of the error
     *************************************************************************/
    fun notImplemented(
      publicMsg: String,
      cause: Exception? = null
    ) = HttpError(501, publicMsg, cause)

    /**************************************************************************
     * Returns a service error with a 503 status when a service feature is
     * unavailable due to a known issue or maintenance
     *
     * @param[publicMsg] Public response message
     * @param[cause] Cause of the error
     *************************************************************************/
    fun featureUnavailable(
      publicMsg: String,
      cause: Exception? = null
    ) = HttpError(503, publicMsg, cause)
  }
}