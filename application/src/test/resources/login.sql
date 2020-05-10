--CREATE TABLE public.login (
--  id bigint NOT NULL,
--  ip_address character varying(255) NULL,
--  last_login timestamp without time zone NULL,
--  success boolean NULL,
--  username character varying(50) NOT NULL
--);
--
--ALTER TABLE public.login ADD CONSTRAINT login_pkey PRIMARY KEY (id)

INSERT INTO login (id, username, login_date, ip_address,success ) VALUES (1, 'kong@gmail.com', '2020-01-22', '192.168.0.1', true);
INSERT INTO login (id, username, login_date, ip_address,success ) VALUES (2, 'kong@gmail.com', '2020-01-22', '192.168.0.1', false);
