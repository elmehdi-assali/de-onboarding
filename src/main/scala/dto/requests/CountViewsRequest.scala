package dto.requests

import java.time.ZonedDateTime

case class CountViewsRequest(projectId: Int, from: ZonedDateTime, to: ZonedDateTime)
