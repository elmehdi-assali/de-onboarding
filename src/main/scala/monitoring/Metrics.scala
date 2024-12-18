package monitoring

import io.prometheus.metrics.core.metrics.Counter

object Metrics {
  val endpointCallCounter: Counter = Counter
    .builder()
    .name("endpoint_call_counter")
    .help("Counter incremented when an endpoint is called")
    .labelNames("endpoint", "status")
    .register()
}
