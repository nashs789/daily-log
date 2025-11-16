ALTER SEQUENCE webhook_history_id_seq RESTART WITH 1;

INSERT INTO webhook_history (url, user_id, content, raw_content, params, platform, http_status, is_success, error_message, created, modified)
VALUES (
         'test_url'
       , 'user1'
       , 'content'
       , 'raw_content'
       , '{"test": "hello", "code": "world"}'
       , 'DISCORD'
       , 203
       , true
       , ''
       , now()
       , now()
);

INSERT INTO webhook_history (url, user_id, content, raw_content, params, platform, http_status, is_success, error_message, created, modified)
VALUES (
         'test_url'
       , 'user1'
       , 'content'
       , 'raw_content'
       , '{"test": "hello", "code": "world"}'
       , 'DISCORD'
       , 203
       , true
       , ''
       , now()
       , now()
);

INSERT INTO webhook_history (url, user_id, content, raw_content, params, platform, http_status, is_success, error_message, created, modified)
VALUES (
         'test_url'
       , 'user1'
       , 'content'
       , 'raw_content'
       , '{"test": "hello", "code": "world"}'
       , 'DISCORD'
       , 203
       , true
       , ''
       , now()
       , now()
);

INSERT INTO webhook_history (url, user_id, content, raw_content, params, platform, http_status, is_success, error_message, created, modified)
VALUES (
         'test_url'
       , 'user1'
       , 'content'
       , 'raw_content'
       , '{"test": "hello", "code": "world"}'
       , 'SLACK'
       , 203
       , true
       , ''
       , now()
       , now()
);

INSERT INTO webhook_history (url, user_id, content, raw_content, params, platform, http_status, is_success, error_message, created, modified)
VALUES (
         'test_url'
       , 'user1'
       , 'content'
       , 'raw_content'
       , '{"test": "hello", "code": "world"}'
       , 'SLACK'
       , 203
       , true
       , ''
       , now()
       , now()
);

INSERT INTO webhook_history (url, user_id, content, raw_content, params, platform, http_status, is_success, error_message, created, modified)
VALUES (
         'test_url'
       , 'user1'
       , 'content'
       , 'raw_content'
       , '{"test": "hello", "code": "world"}'
       , 'SLACK'
       , 203
       , true
       , ''
       , now()
       , now()
);
