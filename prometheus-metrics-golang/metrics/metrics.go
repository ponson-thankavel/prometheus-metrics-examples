package metrics

import (
	"fmt"
	"math/rand"
	"net/http"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Returns a message
func GetMessage() string {
	return "Hello World"
}

// Function to process HTTP requests
func ProcessRequest(w http.ResponseWriter, req *http.Request) {

	// Increase counter for requests
	requestCount.Inc()

	fmt.Fprintf(w, "New Request Received")
}

// Function to push new item to queue. Example of simple gauge
func Push(w http.ResponseWriter, req *http.Request) {
	queueSize.Inc()

	fmt.Fprintf(w, "New Item Pushed to Queue")
}

// Function to push new job to wait queue. Example of complex gauge
func JobWaitQueue(w http.ResponseWriter, req *http.Request) {

	//BEGIN Code to fetch # of jobs waiting in each buckets
	//...
	//END Code to fetch # of jobs waiting in each buckets

	//below example sets random counts just as an example. In actual implementation, we should set the actual counts fetched from previous step
	jobWaitQueueSize.WithLabelValues("0", "60").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("60", "120").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("120", "300").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("300", "900").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("900", "1800").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("1800", "3600").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("3600", "7200").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("7200", "14400").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("14400", "28800").Set(float64(int(rand.Float64() * 100)))
	jobWaitQueueSize.WithLabelValues("28800", "+Inf").Set(float64(int(rand.Float64() * 100)))

	fmt.Fprintf(w, "Gauge Metrics for Job Wait Queue is Set")
}

var (
	//Counter Metrics requestCount Initialization
	requestCount = promauto.NewCounter(prometheus.CounterOpts{
		Name: "x_request_count",
		Help: "Number of HTTP requests",
	})

	//Gauge Metrics queueSize Initialization
	queueSize = promauto.NewGauge(prometheus.GaugeOpts{
		Name: "x_queue_size",
		Help: "Number of requests pending in queue",
	})

	//Gauge Metrics jobWaitQueueSize Initialization
	jobWaitQueueSize = promauto.NewGaugeVec(prometheus.GaugeOpts{
		Name: "x_job_wait_queue_size",
		Help: "Number of jobs waiting in queue",
	},
		[]string{
			//lower bound label
			"gt",
			//upper bound label
			"lte",
		},
	)
)
