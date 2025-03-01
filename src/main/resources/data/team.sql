--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0 (Debian 17.0-1.pgdg120+1)
-- Dumped by pg_dump version 17.0 (Debian 17.0-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: team; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.team (created_at, deleted_at, id, updated_at, name) VALUES ('2025-02-28 09:12:52.422543', NULL, 1, '2025-02-28 09:12:52.422543', '개발 1팀') ON CONFLICT DO NOTHING;
INSERT INTO public.team (created_at, deleted_at, id, updated_at, name) VALUES ('2025-02-28 09:12:52.422543', NULL, 2, '2025-02-28 09:12:52.422543', '개발 2팀') ON CONFLICT DO NOTHING;
INSERT INTO public.team (created_at, deleted_at, id, updated_at, name) VALUES ('2025-02-28 09:12:52.422543', NULL, 3, '2025-02-28 09:12:52.422543', '디자인 1팀') ON CONFLICT DO NOTHING;
INSERT INTO public.team (created_at, deleted_at, id, updated_at, name) VALUES ('2025-02-28 09:12:52.422543', NULL, 4, '2025-02-28 09:12:52.422543', '디자인 2팀') ON CONFLICT DO NOTHING;


--
-- Name: team_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.team_id_seq', 4, true);


--
-- PostgreSQL database dump complete
