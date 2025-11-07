ALTER SEQUENCE post_id_seq RESTART WITH 1;

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user1', NULL, 'test title1', 'test content1', 'NORMAL', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user1', NULL, 'test title2', 'test content3', 'NORMAL', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user1', NULL, 'test title3', 'test content3', 'NORMAL', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user1', NULL, 'test title4', 'test content4', 'DELETED', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user1', NULL, 'test title5', 'test content5', 'DELETED', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user2', NULL, 'test title6', 'test content6', 'NORMAL', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user2', NULL, 'test title7', 'test content7', 'NORMAL', now(), now());

INSERT INTO post (user_id, template_id, title, content, status, created, modified)
VALUES ('user3', NULL, 'test title8', 'test content8', 'NORMAL', now(), now());