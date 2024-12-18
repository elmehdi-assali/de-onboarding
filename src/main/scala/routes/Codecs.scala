package routes

import dto.requests.CountViewsRequest
import dto.responses.CountViewsResponse
import io.circe._
import io.circe.generic.semiauto._

object Codecs {
  implicit val countViewsRequestEncoder: Encoder[CountViewsRequest] = deriveEncoder
  implicit val countViewsRequestDecoder: Decoder[CountViewsRequest] = deriveDecoder

  implicit val countViewsResponseEncoder: Encoder[CountViewsResponse] = deriveEncoder
  implicit val countViewsResponseDecoder: Decoder[CountViewsResponse] = deriveDecoder
}
