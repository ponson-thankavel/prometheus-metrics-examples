package main

import (
	"net/http"

	"prometheus-metrics-golang/metrics"

	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {

	http.HandleFunc("/request", metrics.ProcessRequest)
	http.HandleFunc("/queue", metrics.Push)
	http.HandleFunc("/jobs", metrics.JobWaitQueue)

	http.Handle("/metrics", promhttp.Handler())
	http.ListenAndServe(":8080", nil)
}
