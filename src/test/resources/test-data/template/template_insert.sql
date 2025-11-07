ALTER SEQUENCE template_id_seq RESTART WITH 1;

INSERT INTO template (
                       title
                     , user_id
                     , content
                     , raw_content
                     , discord
                     , slack
                     , params
                     , created
                     , modified
)
VALUES (
         'test title1'
       , 'user1'
       , 'test content'
       , '# [ $date ]
- 달리기: $dist km
- 장소: $place
> 메모: $memo
'
       , 'https://discord.com/api/webhooks/123456789123456/TEST-URL'
       , 'https://hooks.slack.com/services/ABC523F/ABC1234DE/TEST-URL'
       , '{"$date": "2025.11.02", "$dist": "3", "$memo": "2C 는 너무 춥다", "$place": "한내천"}'
       , now()
       , now()
);

INSERT INTO template (
                       title
                     , user_id
                     , content
                     , raw_content
                     , discord
                     , slack
                     , params
                     , created
                     , modified
)
VALUES (
         'test title1'
       , 'user1'
       , 'test content'
       , ''
       , 'https://discord.com/api/webhooks/123456789123456/TEST-URL'
       , 'https://hooks.slack.com/services/ABC523F/ABC1234DE/TEST-URL'
       , '{}'
       , now()
       , now()
       );

INSERT INTO template (
                       title
                     , user_id
                     , content
                     , raw_content
                     , discord
                     , slack
                     , params
                     , created
                     , modified
)
VALUES (
         'test title1'
       , 'user1'
       , 'test content'
       , ''
       , 'https://discord.com/api/webhooks/123456789123456/TEST-URL'
       , 'https://hooks.slack.com/services/ABC523F/ABC1234DE/TEST-URL'
       , '{}'
       , now()
       , now()
       );

INSERT INTO template (
                       title
                     , user_id
                     , content
                     , raw_content
                     , discord
                     , slack
                     , params
                     , created
                     , modified
)
VALUES (
         'test title2'
       , 'user2'
       , 'test content'
       , ''
       , 'https://discord.com/api/webhooks/123456789123456/TEST-URL'
       , 'https://hooks.slack.com/services/ABC523F/ABC1234DE/TEST-URL'
       , '{}'
       , now()
       , now()
       );


INSERT INTO template (
                       title
                     , user_id
                     , content
                     , raw_content
                     , discord
                     , slack
                     , params
                     , created
                     , modified
)
VALUES (
         'test title3'
       , 'user3'
       , 'test content'
       , ''
       , 'https://discord.com/api/webhooks/123456789123456/TEST-URL'
       , 'https://hooks.slack.com/services/ABC523F/ABC1234DE/TEST-URL'
       , '{}'
       , now()
       , now()
       );

