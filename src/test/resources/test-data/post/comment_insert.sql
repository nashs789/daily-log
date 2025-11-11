ALTER SEQUENCE comment_id_seq RESTART WITH 1;

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 1, null, '댓글1', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 1, null, '댓글2', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 1, null, '댓글3', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 1, null, '삭제된 댓글1', 'DELETED', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 1, null, '삭제된 댓글2', 'DELETED', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user2', 1, 1, '대댓글1', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 1, 1, '대댓글2', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user2', 1, 1, '대댓글3', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user3', 1, 1, '대댓글4', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user3', 1, 1, '삭제된 대댓글1', 'DELETED', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user3', 1, 1, '삭제된 대댓글2', 'DELETED', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 2, 2, '대댓글5', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user1', 2, 2, '대댓글6', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user3', 2, 2, '대댓글7', 'NORMAL', now(), now());

INSERT INTO comment (user_id, post_id, parent_id, content, status, created, modified)
VALUES ('user3', 2, 2, '대댓글8', 'NORMAL', now(), now());