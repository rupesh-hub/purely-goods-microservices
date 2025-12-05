1. First, add dependency from where other error prune microservices is called
   <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
   </dependency>

2. In properties file, enable circuit breaker
    spring:
      cloud:
        openfeign:
          circuitbreaker:
            enabled: true
3. Resilience 4j configuration
   resilience4j.circuitbreaker:
      configs:
         default:
           slidingWindowSize: 10
           permittedNumberOfCallsInHalfOpenState: 2
           failureRateThreshold: 50
           waitDurationInOpenState: 10000

   resilience4j.retry:
       configs:
          default:
             maxAttempts: 3
             waitDuration: 500
             enableExponentialBackoff: true
             exponentialBackoffMultiplier: 2
             ignoreExceptions:
                - java.lang.NullPointerException
             retryExceptions:
                - java.util.concurrent.TimeoutException

   resilience4j.ratelimiter:
      configs:
         default:
             timeoutDuration: 1000
             limitRefreshPeriod: 5000
             limitForPeriod: 1

    Explanations:
    -----
    ✔️ Resilience4j Circuit Breaker Terms
            
            slidingWindowSize
            Number of recent calls used to calculate failure rate (e.g., last 10 calls).
            
            permittedNumberOfCallsInHalfOpenState
            When circuit transitions from OPEN → HALF-OPEN, this is how many test calls are allowed before deciding to close or open again.
            
            failureRateThreshold
            Percentage of failed calls (within sliding window) that will OPEN the breaker.
            
            waitDurationInOpenState
            How long the circuit stays OPEN before entering HALF-OPEN state to retry.
    
    ✔️ Resilience4j Retry Terms
    
            maxAttempts
            How many total attempts (initial + retries).
            
            waitDuration
            Delay between attempts.
            
            enableExponentialBackoff
            If true, wait time increases exponentially (e.g., 500ms → 1000ms → 2000ms).
            
            exponentialBackoffMultiplier
            Multiplier used to increase delay each retry.
            
            ignoreExceptions
            Exceptions that should not trigger retry attempts.
            
            retryExceptions
            Exceptions that should trigger retries.
    
    ✔️ Resilience4j Rate Limiter Terms
    
            timeoutDuration
            How long a caller waits for permission when limit is exceeded.
            
            limitRefreshPeriod
            How often the limit counter resets.
            
            limitForPeriod
            How many calls are allowed per refresh period.

4. Add retry method in case of fallback

    RateLimiter
    - Controls how many requests are allowed in a given time window.
    - Its goal is to protect downstream services from being overloaded.
    - Prevents too many requests.
    
    Retry
    - Automatically retries a failed call.
    - Its goal is to handle transient failures (timeouts, temporary network issues).
    - Helps recover from failed requests.

    RateLimiter
    - Limits successful or failed calls equally.
    - Triggers based on request volume, not outcome.
    
    Retry
    - Triggers only when a call fails (per configured exceptions).

    RateLimiter
    - Denies or delays calls when limit is exceeded.
    - Example: “Allow only 1 request every 5 seconds.”
    
    Retry
    - Repeats the call with a delay/exponential backoff.
    - Example: “Try again up to 3 times, wait 500ms between retries.”

    Use RateLimiter when:
    - You're calling a downstream API with strict rate limits.
    - You need to avoid throttling, 429 errors, or DOS-like bursts.
    
    Use Retry when:
    - Failures are temporary (timeouts, network glitches).
    - The remote service usually recovers within a short time.

    RateLimiter = A doorman
    - Only lets a certain number of people enter per minute.
    
    Retry = A person re-knocking on the door
    - If no one answers, they try again.