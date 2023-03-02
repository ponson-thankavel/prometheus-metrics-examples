package metrics_test

import (
	"prometheus-metrics-golang/metrics"
	"testing"
)

func TestMetrics(t *testing.T) {
	if metrics.GetMessage() != "Hello World" {
		t.Fatal("Wrong metrics")
	}
}
