#lang racket
(require htdp/error)
(require json)
(require racket/port)
(require net/url)

(require "sticky-web.rkt")

; Define the structs
(define-struct post (ups downs created subreddit id title author text? nsfw? content permalink))

(define-struct comment (ups downs created subreddit id author body body-html replies))

; Define the functions for building structs from json
(define (json->post jdata)
  (make-post (hash-ref (hash-ref jdata 'data) 'ups)
             (hash-ref (hash-ref jdata 'data) 'downs)
             (hash-ref (hash-ref jdata 'data) 'created)
             (hash-ref (hash-ref jdata 'data) 'subreddit)
             (hash-ref (hash-ref jdata 'data) 'id)
             (hash-ref (hash-ref jdata 'data) 'title)
             (hash-ref (hash-ref jdata 'data) 'author)
             (hash-ref (hash-ref jdata 'data) 'is_self)
             (hash-ref (hash-ref jdata 'data) 'over_18)
             (if (hash-ref (hash-ref jdata 'data) 'is_self)
                 (hash-ref (hash-ref jdata 'data) 'selftext)
                 (hash-ref (hash-ref jdata 'data) 'url))
             (hash-ref (hash-ref jdata 'data) 'permalink)))

(define (json->comment jdata)
  (make-comment (hash-ref (hash-ref jdata 'data) 'ups)
                (hash-ref (hash-ref jdata 'data) 'downs)
                (hash-ref (hash-ref jdata 'data) 'created)
                (hash-ref (hash-ref jdata 'data) 'subreddit)
                (hash-ref (hash-ref jdata 'data) 'id)
                (hash-ref (hash-ref jdata 'data) 'author)
                (hash-ref (hash-ref jdata 'data) 'body)
                (hash-ref (hash-ref jdata 'data) 'body_html)
                (if (equal? "" (hash-ref (hash-ref jdata 'data) 'replies))
                    empty
                    (map json->comment (drop-right (hash-ref (hash-ref (hash-ref (hash-ref jdata 'data) 'replies) 'data) 'children) 1)))))

(define (make-null-post)
  (make-post 0 0 0 "" 0 "" "" #t #f "" ""))

; Check if the sort-mode is a valid option
(define (valid-sorting-mode? sort-mode)
  (and (string? sort-mode) 
       (or (string-ci=? sort-mode "new")
           (string-ci=? sort-mode "top")
           (string-ci=? sort-mode "hot"))))

(define (process-post post)
  (make-post (post-ups post)
             (post-downs post)
             (post-created post)
             (post-subreddit post)
             (post-id post)
             (post-title post)
             (post-author post)
             (post-text? post)
             (post-nsfw? post)
             (post-content post)
             ;(if (post-text? post) 
             ;    (post-content post)
             ;    (bitmap/cache-url (post-content post)))
             (post-permalink post)))


; Define the services, and their helpers
(define (get-posts subreddit sort-mode)
  (check-arg 'get-posts (string? subreddit) 'string 1 subreddit)
  (check-arg 'get-posts (valid-sorting-mode? sort-mode) 'string 2 sort-mode)
  (local [(define post-data (get-posts/string (string-downcase subreddit) (string-downcase sort-mode)))]
    (cond [(eof-object? post-data) empty]
          [(and (string? post-data)
                (string=? "" post-data) "get-posts: the given subreddit is not available offline.")]
          [(and (string? post-data)
                (string-ci=? (substring post-data 0 9) "<!doctype"))
           (error "get-posts: temporary data error, try again")]
          [(or (number? post-data) 
               (and (hash? post-data) 
                    (hash-has-key? post-data 'error)))
           (error "get-posts: unable to find the subreddit")]
          [else (map (lambda (data) (process-post (json->post data)))
                     (hash-ref (hash-ref (string->jsexpr (get-posts/string subreddit sort-mode) )
                                         'data) 
                               'children))])))

(define (get-posts/json subreddit sort-mode)
  (string->jsexpr (get-posts/string subreddit sort-mode)))

(define (get-posts/string subreddit sort-mode)
  (get->string (string-append "http://www.reddit.com/r/" subreddit "/" sort-mode ".json") 
               (list)
               (list (cons 'sort_mode sort-mode) (cons 'subreddit subreddit))))

(define (get-comments id sort-mode)
  (local [(define DATA (get-comments/json id sort-mode))]
    (if (and (hash? DATA) (hash-has-key? DATA 'error))
        empty
        (map json->comment 
             (drop-right (hash-ref (hash-ref (second DATA) 'data) 'children) 1)))
    ))

(define (get-comments/json id sort-mode)
  (string->jsexpr (get-comments/string id sort-mode)))

(define (get-comments/string id sort-mode)
  (get->string (string-append "http://www.reddit.com/r/all/comments/" id "/" sort-mode ".json") 
               (list) 
               (list (cons 'id id) (cons 'sort_mode sort-mode))))

(define (post->key subreddit sort-mode)
  (string-append "http://www.reddit.com/r/" subreddit "/" sort-mode ".json"))

(define (filter-nsfw posts)
  (local [(define data (string->jsexpr posts))]
    (filter (lambda (post)
              (not (hash-ref (hash-ref post 'data) 'over_18)))
            (lookup-post data)))
  )

(define (lookup-post cache)
  (hash-ref (hash-ref cache 'data) 'children))
(define (generate-cache inputs limit)
  (local
    [(define new-cache (make-hash))]
    (map (lambda (input)
           (local [(define subreddit (first input))
                   (define sort-mode (second input))
                   (define posts (take (filter-nsfw (get-posts/string subreddit sort-mode)) limit))]
             (begin
               (hash-set! new-cache 
                          (string->symbol (post->key subreddit sort-mode))
                          (jsexpr->string posts))
               (map (lambda (post)
                      (if (hash-ref (hash-ref post 'data) 'is_self)
                          #f
                          (hash-set! new-cache
                                     (string->symbol (hash-ref (hash-ref post 'data) 'url))
                                     (get/string (hash-ref (hash-ref post 'data) 'url)))))
                    posts)
               )))
         inputs)
    new-cache))

(define (store-cache filename inputs limit)
  (begin 
    (define out (open-output-file filename #:mode 'binary #:exists 'replace))
    (write-json (generate-cache inputs limit)
                out
                #:encode 'all)
    (close-output-port out)))

(define (get/string string-url)
  (bytes->string/latin-1 (port->bytes (get-pure-port (string->url string-url)))))